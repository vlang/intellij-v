package org.vlang.projectWizard

import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleBuilderListener
import com.intellij.ide.util.projectWizard.WizardInputField
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.module.BasePackageParameterFactory
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.startup.StartupManager
import com.intellij.openapi.vfs.VirtualFile
import org.vlang.ide.ui.VIcons
import java.io.IOException

class VlangModuleBuilder : ModuleBuilder(), ModuleBuilderListener {
    override fun getPresentableName() = "V Project"

    override fun getName() = "V Project"

    override fun getDescription() = "Simple V project"

    override fun getNodeIcon() = VIcons.Vlang

    override fun getWeight() = 2100

    override fun getModuleType(): ModuleType<*> = ModuleType.EMPTY

    override fun getAdditionalFields(): MutableList<WizardInputField<*>> {
        val field = BasePackageParameterFactory().createField("other") ?: return mutableListOf()
        return mutableListOf(field)
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
                } catch (ignore: IOException) {
                }
            }
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
