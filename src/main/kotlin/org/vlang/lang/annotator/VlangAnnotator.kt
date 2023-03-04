package org.vlang.lang.annotator

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.elementType
import org.vlang.ide.codeInsight.VlangDeprecationsUtil
import org.vlang.ide.colors.VlangColor
import org.vlang.lang.VlangTypes
import org.vlang.lang.completion.VlangCompletionUtil
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangModule
import org.vlang.lang.psi.impl.VlangReference
import org.vlang.lang.psi.types.VlangPrimitiveTypes

class VlangAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (holder.isBatchMode) return

        val color = highlightLeaf(element, holder) ?: return
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION).textAttributes(color.textAttributesKey).create()
    }

    private fun highlightLeaf(element: PsiElement, holder: AnnotationHolder): VlangColor? {
        val parent = element.parent as? VlangCompositeElement ?: return null

        if (element.elementType == VlangTypes.IDENTIFIER && parent is VlangReferenceExpressionBase) {
            return highlightReference(parent, parent.reference as VlangReference, holder)
        }

        return when (element.elementType) {
            VlangTypes.IDENTIFIER -> highlightIdentifier(element, parent)
            else                  -> null
        }
    }

    private fun highlightReference(
        element: VlangReferenceExpressionBase,
        reference: VlangReference,
        holder: AnnotationHolder,
    ): VlangColor? {
        if (VlangPrimitiveTypes.isPrimitiveType(element.text)) {
            return null
        }

        val resolved = reference.multiResolve(false).firstOrNull()?.element ?: return null

        if (resolved is VlangNamedElement && VlangDeprecationsUtil.isDeprecated(resolved)) {
            val info = VlangDeprecationsUtil.getDeprecationInfo(resolved)
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .highlightType(ProblemHighlightType.LIKE_DEPRECATED)
                .tooltip(info!!.generateMessage())
                .create()
        }

        return when (resolved) {
            is VlangFunctionDeclaration             -> public(resolved, VlangColor.PUBLIC_FUNCTION, VlangColor.FUNCTION)
            is VlangMethodDeclaration               -> public(resolved, VlangColor.PUBLIC_FUNCTION, VlangColor.FUNCTION)
            is VlangInterfaceMethodDefinition       -> public(resolved, VlangColor.INTERFACE_METHOD, VlangColor.INTERFACE_METHOD)
            is VlangStructDeclaration               -> if (!resolved.isUnion)
                public(resolved, VlangColor.PUBLIC_STRUCT, VlangColor.STRUCT)
            else
                public(resolved, VlangColor.PUBLIC_UNION, VlangColor.UNION)

            is VlangEnumDeclaration                 -> public(resolved, VlangColor.PUBLIC_ENUM, VlangColor.ENUM)
            is VlangInterfaceDeclaration            -> public(resolved, VlangColor.PUBLIC_INTERFACE, VlangColor.INTERFACE)
            is VlangEmbeddedDefinition              -> VlangColor.STRUCT
            is VlangFieldDefinition                 -> public(resolved, VlangColor.PUBLIC_FIELD, VlangColor.FIELD)
            is VlangEnumFieldDefinition             -> public(resolved, VlangColor.ENUM_FIELD, VlangColor.ENUM_FIELD)
            is VlangTypeAliasDeclaration            -> public(resolved, VlangColor.PUBLIC_TYPE_ALIAS, VlangColor.TYPE_ALIAS)
            is VlangReceiver                        -> mutable(resolved, VlangColor.MUTABLE_RECEIVER, VlangColor.RECEIVER)
            is VlangGlobalVariableDefinition        -> VlangColor.GLOBAL_VARIABLE

            is VlangParamDefinition                 ->
                if (resolved.isCaptured(element)) {
                    mutable(resolved, VlangColor.MUTABLE_CAPTURED_VARIABLE, VlangColor.CAPTURED_VARIABLE)
                } else {
                    mutable(resolved, VlangColor.MUTABLE_PARAMETER, VlangColor.PARAMETER)
                }

            is VlangVarDefinition                   -> if (resolved.isCaptured(element)) {
                mutable(resolved, VlangColor.MUTABLE_CAPTURED_VARIABLE, VlangColor.CAPTURED_VARIABLE)
            } else {
                mutable(resolved, VlangColor.MUTABLE_VARIABLE, VlangColor.VARIABLE)
            }

            is VlangGenericParameter                -> VlangColor.GENERIC_PARAMETER
            is VlangModule.VlangPomTargetPsiElement -> VlangColor.MODULE
            is VlangImportAlias                     -> VlangColor.MODULE
            is VlangConstDefinition                 -> {
                val identifier = element.getIdentifier()
                if (identifier != null && VlangCompletionUtil.isCompileTimeIdentifier(identifier)) {
                    return VlangColor.CT_CONSTANT
                }

                public(resolved, VlangColor.PUBLIC_CONSTANT, VlangColor.CONSTANT)
            }

            else                                    -> null
        }
    }

    private fun highlightIdentifier(element: PsiElement, parent: VlangCompositeElement): VlangColor? {
        if (parent is VlangLabelDefinition) {
            return label(parent)
        }

        return null
    }

    private fun label(element: VlangLabelDefinition): VlangColor {
        val search = ReferencesSearch.search(element, element.useScope)
        return if (search.findFirst() != null) {
            VlangColor.USED_LABEL
        } else {
            VlangColor.LABEL
        }
    }

    private fun public(element: VlangNamedElement, ifPublic: VlangColor, ifNotPublic: VlangColor): VlangColor {
        return if (element.isPublic()) ifPublic else ifNotPublic
    }

    private fun mutable(element: VlangMutabilityOwner, ifMutable: VlangColor, ifNoMutable: VlangColor): VlangColor {
        return if (element.isMutable()) ifMutable else ifNoMutable
    }
}
