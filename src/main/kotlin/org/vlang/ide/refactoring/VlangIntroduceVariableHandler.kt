package org.vlang.ide.refactoring

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.refactoring.RefactoringActionHandler
import com.intellij.refactoring.util.CommonRefactoringUtil

class VlangIntroduceVariableHandler : VlangIntroduceVariableBase(), RefactoringActionHandler {
    override fun invoke(project: Project, editor: Editor, file: PsiFile, dataContext: DataContext) {
        if (!CommonRefactoringUtil.checkReadOnlyStatus(file)) return
        performAction(VlangIntroduceOperation(project, editor, file))
    }

    override fun invoke(project: Project, elements: Array<PsiElement>, dataContext: DataContext) {}
}
