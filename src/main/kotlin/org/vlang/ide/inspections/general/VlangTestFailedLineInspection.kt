package org.vlang.ide.inspections.general

import com.intellij.codeInsight.intention.preview.IntentionPreviewInfo
import com.intellij.codeInspection.*
import com.intellij.execution.Executor
import com.intellij.execution.ProgramRunnerUtil
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionUtil
import com.intellij.openapi.editor.colors.CodeInsightColors
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Iconable
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import com.intellij.util.ui.UIUtil
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.ide.test.configuration.VlangTestLineMarkerProvider
import org.vlang.lang.psi.VlangAssertStatement
import org.vlang.lang.psi.VlangFunctionDeclaration
import org.vlang.lang.psi.VlangVisitor
import javax.swing.Icon

class VlangTestFailedLineInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitFunctionDeclaration(function: VlangFunctionDeclaration) {
                val state = VlangTestLineMarkerProvider.getTestState(function) ?: return

                val document = PsiDocumentManager.getInstance(function.project).getDocument(function.containingFile)
                if (document == null || state.failedLine < 0 || state.failedLine > document.lineCount) {
                    return
                }

                val failedLine = state.failedLine - 1
                val lineStartOffset = document.getLineStartOffset(failedLine)
                val element = function.containingFile.findElementAt(lineStartOffset) ?: return
                val assert = PsiTreeUtil.nextCodeLeaf(element)?.parentOfType<VlangAssertStatement>() ?: return

                val quickFixes = mutableListOf<LocalQuickFix>()
                quickFixes.add(RunActionFix(function))
                val descriptor = InspectionManager.getInstance(holder.project).createProblemDescriptor(
                    assert.expressionList.first(), state.errorMessage, quickFixes.toTypedArray(), ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                    holder.isOnTheFly, false
                )
                descriptor.setTextAttributes(CodeInsightColors.RUNTIME_ERROR)
                holder.registerProblem(descriptor)
            }
        }
    }

    private class RunActionFix(element: PsiElement) : LocalQuickFix, Iconable {
        private val executor: Executor = DefaultRunExecutor.getRunExecutorInstance()
        private val configuration: RunnerAndConfigurationSettings? = ConfigurationContext(element).configuration

        override fun getFamilyName(): String =
            UIUtil.removeMnemonic(executor.getStartActionText(ProgramRunnerUtil.shortenName(configuration!!.name, 0)))

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val context = ConfigurationContext(descriptor.psiElement).configuration ?: return
            ExecutionUtil.runConfiguration(context, executor)
        }

        override fun generatePreview(project: Project, previewDescriptor: ProblemDescriptor): IntentionPreviewInfo =
            IntentionPreviewInfo.Html("Restarts this failed test")

        override fun availableInBatchMode(): Boolean = false

        override fun getIcon(flags: Int): Icon = executor.icon
    }
}
