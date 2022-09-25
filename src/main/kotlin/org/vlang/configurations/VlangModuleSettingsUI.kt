package org.vlang.configurations

import com.intellij.openapi.Disposable
import com.intellij.openapi.module.Module
import com.intellij.openapi.options.ConfigurableUi
import com.intellij.openapi.util.Disposer
import com.intellij.util.ui.UIUtil
import org.vlang.project.VlangBuildTargetSettings
import org.vlang.project.VlangModuleSettings
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JPanel

class VlangModuleSettingsUI(module: Module, dialogMode: Boolean) : ConfigurableUi<VlangModuleSettings?>, Disposable {
    private var myPanel: JPanel? = null
    private var myBuildTagsPanel: JPanel? = null
    private var myVendoringPanel: JPanel? = null
//    private var myVendoringUI: VlangVendoringUI? = null
    private lateinit var myBuildTagsUI: VlangBuildTagsUI

    init {
        if (dialogMode) {
            myPanel!!.preferredSize = Dimension(400, -1)
        }
//        myVendoringUI.initPanel(module)
        myBuildTagsUI.initPanel(module)
    }

    override fun reset(settings: VlangModuleSettings) {
        myBuildTagsUI.reset(settings.getBuildTargetSettings())
//        myVendoringUI.reset(settings)
    }

    override fun isModified(settings: VlangModuleSettings): Boolean {
        return myBuildTagsUI.isModified(settings.getBuildTargetSettings())
    }

    override fun apply(settings: VlangModuleSettings) {
//        myVendoringUI.apply(settings)
        val newBuildTargetSettings = VlangBuildTargetSettings()
        myBuildTagsUI.apply(newBuildTargetSettings)
        settings.setBuildTargetSettings(newBuildTargetSettings)
    }

    override fun getComponent(): JComponent {
        return myPanel!!
    }

    private fun createUIComponents() {
//        myVendoringUI = VlangVendoringUI()
        myBuildTagsUI = VlangBuildTagsUI()
//        myVendoringPanel = myVendoringUI.getPanel()
        myBuildTagsPanel = myBuildTagsUI.panel
    }

    override fun dispose() {
//        Disposer.dispose(myVendoringUI)
        Disposer.dispose(myBuildTagsUI)
        UIUtil.dispose(myPanel)
    }
}
