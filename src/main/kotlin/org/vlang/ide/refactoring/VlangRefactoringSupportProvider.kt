package org.vlang.ide.refactoring

import com.intellij.lang.refactoring.RefactoringSupportProvider
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangNamedElement

class VlangRefactoringSupportProvider : RefactoringSupportProvider() {
    override fun isMemberInplaceRenameAvailable(element: PsiElement, context: PsiElement?) =
        element is VlangNamedElement

    override fun getIntroduceVariableHandler() = VlangIntroduceVariableHandler()
}
