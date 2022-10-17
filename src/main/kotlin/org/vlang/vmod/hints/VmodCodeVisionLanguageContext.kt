package org.vlang.vmod.hints

import com.intellij.codeInsight.hints.VcsCodeVisionLanguageContext
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import org.vlang.lang.vmod.psi.VmodModuleDeclaration
import java.awt.event.MouseEvent

@Suppress("UnstableApiUsage")
class VmodCodeVisionLanguageContext : VcsCodeVisionLanguageContext {
    override fun isAccepted(element: PsiElement) = element is VmodModuleDeclaration

    override fun handleClick(mouseEvent: MouseEvent, editor: Editor, element: PsiElement) {}
}
