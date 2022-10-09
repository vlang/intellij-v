package org.vlang.configurations

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "VlangProjectSettingsState",
    storages = [Storage(StoragePathMacros.WORKSPACE_FILE)]
)
class VlangProjectSettingsState : PersistentStateComponent<VlangProjectSettingsState?> {
    companion object {
        val Project.projectSettings
            get() = service<VlangProjectSettingsState>()
    }

    var toolchainLocation: String = ""
    var toolchainVersion: String = ""
    var stdlibLocation: String = ""
    var modulesLocation: String = ""

    val compilerLocation: String?
        get() = VlangConfigurationUtil.getCompilerExeLocation(toolchainLocation)

    override fun getState() = this

    override fun loadState(state: VlangProjectSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }
}
