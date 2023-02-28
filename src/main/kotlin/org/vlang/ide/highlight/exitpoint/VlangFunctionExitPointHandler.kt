package org.vlang.ide.highlight.exitpoint

import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerBase
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.Consumer
import org.vlang.lang.psi.*

class VlangFunctionExitPointHandler(editor: Editor, file: PsiFile, private val target: PsiElement, private val function: VlangTypeOwner) :
    HighlightUsagesHandlerBase<PsiElement>(editor, file) {

    override fun getTargets() = listOf(target)

    override fun selectTargets(targets: List<PsiElement>, selectionConsumer: Consumer<in MutableList<out PsiElement>?>) {
        selectionConsumer.consume(targets.toMutableList())
    }

    override fun computeUsages(targets: List<PsiElement>) {
        if (function is VlangFunctionOrMethodDeclaration) {
            val identifier = function.getIdentifier()
            if (identifier != null) {
                addOccurrence(identifier)
            }
        }

        val visitor = object : VlangRecursiveVisitor() {
            override fun visitFunctionLit(literal: VlangFunctionLit) {}
            override fun visitReturnStatement(statement: VlangReturnStatement) {
                addOccurrence(statement)
            }

            override fun visitCallExpr(o: VlangCallExpr) {
                val callerName = o.expression?.text
                if (callerName == "panic" || callerName == "exit") {
                    addOccurrence(o)
                }
            }
        }

        visitor.visitTypeOwner(function)
    }

    companion object {
        fun createForElement(editor: Editor, file: PsiFile, element: PsiElement): VlangFunctionExitPointHandler? {
            val function = PsiTreeUtil.getParentOfType(element, VlangFunctionLit::class.java, VlangFunctionOrMethodDeclaration::class.java)
            return function?.let { VlangFunctionExitPointHandler(editor, file, element, it) }
        }
    }
}
