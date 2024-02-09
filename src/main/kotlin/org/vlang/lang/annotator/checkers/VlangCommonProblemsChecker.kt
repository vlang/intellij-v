package org.vlang.lang.annotator.checkers

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.findTopmostParentInFile
import com.intellij.psi.util.parentOfType
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangElementFactory
import org.vlang.lang.psi.impl.VlangLangUtil
import org.vlang.lang.psi.types.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx

class VlangCommonProblemsChecker(holder: AnnotationHolder) : VlangCheckerBase(holder) {
    override fun visitCallExpr(call: VlangCallExpr) {
        val resolved = call.resolve() as? VlangSignatureOwner ?: return
        val parameters = resolved.getSignature()?.parameters?.paramDefinitionList ?: return
        val arguments = call.argumentList.elementList

        for (i in 0 until minOf(parameters.size, arguments.size)) {
            val parameter = parameters[i]
            if (parameter.isMutable()) {
                val argument = arguments[i].value?.expression
                if (argument != null && argument is VlangReferenceExpression && argument !is VlangMutExpression) {
                    holder.newAnnotation(
                        HighlightSeverity.ERROR,
                        "Use `mut ${argument.text}` instead of `${argument.text}` to pass a mutable argument"
                    )
                        .withFix(WrapExpressionWithMutQuickFix)
                        .range(argument)
                        .create()
                }
            }
        }
    }

    override fun visitContinueStatement(stmt: VlangContinueStatement) {
        val owner = VlangLangUtil.getContinueStatementOwner(stmt)
        if (owner == null) {
            holder.newAnnotation(HighlightSeverity.ERROR, "'continue' statement is outside the 'for' loop")
                .create()
        }
    }

    override fun visitBreakStatement(o: VlangBreakStatement) {
        val owner = VlangLangUtil.getBreakStatementOwner(o)
        if (owner == null) {
            holder.newAnnotation(HighlightSeverity.ERROR, "'break' statement is outside the 'for' loop")
                .create()
        }
    }

    override fun visitFieldDeclaration(decl: VlangFieldDeclaration) {
        val owner = decl.parentOfType<VlangNamedElement>()
        if (owner is VlangStructDeclaration) {
            val type = decl.type?.toEx() ?: return
            if (type is VlangStructTypeEx && type.anchor() == owner.structType) {
                holder.newAnnotation(
                    HighlightSeverity.ERROR,
                    "Invalid recursive type: ${owner.name} refers to itself, try change the type to '&${owner.name}'"
                ).create()
            }
        }

        if (owner is VlangInterfaceDeclaration) {
            val defaultValue = decl.defaultFieldValue
            if (defaultValue != null) {
                holder.newAnnotation(HighlightSeverity.ERROR, "Interface field cannot have a default value")
                    .range(defaultValue)
                    .create()
            }
        }
    }

    override fun visitSendExpr(expr: VlangSendExpr) {
        val left = expr.left
        val leftType = left.getType(null) ?: return
        if (leftType !is VlangChannelTypeEx) {
            holder.newAnnotation(
                HighlightSeverity.ERROR,
                "Cannot push on non-channel `${leftType.readableName(expr)}`, left expression of 'send' operator must be 'chan T' type"
            )
                .range(expr)
                .create()
        }
    }

    override fun visitAliasType(type: VlangAliasType) {
        val visited = mutableMapOf<VlangType, VlangTypeEx>()
        val typeUnionList = type.typeUnionList?.typeList ?: return
        if (typeUnionList.size == 1) {
            return
        }

        typeUnionList.forEach {
            val unionType = it.toEx(visited)
            if (unionType is VlangInterfaceTypeEx) {
                holder.newAnnotation(
                    HighlightSeverity.ERROR,
                    "Sum type cannot hold an interface types"
                )
                    .range(it)
                    .create()
            }
        }
    }

