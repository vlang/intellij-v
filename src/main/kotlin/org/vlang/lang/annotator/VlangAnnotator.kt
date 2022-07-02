package org.vlang.lang.annotator

import com.intellij.ide.highlighter.JavaHighlightingColors
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangFunctionDeclaration
import org.vlang.lang.psi.VlangReferenceExpression

class VlangAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is VlangFunctionDeclaration) {
            val ident = element.getIdentifier() ?: return
            holder.textAttributes(ident, JavaHighlightingColors.METHOD_DECLARATION_ATTRIBUTES)
        }

        if (element is VlangReferenceExpression) {
            val ref = element.reference
            val func = ref.resolve()
            if (func != null) {
                holder.textAttributes(element.getIdentifier(), JavaHighlightingColors.METHOD_DECLARATION_ATTRIBUTES)
            }
        }
    }

    private fun AnnotationHolder.textAttributes(element: PsiElement, textAttributes: TextAttributesKey) {
        newSilentAnnotation(HighlightSeverity.INFORMATION).range(element).textAttributes(textAttributes).create()
    }
}
