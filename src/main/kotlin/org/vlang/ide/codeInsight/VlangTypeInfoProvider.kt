package org.vlang.ide.codeInsight

import com.intellij.lang.ExpressionTypeProvider
import com.intellij.openapi.util.Conditions
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.SyntaxTraverser
import com.intellij.psi.util.PsiTreeUtil
import io.ktor.util.*
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangFunctionOrMethodDeclaration
import org.vlang.lang.psi.VlangTypeOwner

class VlangTypeInfoProvider : ExpressionTypeProvider<VlangTypeOwner>() {
    override fun getInformationHint(element: VlangTypeOwner): String {
        val type = element.getType(null) ?: return ""
        return type.readableName(element).escapeHTML().replace(" ", "&nbsp;")
    }

    override fun getErrorHint(): String {
        return "Selection doesn't contain a V expression"
    }

    override fun getExpressionsAt(at: PsiElement): List<VlangTypeOwner?> {
        var element = at
        if (element is PsiWhiteSpace && element.textMatches("\n")) {
            element = PsiTreeUtil.prevLeaf(element)!!
        }
        return SyntaxTraverser.psiApi()
            .parents(element)
            .takeWhile(
                Conditions.notInstanceOf(VlangFile::class.java),
            )
            .filter(VlangTypeOwner::class.java)
            .filter(Conditions.notInstanceOf(VlangFunctionOrMethodDeclaration::class.java))
            .toList()
    }
}
