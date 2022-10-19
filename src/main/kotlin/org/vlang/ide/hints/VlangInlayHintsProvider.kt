package org.vlang.ide.hints

import com.intellij.codeInsight.hints.*
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import javax.swing.JPanel

@Suppress("UnstableApiUsage")
class VlangInlayHintsProvider : InlayHintsProvider<VlangInlayHintsProvider.Settings> {
    data class Settings(
        var ranges: Boolean = true,
    )

    override val group = InlayGroup.VALUES_GROUP
    override val description = "Show V hints"
    override val key = KEY
    override val name = "V hints"
    override val previewText = null

    override fun createConfigurable(settings: Settings) = object : ImmediateConfigurable {
        override val mainCheckboxText = "Use inline hints for V"

        override val cases = listOf(
            ImmediateConfigurable.Case("Show for ranges", "vlang.hints.ranges", settings::ranges)
        )

        override fun createComponent(listener: ChangeListener) = JPanel()
    }

    override fun getCaseDescription(case: ImmediateConfigurable.Case): String? = when (case.id) {
        "vlang.hints.ranges" -> "Show hint for range."
        else -> null
    }

    override fun createSettings() = Settings()

    override fun getCollectorFor(file: PsiFile, editor: Editor, settings: Settings, sink: InlayHintsSink) =
        VlangInlayHintsCollector(editor, file, settings, sink)

    companion object {
        private val KEY: SettingsKey<Settings> = SettingsKey("vlang.hints")
    }
}
