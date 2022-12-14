package org.vlang.ide.intentions

import com.intellij.codeInsight.intention.LowPriorityAction
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.parentOfType
import com.intellij.refactoring.suggested.startOffset
import org.vlang.lang.psi.VlangMethodDeclaration

class VlangMethodToFunctionIntention : BaseIntentionAction(), LowPriorityAction {
    override fun startInWriteAction() = true
    override fun getFamilyName() = "Method to function"
    override fun getText() = "Convert method with receiver to function with parameter"

    override fun isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean {
        val element = file.findElementAt(editor.caretModel.offset) ?: return false
        val parentMethod = element.parentOfType<VlangMethodDeclaration>() ?: return false
        return parentMethod.getIdentifier() == element
    }

    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        val method = file.findElementAt(editor.caretModel.primaryCaret.offset)!!.parentOfType<VlangMethodDeclaration>()!!
        val receiver = method.receiver
        val parameters = method.getSignature()!!.parameters
        var receiverText = receiver.text

        receiver.prevSibling?.delete()
        if (receiver.prevSibling is PsiWhiteSpace) {
            receiver.prevSibling?.delete()
        }
        receiver.nextSibling?.delete()
        receiver.delete()

        if (parameters.paramDefinitionList.isNotEmpty()) {
            receiverText += ", "
        }

        val document = editor.document
        PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(document)

        document.insertString(
            parameters.startOffset + 1,
            receiverText
        )
    }
}
