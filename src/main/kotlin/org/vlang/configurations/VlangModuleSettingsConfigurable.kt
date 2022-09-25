package org.vlang.configurations

import com.intellij.openapi.module.Module
import com.intellij.openapi.options.ConfigurableBase
import org.vlang.project.VlangModuleSettings

class VlangModuleSettingsConfigurable(private val myModule: Module, private val myDialogMode: Boolean) :
    ConfigurableBase<VlangModuleSettingsUI, VlangModuleSettings>("vlang.project.settings", "V Project Settings", null) {

    override fun getSettings(): VlangModuleSettings {
        return VlangModuleSettings.getInstance(myModule)
    }

    override fun createUi(): VlangModuleSettingsUI {
        return VlangModuleSettingsUI(myModule, myDialogMode)
    }
}
