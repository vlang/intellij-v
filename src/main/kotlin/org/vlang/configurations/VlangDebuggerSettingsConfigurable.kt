package org.vlang.configurations

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import org.vlang.debugger.lang.VlangDebuggerState

class VlangDebuggerSettingsConfigurable : Configurable {
    data class Model(
        var lldbPath: String,
        var downloadAutomatically: Boolean,
        var stopsAtPanics: Boolean,
        var showStrMethodResult: Boolean,
        var dontUsePrettyPrinters: Boolean,
        var dontStepIntoGeneratedFunctions: Boolean,
    )

    private val mainPanel: DialogPanel
    private val model = Model(
        lldbPath = "",
        downloadAutomatically = true,
        stopsAtPanics = true,
        showStrMethodResult = true,
        dontUsePrettyPrinters = false,
        dontStepIntoGeneratedFunctions = false,
    )

    init {
        mainPanel = panel {
            row("LLDB path:") {
                textFieldWithBrowseButton(
                    "Select Directory with LLDB",
                    null,
                    FileChooserDescriptorFactory.createSingleFolderDescriptor()
                )
                    .align(AlignX.FILL)
                    .bindText(model::lldbPath)
                    .comment("Path to the LLDB debugger executable")
            }
            row {
                checkBox("Download and update debugger automatically")
                    .bindSelected(model::downloadAutomatically)
            }
            row {
                checkBox("Stop execution when panics")
                    .bindSelected(model::stopsAtPanics)
                    .comment("When panic occurs, the debugger will stop execution for you to inspect the stack trace")
            }
            row {
                checkBox("Show result of str() method")
                    .bindSelected(model::showStrMethodResult)
                    .comment("""
                        When the debugger stops at a breakpoint, it will show the result of the user-defined <code>str()</> method (if any non mutable) right after variable name.
                        Note that if the method has more than 3 statements, then it will not be executed.
                        """.trimIndent())
            }
            row {
                checkBox("Don't step into generated functions")
                    .bindSelected(model::dontStepIntoGeneratedFunctions)
                    .comment("""
                        When you invoke the <b>Step Into</b> action, the debugger will skip all generated functions (e.g. <code>I_None___to_Interface_IError</code>).
                        This makes debugging easier, but you can disable this behavior if you want to see the generated code.
                        """.trimIndent())
            }
            row {
                checkBox("Don't use pretty printers")
                    .bindSelected(model::dontUsePrettyPrinters)
                    .comment("""
                        Pretty printers are used to show the values of variables in the debugger in a convenient readable form.
                        This setting allows you to <b>disable</b> them in order to be able to see how variables are represented in C code.
                        """.trimIndent())
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
                model.stopsAtPanics != settings.stopsAtPanics ||
                model.showStrMethodResult != settings.showStrMethodResult ||
                model.dontUsePrettyPrinters != settings.dontUsePrettyPrinters ||
                model.dontStepIntoGeneratedFunctions != settings.dontStepIntoGeneratedFunctions
    }

    override fun apply() {
        mainPanel.apply()

        val settings =VlangDebuggerState.getInstance()
        with(settings) {
            lldbPath = model.lldbPath
            downloadAutomatically = model.downloadAutomatically
            stopsAtPanics = model.stopsAtPanics
            showStrMethodResult = model.showStrMethodResult
            dontUsePrettyPrinters = model.dontUsePrettyPrinters
            dontStepIntoGeneratedFunctions = model.dontStepIntoGeneratedFunctions
        }
    }

    override fun reset() {
        val settings = VlangDebuggerState.getInstance()

        with(model) {
            lldbPath = settings.lldbPath ?: ""
            downloadAutomatically = settings.downloadAutomatically
            stopsAtPanics = settings.stopsAtPanics
            showStrMethodResult = settings.showStrMethodResult
            dontUsePrettyPrinters = settings.dontUsePrettyPrinters
            dontStepIntoGeneratedFunctions = settings.dontStepIntoGeneratedFunctions
        }

        mainPanel.reset()
    }
}
