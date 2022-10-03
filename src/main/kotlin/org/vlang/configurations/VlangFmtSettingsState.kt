package org.vlang.configurations

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "VlangFmtSettingsState",
    storages = [Storage(StoragePathMacros.WORKSPACE_FILE)]
)
class VlangFmtSettingsState : PersistentStateComponent<VlangFmtSettingsState?> {
    companion object {
        val Project.vfmtSettings
            get() = service<VlangFmtSettingsState>()
    }

    var additionalArguments: String = ""
    var envs: Map<String, String> = emptyMap()
    var runVfmtOnSave: Boolean = false

    override fun getState() = this

    override fun loadState(state: VlangFmtSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }
}
