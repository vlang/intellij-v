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
import com.intellij.refactoring.suggested.startOffset
import org.vlang.lang.psi.VlangRangeExpr

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

        if (element is VlangRangeExpr && settings.ranges) {
            showAnnotation(element)
        }

        return true
    }

    private fun showAnnotation(element: VlangRangeExpr) {
        val op = element.range ?: element.tripleDot ?: return
        val inclusive = element.tripleDot != null

        addRangeHint(op.startOffset, true)
        addRangeHint(op.endOffset, inclusive)
    }

    private fun addRangeHint(offset: Int, inclusive: Boolean) {
        val visibilityPresentation = withInlayAttributes(
            container(factory.smallText(if (inclusive) "≤" else "<")),
            DefaultLanguageHighlighterColors.INLINE_PARAMETER_HINT
        )

        sink.addInlineElement(
            offset,
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
                top = 0,
                down = 0,
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
