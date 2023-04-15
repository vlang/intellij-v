package org.vlang.ide

import com.intellij.CommonBundle
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.Ref
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.changes.CommitContext
import com.intellij.openapi.vcs.changes.CommitExecutor
import com.intellij.openapi.vcs.checkin.CheckinHandler
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory
import com.intellij.openapi.vcs.ui.RefreshableOnComponent
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.util.PairConsumer
import com.intellij.util.ui.UIUtil
import org.vlang.toolchain.Vfmt
import java.awt.BorderLayout
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JPanel

class VlangFmtCheckinFactory : CheckinHandlerFactory() {
    override fun createHandler(panel: CheckinProjectPanel, commitContext: CommitContext): CheckinHandler {
        return object : CheckinHandler() {
            override fun getBeforeCheckinConfigurationPanel(): RefreshableOnComponent {
                val checkBox = JCheckBox("V fmt")

                return object : RefreshableOnComponent {
                    override fun saveState() {
                        PropertiesComponent.getInstance(panel.project)
                            .setValue(V_FMT, java.lang.Boolean.toString(checkBox.isSelected))
                    }

                    override fun restoreState() {
                        checkBox.isSelected = enabled(panel)
                    }

                    override fun getComponent(): JComponent {
                        val pane = JPanel(BorderLayout())
                        pane.add(checkBox, BorderLayout.WEST)
                        return pane
                    }
                }
            }

            override fun beforeCheckin(
                executor: CommitExecutor?,
                additionalDataConsumer: PairConsumer<Any, Any>,
            ): ReturnResult? {
                if (enabled(panel)) {
                    val success = Ref.create(true)
                    FileDocumentManager.getInstance().saveAllDocuments()
                    for (file in psiFiles) {
                        val virtualFile = file!!.virtualFile
                        val document = FileDocumentManager.getInstance().getDocument(virtualFile) ?: continue
                        Vfmt.reformatDocument(file.project, document) {
                            success.set(false)
                        }
                    }
                    FileDocumentManager.getInstance().saveAllDocuments()
                    if (!success.get()) {
                        return showErrorMessage(executor)
                    }
                }
                return super.beforeCheckin()
            }

            private fun showErrorMessage(executor: CommitExecutor?): ReturnResult {
                val buttons = arrayOf("&Details...", commitButtonMessage(executor, panel), CommonBundle.getCancelButtonText())
                val answer = Messages.showDialog(
                    panel.project,
                    """
                     <html><body>V fmt returned non-zero code on some of the files.<br/>Would you like to commit anyway?</body></html>
                     
                     """.trimIndent(),
                    "V Fmt Failed", null, buttons, 0, 1, UIUtil.getWarningIcon()
                )
                if (answer == Messages.OK) return ReturnResult.CLOSE_WINDOW
                return if (answer == Messages.NO) ReturnResult.COMMIT else ReturnResult.CANCEL
            }

            private val psiFiles: List<PsiFile?>
                get() {
                    val manager = PsiManager.getInstance(panel.project)
                    return panel.virtualFiles.mapNotNull {
                        manager.findFile(it)
                    }
                }
        }
    }

    companion object {
        private const val V_FMT = "V_FMT"

        private fun commitButtonMessage(executor: CommitExecutor?, panel: CheckinProjectPanel): String {
            val text = executor?.actionText ?: panel.commitActionName
            return text.removeSuffix("...")
        }

        private fun enabled(panel: CheckinProjectPanel): Boolean = PropertiesComponent.getInstance(panel.project).getBoolean(V_FMT, true)
    }
}
