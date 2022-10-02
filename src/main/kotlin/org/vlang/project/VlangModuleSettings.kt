package org.vlang.project

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros
import com.intellij.openapi.module.Module
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.util.ThreeState
import com.intellij.util.messages.Topic
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.OptionTag
import com.intellij.util.xmlb.annotations.Property
import org.vlang.configurations.VlangModuleSettingsConfigurable

@State(name = "VlangModuleSettings", storages = [Storage(file = StoragePathMacros.MODULE_FILE)])
class VlangModuleSettings(private val myModule: Module) : PersistentStateComponent<VlangModuleSettings.VlangModuleSettingsState?> {
    private val state = VlangModuleSettingsState()

    fun getVendoringEnabled(): ThreeState {
        return state.vendoring
    }

    fun setVendoringEnabled(vendoringEnabled: ThreeState) {
        if (vendoringEnabled != state.vendoring) {
            cleanResolveCaches()
        }
        state.vendoring = vendoringEnabled
    }

    fun getBuildTargetSettings(): VlangBuildTargetSettings {
        return state.buildTargetSettings
    }

    fun setBuildTargetSettings(buildTargetSettings: VlangBuildTargetSettings) {
        if (buildTargetSettings != state.buildTargetSettings) {
            XmlSerializerUtil.copyBean<Any>(buildTargetSettings, state.buildTargetSettings)
            if (!myModule.isDisposed) {
                myModule.project.messageBus.syncPublisher(TOPIC).changed(myModule)
            }
            cleanResolveCaches()
            state.buildTargetSettings.incModificationCount()
        }
    }

    private fun cleanResolveCaches() {
        val project = myModule.project
        if (!project.isDisposed) {
            ResolveCache.getInstance(project).clearCache(true)
            DaemonCodeAnalyzer.getInstance(project).restart()
        }
    }

    override fun getState() = state

    override fun loadState(state: VlangModuleSettingsState) {
        XmlSerializerUtil.copyBean<VlangModuleSettingsState>(state, this.state)
    }

    interface BuildTargetListener {
        fun changed(module: Module)
    }

    class VlangModuleSettingsState {
        @OptionTag
        var vendoring = ThreeState.UNSURE

        @Property(surroundWithTag = false)
        var buildTargetSettings = VlangBuildTargetSettings()
    }

    companion object {
        val TOPIC = Topic.create("build target changed", BuildTargetListener::class.java)
        
        fun getInstance(module: Module): VlangModuleSettings {
            return module.getService(VlangModuleSettings::class.java)
        }

        fun showModulesConfigurable(project: Project) {
            ApplicationManager.getApplication().assertIsDispatchThread()
            if (!project.isDisposed) {
//                ShowSettingsUtil.getInstance().editConfigurable(project,
//                    VlangProjectSettingsConfigurable(project)
//                )
            }
        }

        fun showModulesConfigurable(module: Module) {
            ApplicationManager.getApplication().assertIsDispatchThread()
            if (!module.isDisposed) {
                ShowSettingsUtil.getInstance().editConfigurable(module.project, VlangModuleSettingsConfigurable(module, true))
            }
        }
    }
}