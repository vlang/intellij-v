package org.vlang.configurations

import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.util.Disposer
import com.intellij.psi.stubs.StubIndexImpl
import org.vlang.configurations.VlangProjectSettingsState.Companion.projectSettings
import org.vlang.projectWizard.VlangToolchainFlavor
import kotlin.io.path.pathString

class VlangProjectSettingsConfigurable(private val project: Project) : Configurable {
    private val mainPanel: DialogPanel
    private val model = VlangProjectSettingsForm.Model(
        toolchainLocation = "",
        toolchainVersion = "N/A",
        stdlibLocation = "",
        modulesLocation = "",
    )

    init {
        mainPanel = VlangProjectSettingsForm(project, model, false) { onToolchainApply(model) }.createComponent()

        val disposable = Disposer.newDisposable()
        mainPanel.registerValidators(disposable)
    }

    override fun getDisplayName() = "V"
    override fun getPreferredFocusedComponent() = mainPanel
    override fun createComponent() = mainPanel

    override fun isModified(): Boolean {
        mainPanel.apply()

        val settings = project.projectSettings
        return model.toolchainLocation != settings.toolchainLocation ||
                model.toolchainVersion != settings.toolchainVersion ||
                model.stdlibLocation != settings.stdlibLocation ||
                model.modulesLocation != settings.modulesLocation
    }

    override fun apply() {
        mainPanel.apply()

        validateSettings()

        if (model.stdlibLocation.isNotEmpty() && isModified) {
            val stdlibRoot = VlangConfiguration.getInstance(project).stdlibLocation
            if (stdlibRoot != null) {
                StubIndexImpl.getInstance().forceRebuild(object : Exception() {})
            }
        }

        val settings = project.projectSettings
        with(settings) {
            toolchainLocation = model.toolchainLocation
            toolchainVersion = model.toolchainVersion
            stdlibLocation = model.stdlibLocation
            modulesLocation = model.modulesLocation
        }
    }

    private fun validateSettings() {
        val issues = mainPanel.validateAll()
        if (issues.isNotEmpty()) {
            throw ConfigurationException(issues.first().message)
        }
    }

    override fun reset() {
        val settings = project.projectSettings

        with(model) {
            if (modulesLocation.isEmpty()) {
                modulesLocation = settings.toolchainLocation
            }
            if (toolchainVersion.isEmpty()) {
                toolchainVersion = settings.toolchainVersion
            }
            if (stdlibLocation.isEmpty()) {
                stdlibLocation = settings.stdlibLocation
            }
            if (modulesLocation.isEmpty()) {
                modulesLocation = settings.modulesLocation
            }
        }

        mainPanel.reset()
    }

    companion object {
        private val LOG = logger<VlangProjectSettingsConfigurable>()

        fun onToolchainApply(model: VlangProjectSettingsForm.Model) {
            if (model.modulesLocation.isEmpty()) {
                val modulesLocation = VlangToolchainFlavor.suggestModulesHomePath()
                if (modulesLocation != null) {
                    model.modulesLocation = modulesLocation.pathString
                }
            }

            model.toolchainVersion = VlangConfigurationUtil.guessToolchainVersion(model.toolchainLocation)

            if (model.toolchainVersion != VlangConfigurationUtil.UNDEFINED_VERSION) {
                model.stdlibLocation = VlangConfigurationUtil.getStdlibLocation(model.toolchainLocation) ?: ""
            } else {
                model.stdlibLocation = ""
            }
        }

        fun show(project: Project) {
            ShowSettingsUtil.getInstance().showSettingsDialog(project, VlangProjectSettingsConfigurable::class.java)
        }
    }
}
