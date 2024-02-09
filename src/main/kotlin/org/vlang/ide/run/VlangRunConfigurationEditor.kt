package org.vlang.ide.run

import com.intellij.execution.configuration.EnvironmentVariablesComponent
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.layout.ComboBoxPredicate
import com.intellij.ui.layout.ComponentPredicate
import javax.swing.JComponent
import javax.swing.JPanel

open class VlangRunConfigurationEditor(private val project: Project) : SettingsEditor<VlangRunConfiguration>() {
    enum class RunKind {
        File,
        Directory;

        companion object {
            fun fromString(str: String?): RunKind {
                return when (str) {
                    "File" -> File
                    "Directory" -> Directory
                    null -> File
                    else -> throw IllegalArgumentException("Unknown run kind: $str")
                }
            }
        }
    }

    data class Model(
        var runKind: RunKind? = RunKind.File,
        var fileName: String = "",
        var directoryName: String = "",
        var outputDir: String = "",
        var runAfterBuild: Boolean = true,
        var workingDir: String = "",
        var envs: MutableMap<String, String> = mutableMapOf(),
        var buildArguments: String = "",
        var programArguments: String = "",
        var production: Boolean = false,
        var emulateTerminal: Boolean = false,
    )

    private val environmentVariables = EnvironmentVariablesComponent()
    private lateinit var mainPanel: DialogPanel
    private val model = Model()

    init {
        environmentVariables.label.text = "Environment:"
    }

    override fun resetEditorFrom(demoRunConfiguration: VlangRunConfiguration) {
        with(model) {
            runKind = demoRunConfiguration.runKind
            fileName = demoRunConfiguration.fileName
            directoryName = demoRunConfiguration.directory
            outputDir = demoRunConfiguration.outputDir
            runAfterBuild = demoRunConfiguration.runAfterBuild
            workingDir = demoRunConfiguration.workingDir
            envs = demoRunConfiguration.envs
            buildArguments = demoRunConfiguration.buildArguments
            programArguments = demoRunConfiguration.programArguments
            production = demoRunConfiguration.production
            emulateTerminal = demoRunConfiguration.emulateTerminal
        }

        mainPanel.reset()
    }

    override fun applyEditorTo(demoRunConfiguration: VlangRunConfiguration) {
        mainPanel.apply()

        with(demoRunConfiguration) {
            runKind = model.runKind ?: RunKind.File
            fileName = model.fileName
            directory = model.directoryName
            outputDir = model.outputDir
            runAfterBuild = model.runAfterBuild
            workingDir = model.workingDir
            envs = model.envs
            buildArguments = model.buildArguments
            programArguments = model.programArguments
            production = model.production
            emulateTerminal = model.emulateTerminal
        }
    }

    override fun createEditor(): JComponent = component()

    private fun component(): JPanel {
        lateinit var kindComboBox: Cell<ComboBox<RunKind>>

        mainPanel = panel {
            row("Run kind:") {
                kindComboBox = comboBox(listOf(RunKind.File, RunKind.Directory))
                    .bindItem(model::runKind)
            }.bottomGap(BottomGap.NONE)

            row("File:") {
                textFieldWithBrowseButton(
                    "Select V File",
                    project,
                    FileChooserDescriptorFactory.createSingleFileDescriptor()
                )
                    .align(AlignX.FILL)
                    .bindText(model::fileName)
            }.bottomGap(BottomGap.NONE)
                .visibleIf(kindComboBox.selectedValueMatches { it == RunKind.File })

            row("Directory:") {
                textFieldWithBrowseButton(
                    "Select V Directory",
                    project,
                    FileChooserDescriptorFactory.createSingleFolderDescriptor()
                )
                    .align(AlignX.FILL)
                    .bindText(model::directoryName)
            }.bottomGap(BottomGap.NONE)
                .visibleIf(kindComboBox.selectedValueMatches { it == RunKind.Directory })

            row("Output directory:") {
                textFieldWithBrowseButton(
                    "Select V Directory",
                    project,
                    FileChooserDescriptorFactory.createSingleFolderDescriptor()
                )
                    .align(AlignX.FILL)
                    .bindText(model::outputDir)
            }.bottomGap(BottomGap.NONE)

            row(" ") {
                checkBox("Run after build")
                    .bindSelected(model::runAfterBuild)
            }.bottomGap(BottomGap.NONE)

            row("Working directory:") {
                textFieldWithBrowseButton(
                    "Select V Directory",
                    project,
                    FileChooserDescriptorFactory.createSingleFolderDescriptor()
                )
                    .align(AlignX.FILL)
                    .bindText(model::workingDir)
            }.bottomGap(BottomGap.NONE)

            row(environmentVariables.label) {
                cell(environmentVariables)
                    .align(AlignX.FILL)
                    .bind(
                        componentGet = { it.envs },
                        componentSet = { component, value -> component.envs = value },
                        prop = model::envs.toMutableProperty()
                    )
            }.bottomGap(BottomGap.NONE)

            row(" ") {
                checkBox("Production build")
                    .bindSelected(model::production)
                    .comment("Builds an optimized version with the <code>-prod</code> flag, increasing compilation time")
            }.bottomGap(BottomGap.NONE)

            row(" ") {
                checkBox("Emulate terminal in output console")
                    .bindSelected(model::emulateTerminal)
            }.bottomGap(BottomGap.NONE)

            row("Build arguments:") {
                expandableTextField()
                    .align(AlignX.FILL)
                    .bindText(model::buildArguments)
                    .comment("Arguments to pass to <b>v build</b> command")
            }.bottomGap(BottomGap.NONE)

            row("Program arguments:") {
                expandableTextField()
                    .align(AlignX.FILL)
                    .bindText(model::programArguments)
                    .comment("Arguments to pass when run executable")
            }.bottomGap(BottomGap.NONE)
        }

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
