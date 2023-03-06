package org.vlang.ide.highlight.exitpoint

import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerBase
import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerFactoryBase
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.tree.TokenSet
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.VlangReferenceExpression

class VlangHighlightExitPointsHandlerFactory : HighlightUsagesHandlerFactoryBase() {
    override fun createHighlightUsagesHandler(editor: Editor, file: PsiFile, target: PsiElement): HighlightUsagesHandlerBase<*>? {
        if (target !is LeafPsiElement) return null

        val elementType = target.elementType
        if (elementType == VlangTypes.RETURN || elementType == VlangTypes.FN) {
            return VlangFunctionExitPointHandler.createForElement(editor, file, target)
        }

        if (elementType == VlangTypes.IDENTIFIER && target.parent is VlangReferenceExpression) {
            val callerName = (target.parent as VlangReferenceExpression).getIdentifier().text
            if (callerName == "panic" || callerName == "exit") {
                return VlangFunctionExitPointHandler.createForElement(editor, file, target)
            }
        }

        if (BREAK_HIGHLIGHTING_TOKENS.contains(elementType)) {
            return VlangBreakAndContinueStatementsExitPointHandler.createForElement(editor, file, target)
        }

        return null
    }

    companion object {
        private val BREAK_HIGHLIGHTING_TOKENS = TokenSet.create(VlangTypes.CONTINUE, VlangTypes.BREAK, VlangTypes.FOR, VlangTypes.SELECT)
    }
}
