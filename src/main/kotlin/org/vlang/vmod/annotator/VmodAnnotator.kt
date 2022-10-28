package org.vlang.vmod.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import org.vlang.lang.vmod.psi.VmodFieldName

class VmodAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is VmodFieldName) {
            holder.textAttributes(element, DefaultLanguageHighlighterColors.INSTANCE_FIELD)
        }
    }

    private fun AnnotationHolder.textAttributes(element: PsiElement, textAttributes: TextAttributesKey) {
        newSilentAnnotation(HighlightSeverity.INFORMATION).range(element).textAttributes(textAttributes).create()
    }
}
