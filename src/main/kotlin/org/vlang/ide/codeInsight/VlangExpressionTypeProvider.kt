package org.vlang.ide.codeInsight

import com.intellij.lang.ExpressionTypeProvider
import com.intellij.openapi.util.Conditions
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.SyntaxTraverser
import com.intellij.psi.util.PsiTreeUtil
import io.ktor.util.*
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangTypeOwner

class VlangExpressionTypeProvider : ExpressionTypeProvider<VlangTypeOwner>() {
    override fun getInformationHint(element: VlangTypeOwner): String {
        return (element.getType(null)?.text ?: "<unknown>").escapeHTML()
    }

    override fun getErrorHint(): String {
        return "Selection doesn't contain a V expression"
    }

    override fun getExpressionsAt(at: PsiElement): List<VlangTypeOwner?> {
        var at = at
        if (at is PsiWhiteSpace && at.textMatches("\n")) {
            at = PsiTreeUtil.prevLeaf(at)!!
        }
        return SyntaxTraverser.psiApi().parents(at).takeWhile(
            Conditions.notInstanceOf(
                VlangFile::class.java
            )
        ).filter(VlangTypeOwner::class.java).toList()
    }
}