    override fun visitFunctionLit(fn: VlangFunctionLit) {
        val captureList = fn.captureList
        if (captureList != null) {
            val isEmpty = captureList.captureList.size
            if (isEmpty == 0) {
                holder.newAnnotation(
                    HighlightSeverity.ERROR,
                    "Capture list cannot be empty"
                )
                    .withFix(VlangRemoveEmptyCaptureList)
                    .range(captureList)
                    .create()
            }
        }

        val parameters = fn.getSignature()?.parameters?.paramDefinitionList ?: return
        val captures = fn.captureList?.captureList?.map { it.referenceExpression } ?: return

        val duplicates = captures.filter { capture ->
            parameters.any { param ->
                param.name == capture.text
            }
        }

        // Capture variable X duplicates parameter name Y
        duplicates.forEach {
            val name = it.text
            val param = parameters.first { param -> param.name == name }
            holder.newAnnotation(
                HighlightSeverity.ERROR,
                "Capture variable '${name}' duplicates parameter '${param.name}'"
            )
                .range(it)
                .create()
        }
    }

    override fun visitWrongPointerType(type: VlangWrongPointerType) {
        holder.newAnnotation(
            HighlightSeverity.ERROR,
            "Use '&T' instead of '*T' to define a pointer type"
        )
            .withFix(VlangChangePointerToCorrect)
            .range(type)
            .create()
    }

    override fun visitStructDeclaration(struct: VlangStructDeclaration) {
        val type = struct.structType
        val groups = type.fieldsGroupList

        val usedModifiers = mutableSetOf<String>()
        groups.forEach { group ->
            val modifiers = group.memberModifiers ?: return@forEach
            val name = modifiers.text
            if (usedModifiers.contains(name)) {
                holder.newAnnotation(
                    HighlightSeverity.ERROR,
                    "Duplicate modifier group '$name'"
                )
                    .range(TextRange(modifiers.startOffset, modifiers.endOffset - 1))
                    .create()
            }
            usedModifiers.add(name)
        }
    }

    override fun visitUnfinishedMemberModifiers(modifiers: VlangUnfinishedMemberModifiers) {
        holder.newAnnotation(
            HighlightSeverity.ERROR,
            "Expected ':' after modifiers"
        )
            .range(modifiers)
            .create()
    }

    override fun visitMemberModifiers(modifiers: VlangMemberModifiers) {
        if (modifiers.memberModifierList.size < 2) {
            return
        }

        val parent = modifiers.parent
        val list = modifiers.memberModifierList

        val used = mutableSetOf<String>()
        list.forEach {
            val name = it.text
            if (used.contains(name)) {
                holder.newAnnotation(
                    HighlightSeverity.ERROR,
                    "Duplicate modifier '$name'"
                )
                    .range(it)
                    .create()
            }
            used.add(name)
        }

        if (parent is VlangFieldsGroup && modifiers.memberModifierList.size == 2) {
            val first = list.first()
            val last = list.last()
            if (first.textMatches("mut") && last.textMatches("pub")) {
                holder.newAnnotation(
                    HighlightSeverity.ERROR,
                    "Use 'pub mut' instead of 'mut pub'"
                )
                    .range(modifiers)
                    .withFix(FlipMutPubModifiersQuickFix(modifiers))
                    .create()
            }
        }
    }

    override fun visitStructType(type: VlangStructType) {
        val fieldList = type.fieldsGroupList.flatMap { it.fieldDeclarationList }

        var sawField = false
        fieldList.forEach { field ->
            if (field.embeddedDefinition == null) {
                sawField = true
            } else {
                if (sawField) {
                    holder.newAnnotation(
                        HighlightSeverity.ERROR,
                        "Embedded structs must be defined before any other fields"
                    )
                        .range(field)
                        .create()
                }
            }
        }

        checkEmbeddedStructs(fieldList.mapNotNull { it.embeddedDefinition })
    }

    private fun checkEmbeddedStructs(embedded: List<VlangEmbeddedDefinition>) {
        val names = HashSet<String>()
        for (embed in embedded) {
            val name = embed.text
            if (name != null && !names.add(name)) {
                holder.newAnnotation(
                    HighlightSeverity.ERROR,
                    "Cannot embed '$name' more than once"
                )
                    .range(embed)
                    .create()
            }
        }
    }

    override fun visitMembersGroup(group: VlangMembersGroup) {
        val attributes =
            group.fieldDeclarationList.mapNotNull { it.attribute } +
                    group.interfaceMethodDeclarationList.mapNotNull { it.attribute }

        attributes.forEach {
            holder.newAnnotation(
                HighlightSeverity.ERROR,
                "Attributes are not allowed for interface members"
            )
                .range(it)
                .create()
        }
    }

