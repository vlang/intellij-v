package org.vlang.configurations

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "V Toolchains",
    storages = [Storage("VlangToolchains.xml")]
)
class VlangToolchainsState : PersistentStateComponent<VlangToolchainsState?> {
    companion object {
        fun getInstance() = service<VlangToolchainsState>()
    }

    var knownToolchains: Set<String> = emptySet()

    fun isKnown(toolchain: String): Boolean {
        return knownToolchains.contains(toolchain)
    }

    fun add(toolchain: String) {
        knownToolchains = knownToolchains + toolchain
    }

    override fun getState() = this

    override fun loadState(state: VlangToolchainsState) {
        XmlSerializerUtil.copyBean(state, this)
    }
}
