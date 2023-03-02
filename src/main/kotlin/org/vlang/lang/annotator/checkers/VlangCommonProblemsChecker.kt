package org.vlang.lang.annotator.checkers

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangElementFactory
import org.vlang.lang.psi.impl.VlangLangUtil
import org.vlang.lang.psi.types.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx

class VlangCommonProblemsChecker(holder: AnnotationHolder) : VlangCheckerBase(holder) {
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

    companion object {
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
    }
}
