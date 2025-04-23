package io.vlang.ide.run

import com.intellij.execution.configuration.EnvironmentVariablesTextFieldWithBrowseButton
import com.intellij.ide.macro.MacrosDialog
import com.intellij.openapi.components.PathMacroManager
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.fields.ExtendableTextField
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.layout.ComboBoxPredicate
import com.intellij.ui.layout.ComponentPredicate
import java.io.File
import javax.swing.JComponent
import javax.swing.JPanel

open class VlangRunConfigurationEditor(private val project: Project) : SettingsEditor<VlangRunConfiguration>() {
    enum class RunKind {
        File,
        Directory;

        companion object {
            fun fromString(str: String?): RunKind {
                return when (str) {
                    "File"      -> File
                    "Directory" -> Directory
                    null        -> Directory
                    else        -> throw IllegalArgumentException("Unknown run kind: $str")
                }
            }
        }
    }

    val pathMacroManager = PathMacroManager.getInstance(project)

    private data class Model(
        var runKind: RunKind? = RunKind.Directory,
        var fileName: String = "",
        var directoryName: String = "",
        var outputFileName: String = "",
        var runAfterBuild: Boolean = true,
        var workingDir: String = "",
        var envs: String = "",
        var isPassParentEnvs: Boolean = true,
        var buildArguments: String = "",
        var programArguments: String = "",
        var production: Boolean = false,
        var emulateTerminal: Boolean = false,
    )

    fun getExpandedWorkingDir(): File = File(pathMacroManager.expandPath(model.workingDir))

    private lateinit var mainPanel: DialogPanel
    private val model = Model()

    override fun resetEditorFrom(demoRunConfiguration: VlangRunConfiguration) {
        with(model) {
            runKind = demoRunConfiguration.options.runKind
            fileName = demoRunConfiguration.options.fileName
            directoryName = demoRunConfiguration.options.directory
            outputFileName = demoRunConfiguration.options.outputFileName
            runAfterBuild = demoRunConfiguration.options.runAfterBuild
            workingDir = demoRunConfiguration.options.workingDir
            envs = demoRunConfiguration.options.envs
            isPassParentEnvs = demoRunConfiguration.options.isPassParentEnvs
            buildArguments = demoRunConfiguration.options.buildArguments
            programArguments = demoRunConfiguration.options.programArguments
            production = demoRunConfiguration.options.production
            emulateTerminal = demoRunConfiguration.options.emulateTerminal
        }

        mainPanel.reset()
    }

    override fun applyEditorTo(demoRunConfiguration: VlangRunConfiguration) {
        mainPanel.apply()

        with(demoRunConfiguration.options) {
            runKind = model.runKind ?: RunKind.Directory
            fileName = model.fileName
            directory = model.directoryName
            outputFileName = model.outputFileName
            runAfterBuild = model.runAfterBuild
            workingDir = model.workingDir
            envs = model.envs
            isPassParentEnvs = model.isPassParentEnvs
            buildArguments = model.buildArguments
            programArguments = model.programArguments
            production = model.production
            emulateTerminal = model.emulateTerminal
        }
    }

    override fun createEditor(): JComponent = component()

