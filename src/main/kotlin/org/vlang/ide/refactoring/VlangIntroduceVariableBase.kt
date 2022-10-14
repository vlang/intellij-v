package org.vlang.ide.refactoring

import com.intellij.codeInsight.PsiEquivalenceUtil
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.util.Conditions
import com.intellij.openapi.util.Pass
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import com.intellij.refactoring.IntroduceTargetChooser
import com.intellij.refactoring.RefactoringBundle
import com.intellij.refactoring.introduce.inplace.InplaceVariableIntroducer
import com.intellij.refactoring.introduce.inplace.OccurrencesChooser
import com.intellij.refactoring.util.CommonRefactoringUtil
import com.intellij.util.ArrayUtil
import com.intellij.util.containers.ContainerUtil
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangElementFactory

open class VlangIntroduceVariableBase {
    fun performAction(operation: VlangIntroduceOperation) {
        val selectionModel = operation.editor.selectionModel
        val hasSelection = selectionModel.hasSelection()

        var expression = if (hasSelection) {
            findExpressionInSelection(operation.file, selectionModel.selectionStart, selectionModel.selectionEnd)
        } else {
            findExpressionAtOffset(operation)
        }

        if (expression is VlangParenthesesExpr)
            expression = expression.expression

        if (expression == null) {
            val message =
                RefactoringBundle.message(if (hasSelection) "selected.block.should.represent.an.expression" else "refactoring.introduce.selection.error")
            showCannotPerform(operation, message)
            return
        }

        val extractableExpressions = collectExtractableExpressions(expression)
        if (extractableExpressions.isEmpty()) {
            showCannotPerform(operation, RefactoringBundle.message("refactoring.introduce.context.error"))
            return
        }

        // TODO: check for return count

        if (extractableExpressions.size == 1 || hasSelection || ApplicationManager.getApplication().isUnitTestMode) {
            operation.expression = ContainerUtil.getFirstItem(extractableExpressions)
            performOnElement(operation)
            return
        }

        IntroduceTargetChooser.showChooser(operation.editor, extractableExpressions, object : Pass<VlangExpression>() {
            override fun pass(expression: VlangExpression) {
                if (!expression.isValid) return

                operation.expression = expression
                performOnElement(operation)
            }
        }) {
            if (it.isValid)
                it.text
            else
                "<invalid expression>"
        }
    }

    private fun findExpressionInSelection(file: PsiFile, start: Int, end: Int): VlangExpression? {
        return PsiTreeUtil.findElementOfClassAtRange(file, start, end, VlangExpression::class.java)
    }

    private fun findExpressionAtOffset(operation: VlangIntroduceOperation): VlangExpression? {
        val file = operation.file
        val offset = operation.editor.caretModel.offset
        val expr = PsiTreeUtil.getNonStrictParentOfType(file.findElementAt(offset), VlangExpression::class.java)
        val preExpr = PsiTreeUtil.getNonStrictParentOfType(file.findElementAt(offset - 1), VlangExpression::class.java)
        return if (expr == null || preExpr != null && PsiTreeUtil.isAncestor(expr, preExpr, false)) preExpr else expr
    }

    private fun collectExtractableExpressions(expression: VlangExpression): List<VlangExpression> {
        if (expression.parentOfType<VlangStatement>() == null) {
            return emptyList()
        }

        return SyntaxTraverser.psiApi().parents(expression).takeWhile(
            Conditions.notInstanceOf(
                VlangFile::class.java
            )
        )
            .filter(VlangExpression::class.java)
            .filter(Conditions.notInstanceOf(VlangParenthesesExpr::class.java))
            .filter { it !is VlangReferenceExpression || it.getParent() !is VlangCallExpr }
            .toList()
    }

    private fun getLocalOccurrences(element: PsiElement): List<PsiElement?> {
        return getOccurrences(element, PsiTreeUtil.getTopmostParentOfType(element, VlangBlock::class.java))
    }

