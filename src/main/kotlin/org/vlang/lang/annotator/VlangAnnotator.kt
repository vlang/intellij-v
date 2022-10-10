package org.vlang.lang.annotator

import com.intellij.ide.highlighter.JavaHighlightingColors
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.elementType
import org.vlang.ide.highlight.VlangHighlightingData
import org.vlang.lang.VlangParserDefinition
import org.vlang.lang.VlangTypes
import org.vlang.lang.completion.VlangCompletionUtil
import org.vlang.lang.psi.*

class VlangAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is VlangFunctionOrMethodDeclaration) {
            val ident = element.getIdentifier() ?: return
            val attr =
                if (element.isPublic()) VlangHighlightingData.VLANG_PUBLIC_FUNCTION_NAME
                else VlangHighlightingData.VLANG_FUNCTION_NAME
            holder.textAttributes(ident, attr)
        }

        if (element is VlangReferenceExpression) {
            if (element.parent is VlangCallExpr) {
                val name = element.getIdentifier().text
                if (VlangParserDefinition.typeCastFunctions.contains(name)) {
                    holder.textAttributes(element.getIdentifier(), JavaHighlightingColors.TYPE_PARAMETER_NAME_ATTRIBUTES)
                    return
                }

                if (name in arrayOf("sizeof", "__offsetof")) {
                    holder.textAttributes(element.getIdentifier(), JavaHighlightingColors.KEYWORD)
                    return
                }
            }

            val ref = element.reference
            val resolvedElement = ref.multiResolve(false).firstOrNull()?.element ?: return

            if (element.parent is VlangCallExpr) {
                if (resolvedElement is VlangInterfaceDeclaration) {
                    holder.textAttributes(element.getIdentifier(), JavaHighlightingColors.INTERFACE_NAME_ATTRIBUTES)
                    return
                }
            }

            if (resolvedElement is VlangFunctionDeclaration) {
                holder.textAttributes(element.getIdentifier(), JavaHighlightingColors.METHOD_DECLARATION_ATTRIBUTES)
                return
            }

            if (resolvedElement is VlangMethodDeclaration) {
                holder.textAttributes(element.getIdentifier(), JavaHighlightingColors.METHOD_DECLARATION_ATTRIBUTES)
                return
            }

            if (resolvedElement is VlangInterfaceMethodDefinition) {
                // TODO: separate styles?
                holder.textAttributes(element.getIdentifier(), JavaHighlightingColors.METHOD_DECLARATION_ATTRIBUTES)
                return
            }

            if (resolvedElement is VlangStructDeclaration) {
                holder.textAttributes(element.getIdentifier(), JavaHighlightingColors.CLASS_NAME_ATTRIBUTES)
                return
            }

            if (resolvedElement is VlangEnumDeclaration) {
                holder.textAttributes(element.getIdentifier(), JavaHighlightingColors.ENUM_NAME_ATTRIBUTES)
                return
            }

            if (resolvedElement is VlangUnionDeclaration) {
                holder.textAttributes(element.getIdentifier(), JavaHighlightingColors.CLASS_NAME_ATTRIBUTES)
                return
            }

            if (resolvedElement is VlangInterfaceDeclaration) {
                holder.textAttributes(element.getIdentifier(), JavaHighlightingColors.INTERFACE_NAME_ATTRIBUTES)
                return
            }

            if (resolvedElement is VlangParamDefinition) {
                holder.textAttributes(element.getIdentifier(), JavaHighlightingColors.PARAMETER_ATTRIBUTES)
                return
            }

            if (resolvedElement is VlangReceiver) {
                holder.textAttributes(element.getIdentifier(), JavaHighlightingColors.PARAMETER_ATTRIBUTES)
                return
            }

            if (resolvedElement is VlangConstDefinition) {
                holder.textAttributes(element.getIdentifier(), DefaultLanguageHighlighterColors.CONSTANT)
                return
            }

            if (resolvedElement is VlangFieldDefinition) {
                holder.textAttributes(element.getIdentifier(), DefaultLanguageHighlighterColors.INSTANCE_FIELD)
                return
            }

            if (resolvedElement is VlangEnumFieldDefinition) {
                holder.textAttributes(element.getIdentifier(), JavaHighlightingColors.INSTANCE_FIELD_ATTRIBUTES)
                return
            }
        }

        if (element is VlangType) {
            val ident = element.identifier
            if (ident != null) {
                holder.textAttributes(ident, JavaHighlightingColors.TYPE_PARAMETER_NAME_ATTRIBUTES)
            }
        }

        if (element is VlangFieldDefinition) {
            holder.textAttributes(element, JavaHighlightingColors.INSTANCE_FIELD_ATTRIBUTES)
        }

        if (element is VlangEnumFieldDefinition) {
            holder.textAttributes(element, JavaHighlightingColors.INSTANCE_FIELD_ATTRIBUTES)
        }

        if (element.elementType == VlangTypes.IDENTIFIER) {
            when (element.parent) {
                is VlangInterfaceMethodDeclaration -> holder.textAttributes(element, JavaHighlightingColors.METHOD_DECLARATION_ATTRIBUTES)
                is VlangEnumDeclaration            -> holder.textAttributes(element, JavaHighlightingColors.ENUM_NAME_ATTRIBUTES)
                is VlangStructType                 -> holder.textAttributes(element, JavaHighlightingColors.CLASS_NAME_ATTRIBUTES)
                is VlangInterfaceDeclaration       -> holder.textAttributes(element, JavaHighlightingColors.INTERFACE_NAME_ATTRIBUTES)
                is VlangEnumFieldDeclaration       -> holder.textAttributes(element, JavaHighlightingColors.STATIC_FINAL_FIELD_ATTRIBUTES)
                is VlangStatement                  -> holder.textAttributes(element, JavaHighlightingColors.INTERFACE_NAME_ATTRIBUTES)
                is VlangLabelRef                   -> holder.textAttributes(element, VlangHighlightingData.VLANG_LABEL)
                is VlangParamDefinition            -> holder.textAttributes(element, JavaHighlightingColors.PARAMETER_ATTRIBUTES)
                is VlangReceiver                   -> holder.textAttributes(element, JavaHighlightingColors.PARAMETER_ATTRIBUTES)
                is VlangConstDefinition            -> holder.textAttributes(element, DefaultLanguageHighlighterColors.CONSTANT)
                is VlangInterfaceMethodDefinition  -> holder.textAttributes(element, JavaHighlightingColors.METHOD_DECLARATION_ATTRIBUTES)
                is VlangLabelDefinition            -> {
                    val parent = element.parent
                    val search = ReferencesSearch.search(parent, parent.useScope)
                    if (search.findFirst() != null) {
                        holder.textAttributes(element, VlangHighlightingData.VLANG_LABEL)
                    }
                }
            }

            when (element.text) {
                "_likely_", "_unlikely_" ->
                    holder.textAttributes(element, JavaHighlightingColors.KEYWORD)
            }
        }

        if (element.parent is VlangPlainAttribute) {
            if (element.elementType == VlangTypes.IDENTIFIER || element.elementType == VlangTypes.UNSAFE || element.elementType == VlangTypes.SQL || element.elementType == VlangTypes.DEFAULT) {
                holder.textAttributes(element, JavaHighlightingColors.ANNOTATION_NAME_ATTRIBUTES)
            }
        }

        if (element.parent is VlangAttribute && (element.elementType == VlangTypes.LBRACK || element.elementType == VlangTypes.RBRACK)) {
            holder.textAttributes(element, JavaHighlightingColors.ANNOTATION_NAME_ATTRIBUTES)
        }

        if (element.elementType == VlangDocTokenTypes.DOC_COMMENT_TAG) {
            holder.textAttributes(element, JavaHighlightingColors.DOC_COMMENT_TAG)
        }

        if (element.elementType in listOf(
                VlangTypes.LONG_TEMPLATE_ENTRY_START,
                VlangTypes.LONG_TEMPLATE_ENTRY_END,
            )
        ) {
            holder.textAttributes(element, JavaHighlightingColors.VALID_STRING_ESCAPE)
        }

        if (element.elementType == VlangTypes.LITERAL_STRING_TEMPLATE_ESCAPE_ENTRY) {
            holder.textAttributes(element, JavaHighlightingColors.VALID_STRING_ESCAPE)
        }

        if (element is VlangLiteral && (element.text == "true" || element.text == "false")) {
            holder.textAttributes(element, JavaHighlightingColors.KEYWORD)
        }

        if (element.elementType == VlangTypes.RAW_STRING) {
            holder.textAttributes(element, TextRange(0, 1), JavaHighlightingColors.VALID_STRING_ESCAPE)
        }

        if (element.elementType == VlangTypes.SHORT_TEMPLATE_ENTRY_START) {
            if (element.parent is VlangShortStringTemplateEntry) {
                holder.textAttributes(element, JavaHighlightingColors.VALID_STRING_ESCAPE)
            } else {
                holder.textAttributes(element, JavaHighlightingColors.STRING)
            }
        }

        if (VlangCompletionUtil.isCompileTimeIdentifier(element)) {
            holder.textAttributes(element, DefaultLanguageHighlighterColors.CONSTANT)
        }
    }

    private fun AnnotationHolder.textAttributes(element: PsiElement, textAttributes: TextAttributesKey) {
        newSilentAnnotation(HighlightSeverity.INFORMATION).range(element).textAttributes(textAttributes).create()
    }

    private fun AnnotationHolder.textAttributes(element: PsiElement, range: TextRange, textAttributes: TextAttributesKey) {
        val newRange = TextRange(element.textRange.startOffset + range.startOffset, element.textRange.startOffset + range.endOffset)
        newSilentAnnotation(HighlightSeverity.INFORMATION).range(newRange).textAttributes(textAttributes).create()
    }
}
