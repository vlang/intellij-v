package io.vlang.configurations

import com.intellij.openapi.observable.properties.PropertyGraph
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel
import io.vlang.lsp.VlangLspSettings

class VlangLspSettingsConfigurable : Configurable {
    private val mainPanel: DialogPanel
    private val graph = PropertyGraph()

    private val suppressWhenLspActive = graph.property(true)
    private val suppressInlayHints = graph.property(true)
    private val suppressSemanticHighlighting = graph.property(true)
    private val suppressDiagnostics = graph.property(true)
    private val suppressCompletion = graph.property(false)

    init {
        mainPanel = panel {
            row {
                checkBox("Suppress duplicate features when a V language server is active")
                    .bindSelected(suppressWhenLspActive)
                    .comment(
                        "When a V language server is running via LSP4IJ, the native plugin can suppress " +
                        "features that the server already provides to avoid duplicates."
                    )
            }
            indent {
                row {
                    checkBox("Suppress inlay hints")
                        .bindSelected(suppressInlayHints)
                        .enabledIf(suppressWhenLspActive)
                }
                row {
                    checkBox("Suppress semantic highlighting")
                        .bindSelected(suppressSemanticHighlighting)
                        .enabledIf(suppressWhenLspActive)
                }
                row {
                    checkBox("Suppress diagnostics")
                        .bindSelected(suppressDiagnostics)
                        .enabledIf(suppressWhenLspActive)
                }
                row {
                    checkBox("Suppress code completion")
                        .bindSelected(suppressCompletion)
                        .enabledIf(suppressWhenLspActive)
                        .comment("Off by default — native completions may complement the language server.")
                }
            }
        }
    }

    override fun getDisplayName() = "Language Server (LSP4IJ)"
    override fun getPreferredFocusedComponent() = mainPanel
    override fun createComponent() = mainPanel

    override fun isModified(): Boolean {
        mainPanel.apply()
        val s = VlangLspSettings.getInstance()
        return suppressWhenLspActive.get() != s.suppressWhenLspActive ||
                suppressInlayHints.get() != s.suppressInlayHints ||
                suppressSemanticHighlighting.get() != s.suppressSemanticHighlighting ||
                suppressDiagnostics.get() != s.suppressDiagnostics ||
                suppressCompletion.get() != s.suppressCompletion
    }

    override fun apply() {
        mainPanel.apply()
        val s = VlangLspSettings.getInstance()
        s.suppressWhenLspActive = suppressWhenLspActive.get()
        s.suppressInlayHints = suppressInlayHints.get()
        s.suppressSemanticHighlighting = suppressSemanticHighlighting.get()
        s.suppressDiagnostics = suppressDiagnostics.get()
        s.suppressCompletion = suppressCompletion.get()
    }

    override fun reset() {
        val s = VlangLspSettings.getInstance()
        suppressWhenLspActive.set(s.suppressWhenLspActive)
        suppressInlayHints.set(s.suppressInlayHints)
        suppressSemanticHighlighting.set(s.suppressSemanticHighlighting)
        suppressDiagnostics.set(s.suppressDiagnostics)
        suppressCompletion.set(s.suppressCompletion)
        mainPanel.reset()
    }
}
