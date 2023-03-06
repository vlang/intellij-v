package org.vlang.lang.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import org.vlang.ide.colors.VlangColor
import org.vlang.lang.doc.psi.*
import org.vlang.lang.psi.VlangDocElementTypes
import org.vlang.utils.ancestorStrict
import org.vlang.utils.descendantOfTypeStrict

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
