package io.vlang.projectWizard

import com.intellij.ide.impl.MultipleFileOpener.Companion.openFiles
import com.intellij.ide.util.PsiNavigationSupport
import com.intellij.ide.util.RunOnceUtil
import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleBuilderListener
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.invokeAndWaitIfNeeded
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.startup.StartupManager
import com.intellij.openapi.vfs.VirtualFile
import io.vlang.configurations.VlangProjectSettingsForm
import io.vlang.ide.ui.VIcons
import io.vlang.toolchain.VlangToolchain
import io.vlang.toolchain.VlangToolchainService.Companion.toolchainSettings
import kotlinx.html.InputType
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
            invokeLater(ModalityState.nonModal()) {
                runWriteAction {
                    try {
                        val filesToOpen = VlangProjectTemplate()
                            .generateProject(module, baseDir)

                        if (filesToOpen.isNotEmpty()) {
                            scheduleFilesOpening(module.project, filesToOpen)
                        }

                        setToolchainInfo(module, toolchainLocation)
                    } catch (_: IOException) {
                    }
                }
            }
        }

        private fun setToolchainInfo(module: Module, toolchainLocation: String) {
            val project = module.project
            val settings = project.toolchainSettings
            settings.setToolchain(project, VlangToolchain.fromPath(toolchainLocation))
        }

        private fun scheduleFilesOpening(project: Project, files: Collection<VirtualFile>) = invokeLater {
            if (!ApplicationManager.getApplication().isHeadlessEnvironment) {
                val navigation = PsiNavigationSupport.getInstance()
                files.forEachIndexed { index, file ->
                    // open all files, but focus only the last one
                    navigation.createNavigatable(project, file, -1).navigate(index == files.size - 1)
                }
            }
        }
    }
}
