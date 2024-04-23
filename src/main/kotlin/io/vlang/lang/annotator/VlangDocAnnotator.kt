package io.vlang.lang.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import io.vlang.ide.colors.VlangColor
import io.vlang.lang.doc.psi.*
import io.vlang.lang.psi.VlangDocElementTypes
import io.vlang.utils.ancestorStrict
import io.vlang.utils.descendantOfTypeStrict

class VlangDocAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (holder.isBatchMode) return
        val color = when {
            element is VlangDocComment && element.owner != null -> VlangColor.DOC_COMMENT
            element.elementType == VlangDocElementTypes.DOC_DATA -> when (element.parent) {
                is VlangDocCodeFence                                   -> VlangColor.DOC_CODE
                is VlangDocCodeFenceStartEnd,
                is VlangDocCodeFenceLang -> VlangColor.DOC_CODE
                is VlangDocCodeSpan -> if (element.ancestorStrict<VlangDocLink>() == null) {
                    VlangColor.DOC_CODE
                } else {
                    null
                }
                is VlangDocCodeBlock -> VlangColor.DOC_CODE
                else -> null
            }
            element is VlangDocEmphasis   -> VlangColor.DOC_EMPHASIS
            element is VlangDocStrong     -> VlangColor.DOC_STRONG
            element is VlangDocAtxHeading -> VlangColor.DOC_HEADING
            element is VlangDocLink
                    && element.descendantOfTypeStrict<VlangDocGap>() == null -> VlangColor.DOC_LINK
            else -> null
        } ?: return

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION).textAttributes(color.textAttributesKey).create()
    }
}
