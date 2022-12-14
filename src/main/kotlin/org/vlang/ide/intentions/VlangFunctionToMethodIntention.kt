package org.vlang.ide.intentions

import com.intellij.codeInsight.intention.LowPriorityAction
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import com.intellij.refactoring.suggested.startOffset
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.VlangFunctionDeclaration

class VlangFunctionToMethodIntention : BaseIntentionAction(), LowPriorityAction {
    override fun startInWriteAction() = true
    override fun getFamilyName() = "Function to method"
    override fun getText() = "Convert function with parameter to method with receiver"

    override fun isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean {
        val element = file.findElementAt(editor.caretModel.offset) ?: return false
        val parentFunction = element.parentOfType<VlangFunctionDeclaration>() ?: return false
        if (parentFunction.getIdentifier() != element) return false
        val parameters = parentFunction.getSignature()?.parameters?.paramDefinitionList
        return parameters?.isNotEmpty() ?: false
    }

    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        val function = file.findElementAt(editor.caretModel.offset)!!.parentOfType<VlangFunctionDeclaration>()!!
        val firstParam = function.getSignature()!!.parameters.paramDefinitionList.first()
        val receiverText = "(${firstParam.text}) "
        val insertPos = function.getIdentifier().startOffset
        val document = editor.document

        val elementAfterParam = firstParam.nextSibling
        if (elementAfterParam != null && elementAfterParam.elementType == VlangTypes.COMMA) {
            elementAfterParam.delete()

            val elementAfterComma = firstParam.nextSibling
            if (elementAfterComma != null && elementAfterComma is PsiWhiteSpace) {
                elementAfterComma.delete()
            }
        }

        firstParam.delete()
        PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(document)

        document.insertString(insertPos, receiverText)
    }
}
