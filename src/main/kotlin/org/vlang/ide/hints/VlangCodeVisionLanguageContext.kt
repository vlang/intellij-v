package org.vlang.ide.hints

import com.intellij.codeInsight.hints.VcsCodeVisionLanguageContext
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.*
import java.awt.event.MouseEvent

@Suppress("UnstableApiUsage")
class VlangCodeVisionLanguageContext : VcsCodeVisionLanguageContext {
    override fun isAccepted(element: PsiElement) = element is VlangFunctionOrMethodDeclaration ||
            element is VlangStructDeclaration ||
            element is VlangInterfaceDeclaration ||
            element is VlangEnumDeclaration ||
            element is VlangConstDeclaration ||
            element is VlangTypeAliasDeclaration ||
            element is VlangGlobalVariableDeclaration

    override fun handleClick(mouseEvent: MouseEvent, editor: Editor, element: PsiElement) {}
}
