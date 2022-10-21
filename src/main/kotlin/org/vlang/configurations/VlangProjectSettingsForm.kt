package org.vlang.configurations

import com.intellij.icons.AllIcons
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.util.Disposer
import com.intellij.ui.JBColor
import com.intellij.ui.dsl.builder.RightGap
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.toMutableProperty
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.intellij.ui.layout.ValidationInfoBuilder
import org.vlang.projectWizard.VlangToolchainFlavor
import org.vlang.utils.toPath
import java.io.File
import javax.swing.JLabel
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

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
    private val toolchainVersion = JLabel()
    private val toolchainIconLabel = JLabel()
    private val pathToToolchainComboBox = VlangToolchainPathChoosingComboBox { onToolchainApplyProxy() }

    init {
        mainPanel = panel {
            row("Toolchain location:") {
                cell(pathToToolchainComboBox)
                    .horizontalAlign(HorizontalAlign.FILL)
                    .validationOnApply { validateToolchainPath(it) }
            }
            row("Toolchain version:") {
                cell(toolchainVersion)
                    .bind(JLabel::getText, JLabel::setText, model::toolchainVersion.toMutableProperty())
                    .gap(RightGap.SMALL)
                cell(toolchainIconLabel)
            }
            row("Standard library location:") {
                textFieldWithBrowseButton(
                    "Select V Standard Library Folder",
                    project,
                    FileChooserDescriptorFactory.createSingleFolderDescriptor()
                )
                    .horizontalAlign(HorizontalAlign.FILL)
                    .bindText(model::stdlibLocation)
                    .validationOnApply { validateStdlibPath(it) }
            }
            row("Modules location:") {
                textFieldWithBrowseButton(
                    "Select Modules Folder",
                    project,
                    FileChooserDescriptorFactory.createSingleFolderDescriptor()
                )
                    .horizontalAlign(HorizontalAlign.FILL)
                    .bindText(model::modulesLocation)
                    .validationOnApply { validateVmodulesPath(it) }
            }
        }

        pathToToolchainComboBox.addToolchainsAsync {
            VlangToolchainFlavor.getApplicableFlavors().flatMap { it.suggestHomePaths() }.distinct()
        }

        val disposable = Disposer.newDisposable()
        mainPanel.registerValidators(disposable)
    }

    private fun ValidationInfoBuilder.validateVmodulesPath(it: TextFieldWithBrowseButton): ValidationInfo? {
        if (it.text.isBlank()) {
            return null
        }

        val file = File(model.modulesLocation)
        if (!file.exists() || !file.isDirectory) {
            return error("V modules location is invalid, $file not exist or not a directory")
        }

        return null
    }

    private fun ValidationInfoBuilder.validateStdlibPath(it: TextFieldWithBrowseButton): ValidationInfo? {
        if (it.text.isBlank()) {
            return error("Standard library location cannot be empty")
        }

        val file = File(model.stdlibLocation)
        if (!file.exists() || !file.isDirectory) {
            return error("Standard library location is invalid, $file not exist or not a directory")
        }

        return null
    }

    private fun ValidationInfoBuilder.validateToolchainPath(it: VlangToolchainPathChoosingComboBox): ValidationInfo? {
        if (model.toolchainLocation.isEmpty()) {
            return error("Toolchain location is required")
        }

        val toolchainPath = model.toolchainLocation.toPath()
        if (!toolchainPath.exists()) {
            return error("Toolchain location is invalid, $toolchainPath not exist")
        }

        if (!toolchainPath.isDirectory()) {
            return error("Toolchain location must be a directory")
        }

        val version = VlangConfigurationUtil.guessToolchainVersion(model.toolchainLocation)
        if (version == VlangConfigurationUtil.UNDEFINED_VERSION) {
            return error("Toolchain location is invalid, can't get version. Please check that V compiler contains in ${model.toolchainLocation} folder")
        }

        return null
    }

    private fun onToolchainApplyProxy() {
        model.toolchainLocation = pathToToolchainComboBox.selectedPath ?: ""
        if (newWizard) {
            mainPanel.apply()
        }
        onToolchainApply()
        mainPanel.reset()

        if (model.toolchainVersion == VlangConfigurationUtil.UNDEFINED_VERSION) {
            toolchainVersion.foreground = JBColor.RED
            toolchainIconLabel.icon = null
        } else {
            toolchainVersion.foreground = JBColor.foreground()
            toolchainIconLabel.icon = AllIcons.General.InspectionsOK
        }
    }

    fun validateSettings() {
        val issues = mainPanel.validateAll()
        if (issues.isNotEmpty()) {
            throw ConfigurationException(issues.first().message)
        }
    }

    fun createComponent() = mainPanel
}
