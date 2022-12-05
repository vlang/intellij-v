package org.vlang.configurations

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import org.vlang.debugger.lang.VlangDebuggerState

class VlangDebuggerSettingsConfigurable : Configurable {
    data class Model(
        var lldbPath: String,
        var downloadAutomatically: Boolean,
        var stopsAtPanics: Boolean,
    )

    private val mainPanel: DialogPanel
    private val model = Model(
        lldbPath = "",
        downloadAutomatically = true,
        stopsAtPanics = true,
    )

    init {
        mainPanel = panel {
            row("LLDB path:") {
                textFieldWithBrowseButton(
                    "Select Directory with LLDB",
                    null,
                    FileChooserDescriptorFactory.createSingleFolderDescriptor()
                )
                    .horizontalAlign(HorizontalAlign.FILL)
                    .bindText(model::lldbPath)
                    .comment("Path to the LLDB debugger executable")
            }
            row {
                checkBox("Stop execution when panics")
                    .bindSelected(model::stopsAtPanics)
                    .comment("When panic occurs, the debugger will stop execution for you to inspect the stack trace")
            }
            row {
                checkBox("Download and update debugger automatically")
                    .bindSelected(model::downloadAutomatically)
            }
        }
    }

    override fun getDisplayName() = "Vfmt"
    override fun getPreferredFocusedComponent() = mainPanel
    override fun createComponent() = mainPanel

    override fun isModified(): Boolean {
        mainPanel.apply()

        val settings = VlangDebuggerState.getInstance()
        return model.lldbPath != settings.lldbPath ||
                model.downloadAutomatically != settings.downloadAutomatically ||
                model.stopsAtPanics != settings.stopsAtPanics
    }

    override fun apply() {
        mainPanel.apply()

        val settings =VlangDebuggerState.getInstance()
        with(settings) {
            lldbPath = model.lldbPath
            downloadAutomatically = model.downloadAutomatically
            stopsAtPanics = model.stopsAtPanics
        }
    }

    override fun reset() {
        val settings = VlangDebuggerState.getInstance()

        with(model) {
            lldbPath = settings.lldbPath ?: ""
            downloadAutomatically = settings.downloadAutomatically
            stopsAtPanics = settings.stopsAtPanics
        }

        mainPanel.reset()
    }
}
