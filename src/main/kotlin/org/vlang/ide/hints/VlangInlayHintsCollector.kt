package org.vlang.ide.hints

import com.intellij.codeInsight.hints.*
import com.intellij.codeInsight.hints.presentation.*
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.project.DumbService
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.refactoring.suggested.endOffset
import org.vlang.lang.psi.VlangVarDefinition
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx

@Suppress("UnstableApiUsage")
class VlangInlayHintsCollector(
    private val editor: Editor,
    private val file: PsiFile,
    private val settings: VlangInlayHintsProvider.Settings,
    private val sink: InlayHintsSink,
) : FactoryInlayHintsCollector(editor) {

    private val textMetricsStorage = InlayTextMetricsStorage(editor as EditorImpl)
    private val offsetFromTopProvider = object : InsetValueProvider {
        override val top: Int =
            (textMetricsStorage.getFontMetrics(false).offsetFromTop()).toInt()
    }

    override fun collect(element: PsiElement, editor: Editor, sink: InlayHintsSink): Boolean {
        // If the indexing process is in progress.
        if (file.project.service<DumbService>().isDumb) return true

        if (element is VlangVarDefinition) {
            showAnnotation(element)
        }

        return true
    }

    private fun showAnnotation(element: VlangVarDefinition) {
        val type = element.getTypeInner(null)?.resolveType() ?: return
        val readableName = type.toEx().readableName(element)
        val visibilityPresentation = withInlayAttributes(
            container(factory.smallText(": $readableName")),
            DefaultLanguageHighlighterColors.INLINE_PARAMETER_HINT
        )

        sink.addInlineElement(
            element.endOffset,
            relatesToPrecedingText = true,
            presentation = visibilityPresentation,
            placeAtTheEndOfLine = false
        )
    }

    private fun container(base: InlayPresentation): InlayPresentation {
        val rounding = withInlayAttributes(RoundWithBackgroundPresentation(
            InsetPresentation(
                base,
                left = 4,
                right = 4,
                top = 2,
                down = 1,
            ),
            8,
            8
        ), DefaultLanguageHighlighterColors.INLAY_DEFAULT)
        return DynamicInsetPresentation(rounding, offsetFromTopProvider)
    }

    private fun withInlayAttributes(base: InlayPresentation, attributes: TextAttributesKey) =
        WithAttributesPresentation(
            base, attributes, editor,
            WithAttributesPresentation.AttributesFlags().withIsDefault(true)
        )

    companion object {
        val LOG = logger<VlangInlayHintsCollector>()
    }
}
