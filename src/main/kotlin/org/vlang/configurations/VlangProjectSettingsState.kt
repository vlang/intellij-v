package org.vlang.configurations

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "VROOT",
    storages = [Storage(StoragePathMacros.WORKSPACE_FILE)]
)
class VlangProjectSettingsState : PersistentStateComponent<VlangProjectSettingsState?> {
    companion object {
        val Project.projectSettings
            get() = service<VlangProjectSettingsState>()
    }

    var customStdlibLocation: String? = null
    var customModulesLocation: String? = null

    override fun getState() = this

    override fun loadState(state: VlangProjectSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }
}
