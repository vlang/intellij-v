package org.vlang.lang.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.VlangCallExpr
import org.vlang.lang.psi.VlangElement

object VlangLangUtil {
    fun indexInCall(pos: PsiElement): Int? {
        val callExpr = pos.parentOfType<VlangCallExpr>() ?: return null
        val element = pos.parentOfType<VlangElement>()
        val args = callExpr.argumentList.elementList
        return args.indexOf(element).apply { if (this == -1) return null }
    }
}
