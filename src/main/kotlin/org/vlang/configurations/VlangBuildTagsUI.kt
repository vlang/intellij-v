package org.vlang.configurations

import com.intellij.ProjectTopics
import com.intellij.openapi.Disposable
import com.intellij.openapi.module.Module
import com.intellij.openapi.roots.ModuleRootAdapter
import com.intellij.openapi.roots.ModuleRootEvent
import com.intellij.openapi.roots.ModuleRootListener
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.MutableCollectionComboBoxModel
import com.intellij.util.containers.ContainerUtil
import com.intellij.util.ui.UIUtil
import org.vlang.project.VlangBuildTargetSettings
import org.vlang.sdk.VlangSdkService
import javax.swing.JPanel

class VlangBuildTagsUI : Disposable {
    private lateinit var myPanel: JPanel
    private lateinit var myOSCombo: ComboBox<String>
    private lateinit var myArchCombo: ComboBox<String>

    init {
        myPanel.border = IdeBorderFactory.createTitledBorder("Build tags")
        myOSCombo.model = createModel(listOf("MacOS"), "MacOS")
        myArchCombo.model = createModel(listOf("arm64"), "arm64")
    }

    fun initPanel(module: Module) {
        if (!module.isDisposed) {
            val connection = module.messageBus.connect(this)
            connection.subscribe<ModuleRootListener>(ProjectTopics.PROJECT_ROOTS, object : ModuleRootAdapter() {
                override fun rootsChanged(event: ModuleRootEvent) {
                    initComboValues(module)
                }
            })
            initComboValues(module)
        }
    }

    private fun initComboValues(module: Module) {
        if (!module.isDisposed) {
            val sdkVersion = VlangSdkService.getInstance(module.project).getSdkVersion(module)
        }
    }

    fun isModified(buildTargetSettings: VlangBuildTargetSettings): Boolean {
        return !buildTargetSettings.os.equals(selected(myOSCombo, "MacOS")) ||
                !buildTargetSettings.arch.equals(selected(myArchCombo, "arm64"))
    }

    fun apply(buildTargetSettings: VlangBuildTargetSettings) {
        buildTargetSettings.os = selected(myOSCombo, "MacOS")
        buildTargetSettings.arch = selected(myArchCombo, "arm64")
    }

    fun reset(buildTargetSettings: VlangBuildTargetSettings) {
        myOSCombo.setSelectedItem(expandDefault(buildTargetSettings.os, "MacOS"))
        myArchCombo.setSelectedItem(expandDefault(buildTargetSettings.arch, "arm64"))
    }

    val panel = myPanel

    override fun dispose() {
        UIUtil.dispose(myPanel)
        UIUtil.dispose(myOSCombo)
        UIUtil.dispose(myArchCombo)
    }

    private fun createUIComponents() {
    }

    companion object {
        private const val ENABLED = "Enabled"
        private const val DISABLED = "Disabled"

        private fun selected(comboBox: ComboBox<*>, defaultValue: String): String {
            val item = comboBox.selectedItem
            return if (item is String) {
                if (defaultValue == item) VlangBuildTargetSettings.DEFAULT else item
            } else VlangBuildTargetSettings.DEFAULT
        }

        private fun expandDefault(value: String, defaultValue: String): String {
            return if (VlangBuildTargetSettings.DEFAULT.equals(value)) defaultValue else value
        }

        private fun createModel(values: Collection<String>, defaultValue: String): MutableCollectionComboBoxModel<String> {
            val items: MutableList<String> = ContainerUtil.newArrayList(defaultValue)
            items.addAll(ContainerUtil.sorted(values))
            return MutableCollectionComboBoxModel<String>(items, defaultValue)
        }
    }
}
