package org.vlang.projectWizard

import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.ui.JBUI
import org.vlang.configurations.VlangProjectSettingsForm
import org.vlang.ide.VlangPostStartupActivity
import org.vlang.toolchain.VlangKnownToolchainsState
import javax.naming.ConfigurationException
import javax.swing.JComponent

class VlangConfigurationWizardStep(context: WizardContext, model: VlangProjectSettingsForm.Model) : ModuleWizardStep() {
    init {
        val knownToolchains = VlangKnownToolchainsState.getInstance().knownToolchains
        val needFindToolchains = knownToolchains.isEmpty()
        if (needFindToolchains) {
            VlangPostStartupActivity.setupToolchainCandidates()
        }
    }

    private val toolchainSettingsForm = VlangProjectSettingsForm(context.project, model).createComponent()
    private val newProjectPanel = panel {
       row("Toolchain:") {
           cell(toolchainSettingsForm)
               .align(AlignX.FILL)
       }
    }.apply {
        border = JBUI.Borders.empty(14, 20)
    }

    override fun getComponent(): JComponent = newProjectPanel

    override fun updateDataModel() {}

    override fun updateStep() {}

    override fun validate(): Boolean {
        validateSettings()
        return true
    }

    private fun validateSettings() {
        val issues = newProjectPanel.validateAll()
        if (issues.isNotEmpty()) {
            throw ConfigurationException(issues.first().message)
        }
    }
}