    override fun visitReturnStatement(ret: VlangReturnStatement) {
        val owner = ret.owner ?: return
        val signature = owner.getSignature() ?: return
        val (countResultTypes, maxAllowedResultTypesCount) = signature.resultCount()
        val countReturnExpressions = ret.expressionList.size

        if (countReturnExpressions == 1 && countResultTypes > 1) {
            val expr = ret.expressionList.first()
            if (expr is VlangCallExpr) {
                val called = expr.resolve() as? VlangSignatureOwner
                val calledFunctionResultCount = called?.getSignature()?.resultCount()
                if (calledFunctionResultCount != null && calledFunctionResultCount.second != maxAllowedResultTypesCount) {
                    holder.newAnnotation(
                        HighlightSeverity.ERROR,
                        "Expected $countResultTypes return values, but '${expr.text}' returns ${calledFunctionResultCount.second}'"
                    )
                        .range(ret)
                        .create()
                    return
                }

                if (calledFunctionResultCount != null) {
                    return
                }
            }
        }

        val results = ret.expressionList.mapNotNull { expr ->
            if (expr is VlangCallExpr) {
                val resolved = expr.resolve()
                if (resolved !is VlangSignatureOwner) {
                    // for example casts like `i32(1)`
                    return@mapNotNull 1 to 1
                }

                resolved.getSignature()?.resultCount()
            } else {
                1 to 1
            }
        }
        val countReturnValues = results.sumOf { it.first }
        val maxCountReturnExpressions = results.sumOf { it.second }

        if (countResultTypes != countReturnValues && maxAllowedResultTypesCount != maxCountReturnExpressions) {
            if (countReturnValues > countResultTypes && countReturnValues <= maxAllowedResultTypesCount) {
                return
            }

            val expectedCount =
                if (maxAllowedResultTypesCount != countResultTypes) "$countResultTypes or $maxAllowedResultTypesCount" else "$countResultTypes"

            val messageBegin = if (countReturnValues > countResultTypes) "Too many return values" else "Not enough return values"
            holder.newAnnotation(
                HighlightSeverity.ERROR,
                "$messageBegin, expected $expectedCount, got $countReturnValues"
            )
                .range(ret)
                .create()
        }
    }

    override fun visitTupleType(tuple: VlangTupleType) {
        val countTypes = tuple.typeListNoPin.typeList.size
        if (countTypes == 1) {
            holder.newAnnotation(
                HighlightSeverity.WEAK_WARNING,
                "Redundant parentheses"
            )
                .withFix(UnwrapQuickFix(tuple.parent))
                .range(tuple)
                .create()
        }
    }

    override fun visitForStatement(stmt: VlangForStatement) {
        val rangeClause = stmt.rangeClause ?: return
        val right = rangeClause.expression ?: return
        val rightType = right.getType(null) ?: return
        val variables = rangeClause.variablesList

        if (rightType is VlangMapTypeEx && variables.size == 1) {
            holder.newAnnotation(
                HighlightSeverity.ERROR,
                "Specify both `key` and `value` variables to iterate over `${rightType.readableName(stmt)}`, use `_` to ignore the key"
            )
                .range(stmt.`for`)
                .withFix(VlangAddKeyToLoopIterateQuickFix("key"))
                .withFix(VlangAddKeyToLoopIterateQuickFix("_"))
                .create()
        }
    }

    override fun visitIfExpression(expr: VlangIfExpression) {
        val ifExpr = expr.findTopmostParentInFile(true) { it is VlangIfExpression } ?: return
        val isStatement = ifExpr.parent is VlangLeftHandExprList && ifExpr.parent.parent is VlangStatement
        if (!isStatement) {
            val hasElse = expr.elseBranch != null
            if (!hasElse) {
                // TODO
//                holder.newAnnotation(
//                    HighlightSeverity.ERROR,
//                    "If must have an else branch when used as an expression"
//                )
//                    .range(expr.`if`)
//                    .withFix(AddElseQuickFix(expr))
//                    .create()
            }
        }
    }

