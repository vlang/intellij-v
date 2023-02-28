package org.vlang.ide.highlight.exitpoint

import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerBase
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.Consumer
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangLangUtil

class VlangBreakAndContinueStatementsExitPointHandler(
    editor: Editor,
    file: PsiFile,
    private val target: PsiElement,
    private val element: VlangCompositeElement?,
    private val owner: PsiElement?,
) : HighlightUsagesHandlerBase<PsiElement>(editor, file) {
    override fun getTargets() = listOf(target)

    override fun selectTargets(targets: List<PsiElement>, selectionConsumer: Consumer<in List<PsiElement>?>) {
        selectionConsumer.consume(targets)
    }

    override fun computeUsages(targets: List<PsiElement>) {
        val statementOwner = findOwner()
        val visitor = object : VlangRecursiveVisitor() {
            override fun visitLabelDefinition(stmt: VlangLabelDefinition) {
                if (stmt == statementOwner) {
                    addOccurrence(stmt)
                }
                super.visitLabelDefinition(stmt)
            }

            override fun visitBreakStatement(stmt: VlangBreakStatement) {
                if (stmt == element || getStatementOwnerOrResolve(stmt) == statementOwner) {
                    addOccurrence(stmt)
                }
                super.visitBreakStatement(stmt)
            }

            override fun visitContinueStatement(stmt: VlangContinueStatement) {
                if (stmt == element || getStatementOwnerOrResolve(stmt) == statementOwner) {
                    addOccurrence(stmt)
                }
                super.visitContinueStatement(stmt)
            }

            override fun visitForStatement(stmt: VlangForStatement) {
                if (stmt == statementOwner) {
                    addOccurrence(stmt.getFor())
                }
                super.visitForStatement(stmt)
            }

            override fun visitSelectExpression(stmt: VlangSelectExpression) {
                if (stmt == statementOwner) {
                    addOccurrence(stmt.select)
                }
                super.visitSelectExpression(stmt)
            }
        }

        if (statementOwner != null) {
            val parent = statementOwner.parent
            if (parent is VlangCompositeElement) {
                visitor.visitCompositeElement(parent)
            }
        }
    }

    private fun findOwner(): PsiElement? {
        if (owner != null) return owner
        if (element != null) return getStatementOwnerOrResolve(element)
        return null
    }

    private fun getStatementOwnerOrResolve(statement: VlangCompositeElement): PsiElement? {
        if (statement is VlangBreakStatement) {
            val label = statement.labelRef ?: return VlangLangUtil.getBreakStatementOwner(statement)
            return label.reference.resolve()
        }
        if (statement is VlangContinueStatement) {
            val label = statement.labelRef ?: return VlangLangUtil.getContinueStatementOwner(statement)
            return label.reference.resolve()
        }
        return null
    }

    companion object {
        fun createForElement(
            editor: Editor,
            file: PsiFile,
            element: PsiElement,
        ): VlangBreakAndContinueStatementsExitPointHandler? {
            val target = PsiTreeUtil.getParentOfType(
                element,
                VlangBreakStatement::class.java, VlangSelectExpression::class.java,
                VlangForStatement::class.java, VlangContinueStatement::class.java
            ) ?: return null

            return if (target is VlangBreakStatement || target is VlangContinueStatement)
                VlangBreakAndContinueStatementsExitPointHandler(editor, file, element, target, null)
            else
                VlangBreakAndContinueStatementsExitPointHandler(editor, file, element, null, target)
        }
    }
}
