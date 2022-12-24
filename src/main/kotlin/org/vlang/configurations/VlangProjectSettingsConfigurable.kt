package org.vlang.configurations

import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.util.Disposer
import org.vlang.toolchain.VlangToolchain
import org.vlang.toolchain.VlangToolchainService.Companion.toolchainSettings

class VlangProjectSettingsConfigurable(private val project: Project) : Configurable {
    private val mainPanel: DialogPanel
    private val model = VlangProjectSettingsForm.Model(
        toolchainLocation = "",
    )
    private val settingsForm = VlangProjectSettingsForm(project, model)

    init {
        mainPanel = settingsForm.createComponent()

        val disposable = Disposer.newDisposable()
        mainPanel.registerValidators(disposable)
    }

    override fun getDisplayName() = "V"
    override fun getPreferredFocusedComponent() = mainPanel
    override fun createComponent() = mainPanel

    override fun isModified(): Boolean {
        mainPanel.apply()

        val settings = project.toolchainSettings
        return model.toolchainLocation != settings.toolchainLocation
    }

    override fun apply() {
        mainPanel.apply()

        validateSettings()

        val settings = project.toolchainSettings
        settings.setToolchain(project, VlangToolchain.fromPath(model.toolchainLocation))
    }

    private fun validateSettings() {
        val issues = mainPanel.validateAll()
        if (issues.isNotEmpty()) {
            throw ConfigurationException(issues.first().message)
        }
    }

    override fun reset() {
        val settings = project.toolchainSettings

        with(model) {
            toolchainLocation = settings.toolchainLocation
        }

        settingsForm.reset()
        mainPanel.reset()
    }

    companion object {
        private val LOG = logger<VlangProjectSettingsConfigurable>()

        fun show(project: Project) {
            ShowSettingsUtil.getInstance().showSettingsDialog(project, VlangProjectSettingsConfigurable::class.java)
        }
    }
}
