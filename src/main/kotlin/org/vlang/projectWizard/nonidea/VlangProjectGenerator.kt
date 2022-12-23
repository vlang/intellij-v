package org.vlang.projectWizard.nonidea

import com.intellij.facet.ui.ValidationResult
import com.intellij.ide.util.projectWizard.AbstractNewProjectStep
import com.intellij.ide.util.projectWizard.CustomStepProjectGenerator
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.impl.welcomeScreen.AbstractActionWithPanel
import com.intellij.platform.DirectoryProjectGenerator
import com.intellij.platform.ProjectGeneratorPeer
import org.vlang.configurations.VlangProjectSettingsForm
import org.vlang.ide.ui.VIcons
import org.vlang.projectWizard.VlangModuleBuilder
import javax.swing.Icon

// We need this class since the default moduleBuilder in IDEA
// doesn't work in small IDEs like CLion or GoLand.
class VlangProjectGenerator : DirectoryProjectGenerator<Unit>, CustomStepProjectGenerator<Unit> {
    private var peer: VlangProjectGeneratorPeer? = null
    private val model = VlangProjectSettingsForm.Model(
        toolchainLocation = "",
    )

    override fun getName(): String = "V"
    override fun getLogo(): Icon = VIcons.V
    override fun createPeer(): ProjectGeneratorPeer<Unit> =
        VlangProjectGeneratorPeer(model).also { peer = it }

    override fun validate(baseDirPath: String): ValidationResult = ValidationResult.OK

    override fun generateProject(project: Project, baseDir: VirtualFile, data: Unit, module: Module) {
        VlangModuleBuilder.setupProject(module, baseDir, model.toolchainLocation)
    }

    override fun createStep(
        projectGenerator: DirectoryProjectGenerator<Unit>,
        callback: AbstractNewProjectStep.AbstractCallback<Unit>,
    ): AbstractActionWithPanel = VlangProjectSettingsStep(projectGenerator)
}
