package io.vlang.lsp

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "VlangLspSettings",
    storages = [Storage("VlangLsp.xml")]
)
class VlangLspSettings : PersistentStateComponent<VlangLspSettings?> {
    companion object {
        fun getInstance() = service<VlangLspSettings>()
    }

    var suppressWhenLspActive: Boolean = true
    var suppressInlayHints: Boolean = true
    var suppressSemanticHighlighting: Boolean = true
    var suppressDiagnostics: Boolean = true
    var suppressCompletion: Boolean = false

    override fun getState() = this

    override fun loadState(state: VlangLspSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }
}
