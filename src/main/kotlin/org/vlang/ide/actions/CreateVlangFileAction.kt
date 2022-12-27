package org.vlang.ide.actions

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import org.vlang.ide.ui.VIcons

class CreateVlangFileAction : CreateFileFromTemplateAction(
    "V File",
    "Create new V file",
    VIcons.V
) {
    override fun isAvailable(dataContext: DataContext): Boolean {
        val project = CommonDataKeys.PROJECT.getData(dataContext)
        return super.isAvailable(dataContext) && project != null
    }

    override fun getActionName(dir: PsiDirectory, newName: String, tpl: String) = "Create V File '$newName'"

    override fun buildDialog(
        project: Project,
        directory: PsiDirectory,
        builder: CreateFileFromTemplateDialog.Builder
    ) {
        builder
            .setTitle("New V File")
            .addKind("V file", VIcons.V, "V File")
            .addKind("V simple application", VIcons.V, "V Simple Application")
            .addKind("V shell script", VIcons.Vsh, "V Shell Script")
    }
}
