package org.vlang.ide.hints

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
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.endOffset
import org.vlang.lang.VlangLanguage
import org.vlang.lang.psi.VlangCallExpr
import org.vlang.utils.childOfType
import org.vlang.utils.parentNth
import javax.swing.JPanel

@Suppress("UnstableApiUsage")
class VlangChainMethodInlayHintsProvider : InlayHintsProvider<VlangChainMethodInlayHintsProvider.Settings> {
    data class Settings(
        var empty: Boolean = true,
    )

    override val group = InlayGroup.METHOD_CHAINS_GROUP
    override val description = "Show method chain hints"
    override val key = KEY
    override val name = "Method chains"
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
                if (element !is VlangCallExpr) return true
                if (element.expression?.childOfType<VlangCallExpr>() != null) return true

                val chain = collectChain(element)

                for (callExpr in chain.dropLast(1)) {
                    if (!callExpr.isLastOnLine) continue
                    val type = callExpr.getType(null) ?: continue
                    val presentation = typeHintsFactory.typeHint(type, callExpr)
                    val finalPresentation = presentation.withDisableAction(element.project)

                    sink.addInlineElement(
                        callExpr.endOffset,
                        relatesToPrecedingText = true,
                        presentation = finalPresentation,
                        placeAtTheEndOfLine = false
                    )
                }

                return true
            }

            private fun collectChain(call: VlangCallExpr): List<VlangCallExpr> {
                val chain = mutableListOf<VlangCallExpr>()
                var current = call
                while (true) {
                    chain.add(current)
                    val prevCurrent = current
                    current = current.parentNth(2) ?: break
                    if (!PsiTreeUtil.isAncestor(current.expression, prevCurrent, false)) break
                }
                return chain
            }

            private val PsiElement.isLastOnLine: Boolean
                get() {
                    return when (val sibling = this.nextSibling) {
                        is PsiWhiteSpace -> sibling.textContains('\n') || sibling.isLastOnLine
                        is PsiComment    -> sibling.isLastOnLine
                        else             -> false
                    }
                }
        }

    private fun InlayPresentation.withDisableAction(project: Project): InsetPresentation = InsetPresentation(
        MenuOnClickPresentation(this, project) {
            listOf(
                InlayProviderDisablingAction(name, VlangLanguage, project, key),
                object : AnAction("Hints Settings...") {
                    override fun actionPerformed(e: AnActionEvent) {
                        showInlaySettings(project, VlangLanguage, null)
                    }
                }
            )
        }, left = 1
    )

    companion object {
        private val KEY: SettingsKey<Settings> = SettingsKey("chain-methods.hints")
    }
}
