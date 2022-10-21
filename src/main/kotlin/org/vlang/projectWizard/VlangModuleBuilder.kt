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
import org.vlang.configurations.VlangProjectSettingsState.Companion.projectSettings
import org.vlang.ide.ui.VIcons
import java.io.IOException

class VlangModuleBuilder : ModuleBuilder(), ModuleBuilderListener {
    private val model = VlangProjectSettingsForm.Model(
        toolchainLocation = "",
        toolchainVersion = "",
        stdlibLocation = "",
        modulesLocation = "",
    )

    override fun getPresentableName() = "V"

    override fun getName() = "V"

    override fun getDescription() = "Simple V project"

    override fun getNodeIcon() = VIcons.Vlang

    override fun getWeight() = 2100

    override fun getModuleType(): ModuleType<*> = ModuleType.EMPTY

    override fun getCustomOptionsStep(context: WizardContext, parentDisposable: Disposable): ModuleWizardStep {
        return VlangConfigurationWizardStep(context, model)
    }

    override fun setupRootModel(modifiableRootModel: ModifiableRootModel) {
        val contentEntry = doAddContentEntry(modifiableRootModel)
        val baseDir = contentEntry?.file ?: return

        setupProject(modifiableRootModel, baseDir)
    }

    override fun moduleCreated(module: Module) {}

    private fun setupProject(
        modifiableRootModel: ModifiableRootModel,
        baseDir: VirtualFile,
    ) {
        invokeLater {
            runWriteAction {
                try {
                    val filesToOpen = VlangProjectTemplate()
                        .generateProject("", modifiableRootModel.module, baseDir)

                    if (!filesToOpen.isEmpty()) {
                        scheduleFilesOpening(modifiableRootModel.module, filesToOpen)
                    }

                    setToolchainInfo(modifiableRootModel)
                } catch (ignore: IOException) {
                }
            }
        }
    }

    private fun setToolchainInfo(modifiableRootModel: ModifiableRootModel) {
        val project = modifiableRootModel.module.project
        val settings = project.projectSettings
        with(settings) {
            toolchainLocation = model.toolchainLocation
            toolchainVersion = model.toolchainVersion
            stdlibLocation = model.stdlibLocation
            modulesLocation = model.modulesLocation
        }
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
        // runnable must not be executed immediately because the new project model might be not yet committed, so Dart SDK won't be found
        // In WebStorm we get already initialized project at this point, but in IntelliJ IDEA - not yet initialized.
        if (module.project.isInitialized) {
            ApplicationManager.getApplication().invokeLater(runnable, ModalityState.NON_MODAL, module.disposed)
            return
        }

        StartupManager.getInstance(module.project).runWhenProjectIsInitialized {
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
