package org.vlang.ide.intentions

import com.intellij.codeInsight.intention.BaseElementAtCaretIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

abstract class VlangBaseIntention : BaseElementAtCaretIntentionAction() {
    protected open fun isAvailableInCodeFragment(): Boolean = true

    override fun isAvailable(project: Project, editor: Editor, element: PsiElement): Boolean {
        return isAvailableInCodeFragment() // TODO: || element.containingFile !is VlangCodeFragment
    }
}
