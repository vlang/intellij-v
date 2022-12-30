package org.vlang.lang.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.VlangUnsafeExpression

object VlangUnsafeUtil {
    fun insideUnsafe(context: PsiElement): Boolean {
        val unsafe = context.parentOfType<VlangUnsafeExpression>()
        return unsafe != null
    }
}
