package org.vlang.configurations

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.util.Disposer
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.toMutableProperty
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import java.io.File
import javax.swing.JLabel
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class VlangProjectSettingsForm(
    private val project: Project?,
    private val model: Model,
    private val newWizard: Boolean,
    private val onToolchainApply: () -> Unit,
) {
    data class Model(
        var toolchainLocation: String,
        var toolchainVersion: String,
        var stdlibLocation: String,
        var modulesLocation: String,
    )

    private val mainPanel: DialogPanel

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
                    .onApply {
                        onToolchainApplyProxy()
                    }
                    .applyToComponent {
                        if (newWizard) {
                            textField.document.addDocumentListener(object : DocumentListener {
                                override fun changedUpdate(e: DocumentEvent?) {
                                    onToolchainApplyProxy()
                                }

                                override fun removeUpdate(e: DocumentEvent?) {
                                    onToolchainApplyProxy()
                                }

                                override fun insertUpdate(e: DocumentEvent?) {
                                    onToolchainApplyProxy()
                                }
                            })
                        }
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
            row("Modules location:") {
                textFieldWithBrowseButton(
                    "Select Modules Folder",
                    project,
                    FileChooserDescriptorFactory.createSingleFolderDescriptor()
                )
                    .horizontalAlign(HorizontalAlign.FILL)
                    .bindText(model::modulesLocation)
            }
        }

        val disposable = Disposer.newDisposable()
        mainPanel.registerValidators(disposable)
    }

    private fun onToolchainApplyProxy() {
        if (newWizard) {
            mainPanel.apply()
        }
        onToolchainApply()
        mainPanel.reset()
    }

    fun createComponent() = mainPanel
}
