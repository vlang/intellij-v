package io.vlang.lang.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import io.vlang.ide.codeInsight.VlangAttributesUtil
import io.vlang.lang.psi.VlangFunctionOrMethodDeclaration
import io.vlang.lang.psi.VlangUnsafeExpression

object VlangUnsafeUtil {
    fun insideUnsafe(context: PsiElement): Boolean {
        val unsafe = context.parentOfType<VlangUnsafeExpression>()
        return unsafe != null || insideUnsafeFunction(context)
    }

    fun insideUnsafeFunction(context: PsiElement): Boolean {
        val function = context.parentOfType<VlangFunctionOrMethodDeclaration>() ?: return false
        return VlangAttributesUtil.isUnsafeFunction(function)
    }
}