    private fun component(): JPanel {
        lateinit var kindComboBox: Cell<ComboBox<RunKind>>

        @Suppress("UnstableApiUsage")
        mainPanel = panel {
            row("Run kind:") {
                kindComboBox = comboBox(listOf(RunKind.File, RunKind.Directory))
                    .bindItem(model::runKind)
            }

            row("Source file:") {
                textFieldWithBrowseButton(
                    FileChooserDescriptorFactory.singleFile()
                        .withTitle("Select V Source File")
                        .withExtensionFilter("V files", "v", "vv", "vsh"),
                    project
                ) { file -> File(file.path).relativeTo(getExpandedWorkingDir()).normalize().toString() }.apply {
                    MacrosDialog.addTextFieldExtension(
                        component.textField as ExtendableTextField,
                        MacrosDialog.Filters.DIRECTORY_PATH,
                        null
                    )
                }
                    .align(AlignX.FILL)
                    .bindText(model::fileName)
                    .validationOnInput {
                        val text = it.text.trim()
                        if (text.isEmpty()) {
                            return@validationOnInput error("Source file cannot be empty")
                        }
                        if (!text.endsWith(".v") && !text.endsWith(".vv") && !text.endsWith(".vsh")) {
                            return@validationOnInput error("Source file must be a V file (\".v\", \".vv\", \".vsh\")")
                        }
                        null
                    }
            }
                .visibleIf(kindComboBox.selectedValueMatches { it == RunKind.File })

            row("Source directory:") {
                textFieldWithBrowseButton(
                    FileChooserDescriptorFactory.createSingleFolderDescriptor().withTitle("Select V Source Directory"),
                    project
                ) { file ->
                    if (file.path == model.workingDir) {
                        "."
                    } else {
                        File(file.path).relativeTo(getExpandedWorkingDir()).normalize().toString()
                    }
                }.apply {
                    MacrosDialog.addTextFieldExtension(
                        component.textField as ExtendableTextField,
                        MacrosDialog.Filters.DIRECTORY_PATH,
                        null
                    )
                }
                    .align(AlignX.FILL)
                    .bindText(model::directoryName)
                    .validationOnInput {
                        val text = it.text.trim()
                        if (text.isEmpty()) {
                            return@validationOnInput error("Source directory cannot be empty")
                        }
                        null
                    }
            }
                .visibleIf(kindComboBox.selectedValueMatches { it == RunKind.Directory })

            row("Output file:") {
                textFieldWithBrowseButton(
                    FileChooserDescriptorFactory.singleFile().withTitle("Select File"),
                    project
                ) { file -> File(file.path).relativeTo(getExpandedWorkingDir()).normalize().toString() }.apply {
                    MacrosDialog.addTextFieldExtension(
                        component.textField as ExtendableTextField,
                        MacrosDialog.Filters.DIRECTORY_PATH,
                        null
                    )
                }
                    .align(AlignX.FILL)
                    .bindText(model::outputFileName)
                    .comment("If not empty will override the default output file with <code>-o</code> flag")
            }

            row(" ") {
                checkBox("Run after build")
                    .bindSelected(model::runAfterBuild)
            }

            row("Working directory:") {
                textFieldWithBrowseButton(
                    FileChooserDescriptorFactory.createSingleFolderDescriptor().withTitle("Select V Directory"),
                    project
                )
                    .apply {
                        MacrosDialog.addTextFieldExtension(
                            component.textField as ExtendableTextField,
                            MacrosDialog.Filters.DIRECTORY_PATH,
                            null
                        )
                    }
                    .align(AlignX.FILL)
                    .bindText(model::workingDir)
            }

            row("Environment:") {
                cell(EnvironmentVariablesTextFieldWithBrowseButton())
                    .apply {
                        MacrosDialog.addTextFieldExtension(
                            component.textField,
                            MacrosDialog.Filters.ALL,
                            null
                        )
                        component.isPassParentEnvs = model.isPassParentEnvs
                        component.addChangeListener { model.isPassParentEnvs = component.isPassParentEnvs }
                    }
                    .align(AlignX.FILL)
                    .bindText(model::envs)
            }

            row(" ") {
                checkBox("Production build")
                    .bindSelected(model::production)
                    .comment("Builds an optimized version with the <code>-prod</code> flag, increasing compilation time")
            }

            row(" ") {
                checkBox("Emulate terminal in output console")
                    .bindSelected(model::emulateTerminal)
            }

            row("Build arguments:") {
                expandableTextField().apply {
                    MacrosDialog.addTextFieldExtension(component, MacrosDialog.Filters.DIRECTORY_PATH, null)
                }
                    .align(AlignX.FILL)
                    .bindText(model::buildArguments)
                    .comment("Arguments to pass to <b>v build</b> command")
            }

            row("Program arguments:") {
                expandableTextField().apply {
                    MacrosDialog.addTextFieldExtension(component, MacrosDialog.Filters.DIRECTORY_PATH, null)
                }
                    .align(AlignX.FILL)
                    .bindText(model::programArguments)
                    .comment("Arguments to pass when run executable")
            }
        }

        mainPanel.registerValidators(this)

        return mainPanel
    }

    companion object {
        private fun <T> ComboBox<T>.selectedValueMatches(predicate: (T?) -> Boolean): ComponentPredicate {
            return ComboBoxPredicate(this, predicate)
        }

        fun <T> Cell<ComboBox<T>>.selectedValueMatches(predicate: (T?) -> Boolean): ComponentPredicate {
            return component.selectedValueMatches(predicate)
        }
    }
}
