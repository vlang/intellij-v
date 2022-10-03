package org.vlang.configurations

import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.util.Disposer
import com.intellij.psi.stubs.StubIndexImpl
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.toMutableProperty
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import org.vlang.configurations.VlangProjectSettingsState.Companion.projectSettings
import java.io.File
import javax.swing.JLabel

class VlangProjectSettingsConfigurable(private val project: Project) : Configurable {
    data class Model(
        var toolchainLocation: String,
        var toolchainVersion: String,
        var stdlibLocation: String,
    )

    private val mainPanel: DialogPanel
    private val model = Model(
        toolchainLocation = "",
        toolchainVersion = "",
        stdlibLocation = "",
    )

    init {
        mainPanel = panel {
            row("Toolchain location:") {
                textFieldWithBrowseButton(
                    "Select V Toolchain Folder",
                    project,
                    FileChooserDescriptorFactory.createSingleFolderDescriptor()
                )
                    .horizontalAlign(HorizontalAlign.FILL)
                    .bindText(model::toolchainLocation)
                    .onApply {
                        onToolchainApply()
                    }
                    .validationOnApply {
                        if (it.text.isBlank()) {
                            return@validationOnApply error("Toolchain location is required")
                        }

                        val version = VlangConfigurationUtil.guessToolchainVersion(model.toolchainLocation)
                        if (version == VlangConfigurationUtil.UNDEFINED_VERSION) {
                            return@validationOnApply error("Toolchain location is invalid")
                        }

                        null
                    }
            }
            row("Toolchain version:") {
                label(VlangConfigurationUtil.UNDEFINED_VERSION)
                    .bind(JLabel::getText, JLabel::setText, model::toolchainVersion.toMutableProperty())
            }
            row("Standard library location:") {
                textFieldWithBrowseButton(
                    "Select V Standard Library Folder",
                    project,
                    FileChooserDescriptorFactory.createSingleFolderDescriptor()
                )
                    .horizontalAlign(HorizontalAlign.FILL)
                    .bindText(model::stdlibLocation)
                    .validationOnApply {
                        if (it.text.isBlank()) {
                            return@validationOnApply error("Standard library location cannot be empty")
                        }

                        val file = File(model.stdlibLocation)
                        if (!file.exists() || !file.isDirectory) {
                            return@validationOnApply error("Standard library location is invalid")
                        }

                        null
                    }
            }
        }

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
                model.stdlibLocation != settings.stdlibLocation
    }

    private fun onToolchainApply() {
        if (isModified) {
            model.toolchainVersion = VlangConfigurationUtil.guessToolchainVersion(model.toolchainLocation)

            if (model.stdlibLocation.isEmpty() && model.toolchainVersion != VlangConfigurationUtil.UNDEFINED_VERSION) {
                model.stdlibLocation = VlangConfigurationUtil.getStdlibLocation(model.toolchainLocation) ?: ""
            }

            mainPanel.reset()
        }
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
        }
    }

    override fun reset() {
        val settings = project.projectSettings

        with(model) {
            toolchainLocation = settings.toolchainLocation
            toolchainVersion = settings.toolchainVersion
            stdlibLocation = settings.stdlibLocation
        }

        mainPanel.reset()
    }

    companion object {
        private val LOG = logger<VlangProjectSettingsConfigurable>()
    }
}
