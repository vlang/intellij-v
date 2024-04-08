package org.vlang.ide.intentions

import com.intellij.codeInsight.intention.HighPriorityAction
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ex.util.EditorUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Iconable
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.vlang.ide.refactoring.VlangImplementMethodsHandler.Companion.findStructDeclaration
import org.vlang.ide.ui.VIcons
import javax.swing.Icon

class VlangImplementInterfaceIntention : VlangBaseIntention(), HighPriorityAction, ShortcutProvider, Iconable {
    override fun getText(): String = "Implement interface"

    override fun getFamilyName(): String = "Implement interface"

    override fun isAvailable(project: Project, editor: Editor, element: PsiElement): Boolean {
        val application = ApplicationManager.getApplication()
        return (!application.isHeadlessEnvironment || application.isUnitTestMode) && findStructDeclaration(element) != null
    }

    override operator fun invoke(project: Project, editor: Editor, element: PsiElement) {
        val action = findImplementMethodsAction()
        action?.actionPerformed(
            AnActionEvent.createFromDataContext(
                ActionPlaces.UNKNOWN,
                null,
                EditorUtil.getEditorDataContext(editor)
            )
        )
    }

    override fun getShortcut(): ShortcutSet? {
        val action = findImplementMethodsAction()
        return action?.shortcutSet
    }

    override fun startInWriteAction(): Boolean = false

    override fun checkFile(file: PsiFile): Boolean = true

    override fun getIcon(flags: Int): Icon = VIcons.Interface

    private fun findImplementMethodsAction(): AnAction? {
        return ActionManager.getInstance().getAction("ImplementMethods")
    }
}
