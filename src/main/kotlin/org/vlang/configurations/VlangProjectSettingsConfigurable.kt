package org.vlang.configurations

import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.util.Disposer
import com.intellij.psi.stubs.StubIndexImpl
import org.vlang.configurations.VlangProjectSettingsState.Companion.projectSettings

class VlangProjectSettingsConfigurable(private val project: Project) : Configurable {
    private val mainPanel: DialogPanel
    private val model = VlangProjectSettingsForm.Model(
        toolchainLocation = "",
        toolchainVersion = "",
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

        mainPanel.validateAll()

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

    override fun reset() {
        val settings = project.projectSettings

        with(model) {
            toolchainLocation = settings.toolchainLocation
            toolchainVersion = settings.toolchainVersion
            stdlibLocation = settings.stdlibLocation
            modulesLocation = settings.modulesLocation
        }

        mainPanel.reset()
    }

    companion object {
        private val LOG = logger<VlangProjectSettingsConfigurable>()

        fun onToolchainApply(model: VlangProjectSettingsForm.Model) {
            model.toolchainVersion = VlangConfigurationUtil.guessToolchainVersion(model.toolchainLocation)

            if (model.stdlibLocation.isEmpty() && model.toolchainVersion != VlangConfigurationUtil.UNDEFINED_VERSION) {
                model.stdlibLocation = VlangConfigurationUtil.getStdlibLocation(model.toolchainLocation) ?: ""
            }
        }

        fun show(project: Project) {
            ShowSettingsUtil.getInstance().showSettingsDialog(project, VlangProjectSettingsConfigurable::class.java)
        }
    }
}