    private fun getOccurrences(pattern: PsiElement, context: PsiElement?): List<PsiElement> {
        if (context == null) return emptyList()
        val occurrences = mutableListOf<PsiElement>()
        val visitor = object : PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                if (PsiEquivalenceUtil.areElementsEquivalent(element, pattern)) {
                    occurrences.add(element)
                    return
                }
                super.visitElement(element)
            }
        }
        context.acceptChildren(visitor)
        return occurrences
    }

    private fun performOnElement(operation: VlangIntroduceOperation) {
        val expression = operation.expression ?: return
        operation.occurrences = getLocalOccurrences(expression)
        val editor = operation.editor

        if (editor.settings.isVariableInplaceRenameEnabled) {
            operation.name = "name" // TODO: suggest names?
            if (ApplicationManager.getApplication().isUnitTestMode) {
                performInplaceIntroduce(operation)
                return
            }

            OccurrencesChooser.simpleChooser<PsiElement>(editor)
                .showChooser(expression, operation.occurrences, object : Pass<OccurrencesChooser.ReplaceChoice>() {
                    override fun pass(choice: OccurrencesChooser.ReplaceChoice) {
                        performInplaceIntroduce(operation)
                    }
                })

            return
        }

        val dialog = VlangIntroduceVariableDialog(operation)
        if (dialog.showAndGet()) {
            operation.name = dialog.name
            performReplace(operation)
        }
    }

    private fun performInplaceIntroduce(operation: VlangIntroduceOperation) {
        if (!operation.expression!!.isValid) {
            showCannotPerform(operation, RefactoringBundle.message("refactoring.introduce.context.error"))
            return
        }
        performReplace(operation)
        VlangInplaceVariableIntroducer(operation).performInplaceRefactoring(null)
    }

    private fun performReplace(operation: VlangIntroduceOperation) {
        val project = operation.project
        val expression = operation.expression ?: return
        val occurrences = listOf(expression)
        val anchor = findLocalAnchor(occurrences)
        if (anchor == null) {
            showCannotPerform(operation, RefactoringBundle.message("refactoring.introduce.context.error"))
            return
        }
        val context = anchor.parent as VlangBlock
        val name = operation.name
        val newOccurrences = mutableListOf<PsiElement>()

        WriteCommandAction.runWriteCommandAction(project) {
            val declarationStatement = VlangElementFactory.createVariableDeclarationStatement(project, name, expression)
            val newLine = VlangElementFactory.createNewLine(project)
            val statement = context.addBefore(declarationStatement, context.addBefore(newLine, anchor))
            val varDefinition = PsiTreeUtil.findChildOfType(statement, VlangVarDefinition::class.java)!!

            assert(varDefinition.isValid) { "invalid var `" + varDefinition.text + "` definition in `" + statement.text + "`" }

            operation.variable = varDefinition
            var firstOccurrence = true

            for (occurrence in occurrences) {
                var occ = occurrence
                val occurrenceParent = occ.parent
                if (occurrenceParent is VlangParenthesesExpr) {
                    occ = occurrenceParent
                }
                if (firstOccurrence) {
                    firstOccurrence = false
                    val parent = occ.parent
                    // single-expression statement
                    if (parent is VlangLeftHandExprList && parent.parent is VlangSimpleStatement) {
                        parent.parent.delete()
                        continue
                    }
                }
                newOccurrences.add(occ.replace(VlangElementFactory.createReferenceExpression(project, name)))
            }
            operation.editor.caretModel.moveToOffset(varDefinition.getIdentifier().textRange.startOffset)
        }

        operation.occurrences = newOccurrences
        PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(operation.editor.document)
    }

    private fun findLocalAnchor(occurrences: List<PsiElement?>): PsiElement? {
        return findAnchor(
            occurrences,
            PsiTreeUtil.getNonStrictParentOfType(PsiTreeUtil.findCommonParent(occurrences), VlangBlock::class.java)
        )
    }

    private fun findAnchor(occurrences: List<PsiElement?>, context: PsiElement?): PsiElement? {
        val first = ContainerUtil.getFirstItem(occurrences)
        var statement: PsiElement? = PsiTreeUtil.getNonStrictParentOfType(first, VlangStatement::class.java)
        while (statement != null && statement.parent !== context) {
            statement = statement.parent
        }
        return statement
    }

    private fun showCannotPerform(operation: VlangIntroduceOperation, message: String) {
        val msg = RefactoringBundle.getCannotRefactorMessage(message)
        CommonRefactoringUtil.showErrorHint(
            operation.project, operation.editor, msg,
            RefactoringBundle.getCannotRefactorMessage(null), "refactoring.extractVariable"
        )
    }

    private class VlangInplaceVariableIntroducer(operation: VlangIntroduceOperation) :
        InplaceVariableIntroducer<PsiElement?>(
            operation.variable, operation.editor, operation.project, "Introduce Variable",
            ArrayUtil.toObjectArray(operation.occurrences, PsiElement::class.java), null
        )
}
