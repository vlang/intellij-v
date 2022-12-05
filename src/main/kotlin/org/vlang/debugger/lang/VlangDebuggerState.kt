package org.vlang.debugger.lang

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "V Debugger",
    storages = [Storage("VlangDebugger.xml")]
)
class VlangDebuggerState : PersistentStateComponent<VlangDebuggerState?> {
    companion object {
        fun getInstance() = service<VlangDebuggerState>()
    }

    var lldbPath: String? = null
    var downloadAutomatically: Boolean = true
    var stopsAtPanics: Boolean = true

    override fun getState() = this

    override fun loadState(state: VlangDebuggerState) {
        XmlSerializerUtil.copyBean(state, this)
    }
}