    override fun visitVarDeclaration(decl: VlangVarDeclaration) {
        if (decl is VlangRangeClause) return

        val left = decl.varDefinitionList
        val right = decl.expressionList

        checkListCountMismatch(decl, left, right)
    }

    override fun visitAssignmentStatement(assign: VlangAssignmentStatement) {
        val left = assign.leftHandExprList.expressionList
        val right = assign.expressionList

        checkListCountMismatch(assign, left, right)
    }

    private fun checkListCountMismatch(
        context: PsiElement,
        left: List<PsiElement>,
        right: List<VlangExpression>,
    ) {
        val hasCallsRight =
            right.any { it is VlangCallExpr || it is VlangOrBlockExpr || it is VlangIfExpression || it is VlangMatchExpression }
        if (!hasCallsRight) {
            if (left.size != right.size) {
                holder.newAnnotation(
                    HighlightSeverity.ERROR,
                    "Expected ${left.size} values on the right side, got ${right.size}"
                )
                    .range(context)
                    .create()
            }
        }
    }

    override fun visitElement(element: VlangElement) {
        val parent = element.parent
        if (parent !is VlangLiteralValueExpression) return

        val isArrayInit = parent.type is VlangArrayType || parent.type is VlangFixedSizeArrayType
        if (!isArrayInit) return

        val name = element.key?.fieldName?.text ?: return
        if (name != "init") return

        val value = element.value ?: return
        val checker = object : VlangRecursiveVisitor() {
            override fun visitReferenceExpression(ref: VlangReferenceExpression) {
                if (ref.textMatches(VlangCodeInsightUtil.IT_VARIABLE)) {
                    holder.newAnnotation(
                        HighlightSeverity.WARNING,
                        "'it' is deprecated, use 'index' instead"
                    )
                        .highlightType(ProblemHighlightType.LIKE_DEPRECATED)
                        .withFix(VlangReplaceItVariableWithIndex(ref))
                        .range(ref)
                        .create()
                }
            }
        }

        value.accept(checker)
    }

    override fun visitEnumType(enum: VlangEnumType) {
        val identifier = enum.identifier ?: return
        val fields = enum.fieldList
        if (fields.isEmpty()) {
            holder.newAnnotation(
                HighlightSeverity.WARNING,
                "Enum must have at least one field"
            )
                .range(identifier)
                .create()
        }
    }

