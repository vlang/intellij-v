package org.vlang.ide.hints

import com.intellij.codeInsight.daemon.impl.InlayHintsPassFactoryInternal
import com.intellij.codeInsight.hints.*
import com.intellij.codeInsight.hints.presentation.InlayPresentation
import com.intellij.codeInsight.hints.presentation.InsetPresentation
import com.intellij.codeInsight.hints.presentation.MenuOnClickPresentation
import com.intellij.codeInsight.hints.settings.showInlaySettings
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.refactoring.suggested.endOffset
import org.vlang.lang.VlangLanguage
import org.vlang.lang.psi.VlangEnumDeclaration
import org.vlang.lang.psi.VlangEnumFieldDeclaration
import javax.swing.JPanel

@Suppress("UnstableApiUsage")
class VlangEnumValuesInlayHintsProvider : InlayHintsProvider<VlangEnumValuesInlayHintsProvider.Settings> {
    data class Settings(
        var showOnlyDecimal: Boolean = false,
    )

    override val group = InlayGroup.VALUES_GROUP
    override val description = "Show enum value hints"
    override val key = KEY
    override val name = "Enum values"
    override val previewText = null

    override fun createConfigurable(settings: Settings) = object : ImmediateConfigurable {
        override val mainCheckboxText = "Show hints for:"
        override val cases = emptyList<ImmediateConfigurable.Case>()
        override fun createComponent(listener: ChangeListener) = JPanel()
    }

    override fun createSettings() = Settings()

    override fun getCollectorFor(file: PsiFile, editor: Editor, settings: Settings, sink: InlayHintsSink) =
        object : FactoryInlayHintsCollector(editor) {
            private val typeHintsFactory = VlangInlayHintsFactory(editor, factory)

            override fun collect(element: PsiElement, editor: Editor, sink: InlayHintsSink): Boolean {
                // If the indexing process is in progress.
                if (file.project.service<DumbService>().isDumb) return false
                if (element !is VlangEnumFieldDeclaration) return true
                if (element.expression != null) return true

                val value = element.enumFieldDefinition.constantValue() ?: return true
                val decl = element.parent.parent as? VlangEnumDeclaration ?: return true
                val valueText = valuePresentationText(decl, value, settings)

                val presentation = typeHintsFactory.enumValue(valueText)
                val finalPresentation = presentation.withActions(element.project, decl, settings)

                sink.addInlineElement(
                    element.enumFieldDefinition.endOffset,
                    relatesToPrecedingText = true,
                    presentation = finalPresentation,
                    placeAtTheEndOfLine = false
                )

                return true
            }

            private fun valuePresentationText(decl: VlangEnumDeclaration, value: Long, settings: Settings): String {
                if (settings.showOnlyDecimal) return value.toString()
                if (decl.isFlag) return "0b" + value.toString(2) + " ($value)"

                return value.toString()
            }
        }

    private fun InlayPresentation.withActions(project: Project, decl: VlangEnumDeclaration, settings: Settings): InsetPresentation =
        InsetPresentation(
            MenuOnClickPresentation(this, project) {
                mutableListOf(
                    InlayProviderDisablingAction(name, VlangLanguage, project, key),
                    object : AnAction("Hints Settings...") {
                        override fun actionPerformed(e: AnActionEvent) {
                            showInlaySettings(project, VlangLanguage, null)
                        }
                    },
                ).apply {
                    if (decl.isFlag) {
                        add(
                            object : AnAction(
                                if (settings.showOnlyDecimal) "Show Binary Representation" else "Show Only Decimal Representation"
                            ) {
                                override fun actionPerformed(e: AnActionEvent) {
                                    settings.showOnlyDecimal = !settings.showOnlyDecimal

                                    val inlayHintsSettings = InlayHintsSettings.instance()
                                    inlayHintsSettings.storeSettings(key, VlangLanguage, settings)
                                    InlayHintsPassFactoryInternal.forceHintsUpdateOnNextPass()
                                }
                            }
                        )
                    }
                }
            }, left = 1
        )

    companion object {
        private val KEY: SettingsKey<Settings> = SettingsKey("enum-values.hints")
    }
}
