package org.vlang.lang.psi.impl

import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangImportSpec
import org.vlang.lang.psi.VlangReferenceExpression

object VlangPsiImplUtil {
    private val LOG = Logger.getInstance(
        VlangPsiImplUtil::class.java
    )

    @JvmStatic
    fun getReference(o: VlangReferenceExpression): VlangReference {
        return VlangReference(o)
    }

    @JvmStatic
    fun getIdentifier(o: VlangImportSpec): PsiElement {
        return o.firstChild
    }
}