package org.vlang.lang.psi.impl

import com.intellij.psi.scope.PsiScopeProcessor

abstract class VlangScopeProcessor : PsiScopeProcessor {
    open fun isCompletion(): Boolean = false
    open fun isCodeFragment(): Boolean = false
}
