package org.vlang.projectWizard

import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleBuilderListener
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.startup.StartupManager
import com.intellij.openapi.vfs.VirtualFile
import org.vlang.configurations.VlangProjectSettingsForm
import org.vlang.ide.ui.VIcons
import org.vlang.toolchain.VlangToolchain
import org.vlang.toolchain.VlangToolchainService.Companion.toolchainSettings
import java.io.IOException

class VlangModuleBuilder : ModuleBuilder(), ModuleBuilderListener {
    private val model = VlangProjectSettingsForm.Model(
        toolchainLocation = "",
    )

    override fun getPresentableName() = "V"

    override fun getName() = "V"

    override fun getDescription() = "Simple V project"

    override fun getNodeIcon() = VIcons.V

    override fun getWeight() = 2100

    override fun getModuleType(): ModuleType<*> = ModuleType.EMPTY

    override fun getCustomOptionsStep(context: WizardContext, parentDisposable: Disposable): ModuleWizardStep {
        return VlangConfigurationWizardStep(context, model)
    }

    override fun setupRootModel(modifiableRootModel: ModifiableRootModel) {
        val contentEntry = doAddContentEntry(modifiableRootModel)
        val baseDir = contentEntry?.file ?: return

        setupProject(modifiableRootModel.module, baseDir, model.toolchainLocation)
    }

    override fun moduleCreated(module: Module) {}

    companion object {
        fun setupProject(
            module: Module,
            baseDir: VirtualFile,
            toolchainLocation: String,
        ) {
            invokeLater {
                runWriteAction {
                    try {
                        val filesToOpen = VlangProjectTemplate()
                            .generateProject(module, baseDir)

                        if (!filesToOpen.isEmpty()) {
                            scheduleFilesOpening(module, filesToOpen)
                        }

                        setToolchainInfo(module, toolchainLocation)
                    } catch (ignore: IOException) {
                    }
                }
            }
        }

        private fun setToolchainInfo(module: Module, toolchainLocation: String) {
            val project = module.project
            val settings = project.toolchainSettings
            settings.setToolchain(project, VlangToolchain.fromPath(toolchainLocation))
        }

        private fun scheduleFilesOpening(module: Module, files: Collection<VirtualFile>) {
            runWhenNonModalIfModuleNotDisposed(module) {
                val manager = FileEditorManager.getInstance(module.project)
                files.forEach { file ->
                    manager.openFile(file, true)
                }
            }
        }

        private fun runWhenNonModalIfModuleNotDisposed(module: Module, runnable: Runnable) {
            // runnable must not be executed immediately because the new project model might be not yet committed, so V Toolchain won't be found
            // In WebStorm we get already initialized project at this point, but in IntelliJ IDEA - not yet initialized.
            if (module.project.isInitialized) {
                ApplicationManager.getApplication().invokeLater(runnable, ModalityState.NON_MODAL, module.disposed)
                return
            }

            StartupManager.getInstance(module.project).runAfterOpened {
                if (ApplicationManager.getApplication().currentModalityState === ModalityState.NON_MODAL) {
                    runnable.run()
                } else {
                    ApplicationManager.getApplication()
                        .invokeLater(runnable, ModalityState.NON_MODAL) {
                            module.isDisposed
                        }
                }
            }
        }
    }
}
