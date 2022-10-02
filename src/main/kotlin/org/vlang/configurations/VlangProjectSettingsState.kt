package org.vlang.configurations

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@Service
@State(
    name = "com.vk.admstorm.settings.AdmStormSettingsState",
    storages = [Storage("AdmStormPlugin.xml")]
)
class VlangProjectSettingsState : PersistentStateComponent<VlangProjectSettingsState?> {
    companion object {
        fun getInstance(project: Project) = project.service<VlangProjectSettingsState>()
    }

    var toolchainLocation: String = ""
    var toolchainVersion: String = ""
    var stdlibLocation: String = ""

    val compilerLocation: String?
        get() = VlangConfigurationUtil.getCompilerExeLocation(toolchainLocation)

    override fun getState() = this

    override fun loadState(state: VlangProjectSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }
}
