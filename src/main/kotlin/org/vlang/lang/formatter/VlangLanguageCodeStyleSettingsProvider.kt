package org.vlang.lang.formatter

import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import org.vlang.lang.VlangLanguage

class VlangLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {
    override fun getLanguage() = VlangLanguage

    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
        // TODO:
//        if (settingsType == SettingsType.SPACING_SETTINGS) {
//            consumer.showStandardOptions("SPACE_AROUND_ASSIGNMENT_OPERATORS")
//            consumer.renameStandardOption("SPACE_AROUND_ASSIGNMENT_OPERATORS", "Separator")
//        } else if (settingsType == SettingsType.BLANK_LINES_SETTINGS) {
//            consumer.showStandardOptions("KEEP_BLANK_LINES_IN_CODE")
//        }
    }

    override fun getIndentOptionsEditor() = SmartIndentOptionsEditor()

    override fun customizeDefaults(commonSettings: CommonCodeStyleSettings, indentOptions: CommonCodeStyleSettings.IndentOptions) {
        indentOptions.INDENT_SIZE = 4
        indentOptions.CONTINUATION_INDENT_SIZE = 4
        indentOptions.TAB_SIZE = 4
        indentOptions.USE_TAB_CHARACTER = true
        commonSettings.BLOCK_COMMENT_AT_FIRST_COLUMN = false
        commonSettings.LINE_COMMENT_AT_FIRST_COLUMN = false
        commonSettings.BLANK_LINES_AROUND_CLASS
    }

    override fun getCodeSample(settingsType: SettingsType): String {
        return """
            // Vlang code sample
            fn main() {
                println('Hello, World!')
            }
        """.trimIndent()
    }
}
