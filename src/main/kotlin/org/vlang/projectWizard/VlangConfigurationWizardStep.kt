package org.vlang.projectWizard

import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.util.ui.JBUI
import org.vlang.configurations.VlangProjectSettingsConfigurable
import org.vlang.configurations.VlangProjectSettingsForm
import javax.swing.JComponent

class VlangConfigurationWizardStep(private val context: WizardContext, model: VlangProjectSettingsForm.Model) : ModuleWizardStep() {
    private val newProjectPanel = VlangProjectSettingsForm(context.project, model, true) {
        VlangProjectSettingsConfigurable.onToolchainApply(model)
    }.createComponent().apply {
        border = JBUI.Borders.empty(14, 20)
    }

    override fun getComponent(): JComponent = newProjectPanel

    override fun updateDataModel() {}

    override fun updateStep() {}

    override fun validate(): Boolean {
        newProjectPanel.apply()
        return true
    }
}
