package org.vlang.ide.codeInsight.imports

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "Vlang", storages = [Storage("editor.codeinsight.xml")])
class VlangCodeInsightSettings : PersistentStateComponent<VlangCodeInsightSettings?> {
    var isShowImportPopup = true
    var isAddUnambiguousImportsOnTheFly = true

    override fun getState() = this

    override fun loadState(state: VlangCodeInsightSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): VlangCodeInsightSettings = ApplicationManager.getApplication().getService(VlangCodeInsightSettings::class.java)
    }
}
