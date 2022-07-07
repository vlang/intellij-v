package org.vlang.lang.annotator

import com.intellij.ide.highlighter.JavaHighlightingColors
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import com.intellij.psi.util.findParentOfType
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.*

class VlangAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is VlangFunctionDeclaration) {
            val ident = element.getIdentifier()
            holder.textAttributes(ident, JavaHighlightingColors.METHOD_DECLARATION_ATTRIBUTES)
        }

        if (element is VlangReferenceExpression) {
            val ref = element.reference
            val resolvedElement = ref.multiResolve(false).firstOrNull()?.element ?: return

            if (resolvedElement is VlangFunctionDeclaration) {
                holder.textAttributes(element.getIdentifier(), JavaHighlightingColors.METHOD_DECLARATION_ATTRIBUTES)
                return
            }

            if (resolvedElement is VlangStructDeclaration) {
                holder.textAttributes(element.getIdentifier(), JavaHighlightingColors.CLASS_NAME_ATTRIBUTES)
                return
            }
        }

        if (element.elementType == VlangTypes.TYPE_REFERENCE_EXPRESSION) {
            holder.textAttributes(element, JavaHighlightingColors.TYPE_PARAMETER_NAME_ATTRIBUTES)
        }

        if (element.elementType == VlangTypes.IDENTIFIER && element.parent is VlangPlainAttribute) {
            holder.textAttributes(element, JavaHighlightingColors.ANNOTATION_NAME_ATTRIBUTES)
        }

        if (element is VlangFieldName) {
            holder.textAttributes(element, JavaHighlightingColors.INSTANCE_FIELD_ATTRIBUTES)
        }

        if (element.elementType == VlangTypes.IDENTIFIER && element.parent is VlangInterfaceMethodDeclaration) {
            holder.textAttributes(element, JavaHighlightingColors.METHOD_DECLARATION_ATTRIBUTES)
        }

        if (element.elementType == VlangTypes.IDENTIFIER && element.parent is VlangEnumDeclaration) {
            holder.textAttributes(element, JavaHighlightingColors.ENUM_NAME_ATTRIBUTES)
        }

        if (element.elementType == VlangTypes.IDENTIFIER && element.parent is VlangStructType) {
            holder.textAttributes(element, JavaHighlightingColors.CLASS_NAME_ATTRIBUTES)
        }

        if (element.elementType == VlangTypes.IDENTIFIER && element.parent is VlangInterfaceDeclaration) {
            holder.textAttributes(element, JavaHighlightingColors.INTERFACE_NAME_ATTRIBUTES)
        }

        if (element.elementType == VlangTypes.IDENTIFIER && element.parent is VlangEnumFieldDeclaration) {
            holder.textAttributes(element, JavaHighlightingColors.STATIC_FINAL_FIELD_ATTRIBUTES)
        }

        if (element.elementType == VlangTypes.IDENTIFIER && element.parent is VlangStatement) {
            holder.textAttributes(element, JavaHighlightingColors.INTERFACE_NAME_ATTRIBUTES)
        }

        if (element.elementType == VlangTypes.IDENTIFIER && element.parent is VlangEnumFetch) {
            holder.textAttributes(element, JavaHighlightingColors.STATIC_FINAL_FIELD_ATTRIBUTES)
        }

        if (element.elementType == VlangTypes.IDENTIFIER && element.parent is VlangLabelRef) {
            holder.textAttributes(element, VlangHighlightingData.VLANG_LABEL)
        }

        if (element.elementType == VlangTypes.IDENTIFIER && element.findParentOfType<VlangFieldInitializationKey>() != null) {
            holder.textAttributes(element, JavaHighlightingColors.METHOD_DECLARATION_ATTRIBUTES)
        }
    }

    private fun AnnotationHolder.textAttributes(element: PsiElement, textAttributes: TextAttributesKey) {
        newSilentAnnotation(HighlightSeverity.INFORMATION).range(element).textAttributes(textAttributes).create()
    }
}
