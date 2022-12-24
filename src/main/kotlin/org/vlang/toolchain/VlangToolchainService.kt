package org.vlang.toolchain

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.Attribute
import org.vlang.configurations.VlangLibrariesUtil

@State(
    name = "V Toolchain",
    storages = [Storage(StoragePathMacros.WORKSPACE_FILE)]
)
@Service
class VlangToolchainService(private val project: Project) : PersistentStateComponent<VlangToolchainService.ToolchainState?> {
    private var state = ToolchainState()
    val toolchainLocation: String
        get() = state.toolchainLocation

    @Volatile
    private var toolchain: VlangToolchain = VlangToolchain.NULL

    fun setToolchain(project: Project, newToolchain: VlangToolchain) {
        toolchain = newToolchain
        state.toolchainLocation = newToolchain.homePath()
        VlangLibrariesUtil.updateLibraries(project)
    }

    fun toolchain(): VlangToolchain {
        val currentLocation = state.toolchainLocation
        if (toolchain == VlangToolchain.NULL && currentLocation.isNotEmpty()) {
            setToolchain(project, VlangToolchain.fromPath(currentLocation))
        }
        return toolchain
    }

    fun isNotSet(): Boolean = toolchain == VlangToolchain.NULL

    override fun getState() = state

    override fun loadState(state: ToolchainState) {
        XmlSerializerUtil.copyBean(state, this.state)
    }

    companion object {
        val Project.toolchainSettings
            get() = service<VlangToolchainService>()
    }

    class ToolchainState {
        @Attribute("url")
        var toolchainLocation: String = ""
    }
}
