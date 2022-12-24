package org.vlang.toolchain

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "V Toolchains",
    storages = [Storage("VlangToolchains.xml")]
)
class VlangKnownToolchainsState : PersistentStateComponent<VlangKnownToolchainsState?> {
    companion object {
        fun getInstance() = service<VlangKnownToolchainsState>()
    }

    var knownToolchains: Set<String> = emptySet()

    fun isKnown(homePath: String): Boolean {
        return knownToolchains.contains(homePath)
    }

    fun add(toolchain: VlangToolchain) {
        knownToolchains = knownToolchains + toolchain.homePath()
    }

    override fun getState() = this

    override fun loadState(state: VlangKnownToolchainsState) {
        XmlSerializerUtil.copyBean(state, this)
    }
}
