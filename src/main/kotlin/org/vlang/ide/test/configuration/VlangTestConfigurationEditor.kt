package org.vlang.ide.test.configuration

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.layout.or
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JRadioButton

open class VlangTestConfigurationEditor(private val project: Project) : SettingsEditor<VlangTestConfiguration>() {
    data class Model(
        var scope: VlangTestScope = VlangTestScope.Directory,
        var directory: String = "",
        var filename: String = "",
        var pattern: String = "",
        var additionalParameters: String = "",
    )

    private lateinit var mainPanel: DialogPanel
    private val model = Model()

    override fun resetEditorFrom(demoRunConfiguration: VlangTestConfiguration) {
        with(model) {
            scope = demoRunConfiguration.scope
            directory = demoRunConfiguration.directory
            filename = demoRunConfiguration.filename
            pattern = demoRunConfiguration.pattern
            additionalParameters = demoRunConfiguration.additionalParameters
        }

        mainPanel.reset()
    }

    override fun applyEditorTo(demoRunConfiguration: VlangTestConfiguration) {
        mainPanel.apply()

        with(demoRunConfiguration) {
            scope = model.scope
            directory = model.directory
            filename = model.filename
            pattern = model.pattern
            additionalParameters = model.additionalParameters
        }
    }

    override fun createEditor(): JComponent = component()

    private fun component(): JPanel {
        lateinit var directoryRadioButton: Cell<JRadioButton>
        lateinit var fileRadioButton: Cell<JRadioButton>
        lateinit var functionRadioButton: Cell<JRadioButton>

        mainPanel = panel {
            group("Test Runner") {
                buttonsGroup {
                    row("Scope:") {
                        directoryRadioButton = radioButton("Directory", VlangTestScope.Directory)
                            .align(AlignX.LEFT)
                            .apply {
                                component.isSelected = true
                            }

                        fileRadioButton = radioButton("File", VlangTestScope.File)
                            .align(AlignX.LEFT)
                        functionRadioButton = radioButton("Function", VlangTestScope.Function)
                            .align(AlignX.LEFT)
                    }.bottomGap(BottomGap.SMALL)
                }.bind(model::scope)

                row("Directory:") {
                    textFieldWithBrowseButton(
                        "Select V Tests Folder",
                        project,
                        FileChooserDescriptorFactory.createSingleFolderDescriptor()
                    )
                        .align(AlignX.FILL)
                        .bindText(model::directory)
                }.visibleIf(directoryRadioButton.selected)
                    .bottomGap(BottomGap.NONE)

                row("Test file:") {
                    textFieldWithBrowseButton(
                        "Select V Test File",
                        project,
                        FileChooserDescriptorFactory.createSingleFileDescriptor()
                    )
                        .align(AlignX.FILL)
                        .bindText(model::filename)
                }.visibleIf(fileRadioButton.selected.or(functionRadioButton.selected))
                    .bottomGap(BottomGap.NONE)

                row("Pattern:") {
                    expandableTextField()
                        .align(AlignX.FILL)
                        .bindText(model::pattern)
                        .comment("""
                            Glob pattern to filter tests.<br>
                            You can separate multiple glob patterns with `,`.<br> 
                            Glob patterns support `*` which matches anything, and `?`, that matches any single character.<br>
                            They are *NOT* regular expressions however.
                        """.trimIndent())
                }.visibleIf(functionRadioButton.selected)
                    .bottomGap(BottomGap.NONE)
            }

            group("Command Line") {
                row("Additional parameters:") {
                    textField()
                        .align(AlignX.FILL)
                        .bindText(model::additionalParameters)
                }.bottomGap(BottomGap.NONE)
            }.topGap(TopGap.NONE)
        }

        return mainPanel
    }
}
