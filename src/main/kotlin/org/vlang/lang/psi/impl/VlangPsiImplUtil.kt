package org.vlang.lang.psi.impl

import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.openapi.util.Conditions
import com.intellij.psi.PsiElement
import com.intellij.psi.SyntaxTraverser
import org.vlang.lang.psi.VlangFunctionDeclaration
import org.vlang.lang.psi.VlangImportSpec
import org.vlang.lang.psi.VlangModuleClause
import org.vlang.lang.psi.VlangReferenceExpression

object VlangPsiImplUtil {
    @JvmStatic
    fun getName(o: VlangFunctionDeclaration): String {
        return o.getIdentifier()?.text ?: ""
    }

    @JvmStatic
    fun getReference(o: VlangReferenceExpression): VlangReference {
        return VlangReference(o)
    }

    @JvmStatic
    fun getIdentifier(o: VlangImportSpec): PsiElement {
        return o.firstChild
    }


    @JvmStatic
    fun getName(o: VlangModuleClause): String {
        return o.firstChild.text
    }

    fun goTraverser(): SyntaxTraverser<PsiElement?> {
        return SyntaxTraverser.psiTraverser()
            .forceDisregardTypes(Conditions.equalTo(GeneratedParserUtilBase.DUMMY_BLOCK))
    }

}