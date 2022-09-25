package org.vlang.configurations

import com.intellij.openapi.module.ModuleConfigurationEditor
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.roots.ui.configuration.*
import org.vlang.sdk.VlangModuleType
import javax.swing.JComponent

class VlangModuleEditorsProvider : ModuleConfigurationEditorProvider {
    override fun createEditors(state: ModuleConfigurationState): Array<ModuleConfigurationEditor> {
        val rootModel = state.currentRootModel
        val module = rootModel.module
        if (ModuleType.get(module) !is VlangModuleType) {
            return ModuleConfigurationEditor.EMPTY
        }
        val moduleName = module.name
        val editors = mutableListOf<ModuleConfigurationEditor>()
        editors.add(ContentEntriesEditor(moduleName, state))
        editors.add(OutputEditorEx(state))
        editors.add(ClasspathEditor(state))
        return editors.toTypedArray()
    }

    internal class OutputEditorEx(state: ModuleConfigurationState?) : OutputEditor(state) {
        override fun createComponentImpl(): JComponent {
            val component = super.createComponentImpl()
            component.remove(1) // todo: looks ugly
            return component
        }
    }
}
