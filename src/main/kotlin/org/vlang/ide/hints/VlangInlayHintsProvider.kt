package org.vlang.ide.hints

import com.intellij.codeInsight.hints.*
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import javax.swing.JPanel

@Suppress("UnstableApiUsage")
class VlangInlayHintsProvider : InlayHintsProvider<VlangInlayHintsProvider.Settings> {
    data class Settings(
        var showForRanges: Boolean = true,
        var showForVariables: Boolean = true,
        var showForErrVariable: Boolean = true,
        var showForConstants: Boolean = true,
    )

    override val group = InlayGroup.VALUES_GROUP
    override val description = "Show hints for:"
    override val key = KEY
    override val name = "V hints"
    override val previewText = null

    override fun createConfigurable(settings: Settings) = object : ImmediateConfigurable {
        override val mainCheckboxText = "Use inline hints for V"

        override val cases = listOf(
            ImmediateConfigurable.Case("Show for ranges", "ranges", settings::showForRanges),
            ImmediateConfigurable.Case("Show for variables", "variables", settings::showForVariables),
            ImmediateConfigurable.Case("Show for implicit err variable", "err_variable", settings::showForErrVariable),
            ImmediateConfigurable.Case("Show for constants", "constants", settings::showForConstants),
        )

        override fun createComponent(listener: ChangeListener) = JPanel()
    }

    override fun getCaseDescription(case: ImmediateConfigurable.Case): String? = when (case.id) {
        "ranges" -> "Show hint for ranges."
        "variables" -> "Show hint for variables."
        "err_variable" -> "Show hint for implicit err variable."
        "constants" -> "Show hint for constants."
        else -> null
    }

    override fun createSettings() = Settings()

    override fun getCollectorFor(file: PsiFile, editor: Editor, settings: Settings, sink: InlayHintsSink) =
        VlangInlayHintsCollector(editor, file, settings, sink, KEY)

    companion object {
        private val KEY: SettingsKey<Settings> = SettingsKey("vlang.hints")
    }
}
