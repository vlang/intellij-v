package org.vlang.configurations

import com.intellij.execution.configuration.EnvironmentVariablesComponent
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.*
import org.vlang.configurations.VlangFmtSettingsState.Companion.vfmtSettings

class VlangFmtSettingsConfigurable(private val project: Project) : Configurable {
    data class Model(
        var additionalArguments: String,
        var envs: Map<String, String>,
        var runVfmtOnSave: Boolean,
    )

    private val mainPanel: DialogPanel
    private val model = Model(
        additionalArguments = "",
        envs = emptyMap(),
        runVfmtOnSave = false,
    )

    private val environmentVariables = EnvironmentVariablesComponent()

    init {
        mainPanel = panel {
            row("Additional arguments:") {
                expandableTextField()
                    .align(AlignX.FILL)
                    .bindText(model::additionalArguments)
                    .comment("Additional arguments to pass to <b>v fmt</b> command")
            }
            row(environmentVariables.label) {
                cell(environmentVariables)
                    .align(AlignX.FILL)
                    .bind(
                        componentGet = { it.envs },
                        componentSet = { component, value -> component.envs = value },
                        prop = model::envs.toMutableProperty()
                    )
            }
            row {
                checkBox("Run vfmt on Save")
                    .bindSelected(model::runVfmtOnSave)
            }
        }
    }

    override fun getDisplayName() = "Vfmt"
    override fun getPreferredFocusedComponent() = mainPanel
    override fun createComponent() = mainPanel

    override fun isModified(): Boolean {
        mainPanel.apply()

        val settings = project.vfmtSettings
        return model.additionalArguments != settings.additionalArguments ||
                model.envs != settings.envs ||
                model.runVfmtOnSave != settings.runVfmtOnSave
    }

    override fun apply() {
        mainPanel.apply()

        val settings = project.vfmtSettings
        with(settings) {
            additionalArguments = model.additionalArguments
            envs = model.envs
            runVfmtOnSave = model.runVfmtOnSave
        }
    }

    override fun reset() {
        val settings = project.vfmtSettings

        with(model) {
            additionalArguments = settings.additionalArguments
            envs = settings.envs
            runVfmtOnSave = settings.runVfmtOnSave
        }

        mainPanel.reset()
    }

    companion object {
        private val LOG = logger<VlangFmtSettingsConfigurable>()
    }
}
