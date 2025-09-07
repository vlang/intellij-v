package io.vlang.lang.formatter

import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleConfigurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider
import io.vlang.lang.VlangLanguage

class VlangCodeStyleSettingsProvider : CodeStyleSettingsProvider() {

    override fun getLanguage() = VlangLanguage

    override fun createCustomSettings(settings: CodeStyleSettings) = VlangCodeStyleSettings(settings)

    override fun getConfigurableDisplayName() = "V"

    override fun createConfigurable(
        settings: CodeStyleSettings,
        modelSettings: CodeStyleSettings,
    ): CodeStyleConfigurable {
        return object : CodeStyleAbstractConfigurable(settings, modelSettings, this.configurableDisplayName) {
            override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel {
                return VlangCodeStyleMainPanel(currentSettings, settings)
            }
        }
    }

    private class VlangCodeStyleMainPanel(currentSettings: CodeStyleSettings, settings: CodeStyleSettings) :
        TabbedLanguageCodeStylePanel(VlangLanguage, currentSettings, settings)
}