    companion object {
        class VlangReplaceItVariableWithIndex(element: PsiElement) : LocalQuickFixAndIntentionActionOnPsiElement(element) {
            override fun getFamilyName() = "Replace 'it' with 'index'"
            override fun getText() = "Replace 'it' with 'index'"

            override fun invoke(project: Project, file: PsiFile, editor: Editor?, startElement: PsiElement, endElement: PsiElement) {
                val ref = startElement as? VlangReferenceExpression ?: return
                ref.reference.handleElementRename("index")
            }
        }

        class VlangAddKeyToLoopIterateQuickFix(val name: String) : BaseIntentionAction() {
            override fun startInWriteAction() = true
            override fun getFamilyName() = "Add `$name` variable to loop iterate over map"
            override fun getText() = "Add `$name` variable to loop iterate over map"
            override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean = true

            override fun invoke(project: Project, editor: Editor, file: PsiFile) {
                val element = file.findElementAt(editor.caretModel.primaryCaret.offset)!!.parentOfType<VlangForStatement>() ?: return
                val rangeClause = element.rangeClause ?: return
                val variables = rangeClause.variablesList
                val key = VlangElementFactory.createVariableDefinition(project, name)
                val value = variables[0]
                rangeClause.addBefore(key, value)
                rangeClause.addBefore(VlangElementFactory.createComma(project), value)
                rangeClause.addBefore(VlangElementFactory.createSpace(project), value)
            }
        }

        object VlangRemoveEmptyCaptureList : BaseIntentionAction() {
            override fun startInWriteAction() = true
            override fun getFamilyName() = "Remove empty capture list"
            override fun getText() = "Remove empty capture list"
            override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean = true

            override fun invoke(project: Project, editor: Editor, file: PsiFile) {
                val element = file.findElementAt(editor.caretModel.primaryCaret.offset)!!.parentOfType<VlangFunctionLit>() ?: return
                val captureList = element.captureList ?: return
                val nextWhitespace = captureList.nextSibling

                if (nextWhitespace is PsiWhiteSpace) {
                    nextWhitespace.delete()
                }
                captureList.delete()
            }
        }

        object VlangChangePointerToCorrect : BaseIntentionAction() {
            override fun startInWriteAction() = true
            override fun getFamilyName() = "Change '*' to '&'"
            override fun getText() = "Change '*' to '&'"
            override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean = true

            override fun invoke(project: Project, editor: Editor, file: PsiFile) {
                val element = file.findElementAt(editor.caretModel.primaryCaret.offset)!!.parentOfType<VlangWrongPointerType>() ?: return
                val text = element.text
                val correct = text.replace("*", "&")
                val correctType = VlangElementFactory.createType(project, correct)
                element.replace(correctType)
            }
        }

        class UnwrapQuickFix(element: PsiElement) : LocalQuickFixAndIntentionActionOnPsiElement(element) {
            override fun startInWriteAction() = true
            override fun getFamilyName() = "Unwrap parentheses"
            override fun getText() = "Unwrap parentheses"

            override fun invoke(project: Project, file: PsiFile, editor: Editor?, startElement: PsiElement, endElement: PsiElement) {
                if (startElement !is VlangResult) return
                val text = startElement.text
                val withoutParens = text.removeSurrounding("(", ")")
                val newResult = VlangElementFactory.createFunctionResult(project, withoutParens)
                startElement.replace(newResult)
            }
        }

        class FlipMutPubModifiersQuickFix(element: PsiElement) : LocalQuickFixAndIntentionActionOnPsiElement(element) {
            override fun startInWriteAction() = true
            override fun getFamilyName() = "Flip mut/pub modifiers"
            override fun getText() = "Flip mut/pub modifiers"

            override fun invoke(project: Project, file: PsiFile, editor: Editor?, startElement: PsiElement, endElement: PsiElement) {
                if (startElement !is VlangMemberModifiers) return
                val list = startElement.memberModifierList

                val first = list.first()
                val last = list.last()
                val lastCopy = list.last().copy()

                last.replace(first)
                first.replace(lastCopy)
            }
        }

        class AddElseQuickFix(element: PsiElement) : LocalQuickFixAndIntentionActionOnPsiElement(element) {
            override fun startInWriteAction() = true
            override fun getFamilyName() = "Add else branch"
            override fun getText() = "Add else branch"

            override fun invoke(project: Project, file: PsiFile, editor: Editor?, startElement: PsiElement, endElement: PsiElement) {
                if (startElement !is VlangIfExpression) return

                val document = PsiDocumentManager.getInstance(project).getDocument(file) ?: return
                val startLine = document.getLineNumber(startElement.textOffset)
                val endLine = document.getLineNumber(startElement.textRange.endOffset)
                val isOneLine = startLine == endLine
                val elseText = if (isOneLine) " else {  }" else " else {\n\t\n}"

                val elseBranch = VlangElementFactory.createElseBranch(project, elseText)
                startElement.add(VlangElementFactory.createSpace(project))
                val addedElseBranch = startElement.add(elseBranch)

                if (editor != null) {
                    val shiftBack = if (isOneLine) 2 else 3
                    val offset = addedElseBranch.textOffset + addedElseBranch.textLength - shiftBack
                    editor.caretModel.moveToOffset(offset)
                    editor.selectionModel.removeSelection()
                }
            }
        }

        object WrapExpressionWithMutQuickFix : BaseIntentionAction() {
            override fun startInWriteAction(): Boolean = true
            override fun getText(): String = "Wrap expression with `mut`"
            override fun getFamilyName(): String = "Wrap expression with `mut`"
            override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean = true

            override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
                val element = file?.findElementAt(editor?.caretModel?.offset ?: 0) ?: return
                val elem = element.parentOfType<VlangElement>() ?: return
                val expr = elem.value?.expression ?: return
                val mutExpr = VlangElementFactory.createMutExpression(expr.project, expr.text)
                expr.replace(mutExpr)
            }
        }
    }
}
