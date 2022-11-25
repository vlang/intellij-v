package org.vlang.ide.intentions

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.*

class VlangMakePrivateIntention : BaseIntentionAction() {
    override fun startInWriteAction() = true
    override fun getFamilyName() = "Make private"
    override fun getText() = "Make private"

    override fun isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean {
        val identifier = file.findElementAt(editor.caretModel.offset) ?: return false
        val parentElement = identifier.parentOfType<VlangNamedElement>() ?: return false
        if (parentElement !is VlangStructDeclaration &&
            parentElement !is VlangInterfaceDeclaration &&
            parentElement !is VlangEnumDeclaration &&
            parentElement !is VlangConstDefinition &&
            parentElement !is VlangFunctionOrMethodDeclaration &&
            parentElement !is VlangTypeAliasDeclaration
        ) {
            return false
        }
        return parentElement.getIdentifier() == identifier && parentElement.isPublic()
    }

    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        val element = file.findElementAt(editor.caretModel.primaryCaret.offset)!!.parentOfType<VlangNamedElement>()!!
        element.makePrivate()
    }
}
