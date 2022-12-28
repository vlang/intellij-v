package org.vlang.lang.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import org.vlang.ide.colors.VlangColor
import org.vlang.lang.VlangTypes
import org.vlang.lang.completion.VlangCompletionUtil
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangModule
import org.vlang.lang.psi.impl.VlangReference
import org.vlang.lang.psi.types.VlangPrimitiveTypes
import org.vlang.lang.sql.VlangSqlUtil
import org.vlang.utils.inside
import org.vlang.utils.parentNth

class VlangAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (holder.isBatchMode) return

        val color = when (element) {
            is LeafPsiElement           -> highlightLeaf(element, holder)
            is VlangSqlBlock            -> VlangColor.SQL_CODE
            is VlangUnsafeExpression    -> VlangColor.UNSAFE_CODE
            is VlangAttributeIdentifier -> VlangColor.ATTRIBUTE
            else                        -> null
        } ?: return

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION).textAttributes(color.textAttributesKey).create()
    }

    private fun highlightAttribute(element: PsiElement): VlangColor? {
        if (element.parent is VlangPlainAttribute) {
            if (element.elementType == VlangTypes.IDENTIFIER || element.elementType == VlangTypes.UNSAFE) {
                return VlangColor.ATTRIBUTE
            }
        }

        if (element.parent is VlangAttribute && (element.elementType == VlangTypes.LBRACK || element.elementType == VlangTypes.RBRACK)) {
            return VlangColor.ATTRIBUTE
        }

        return null
    }

    private fun highlightLeaf(element: PsiElement, holder: AnnotationHolder): VlangColor? {
        val parent = element.parent as? VlangCompositeElement ?: return null

        if (VlangCompletionUtil.isCompileTimeMethodIdentifier(element) && parent.parentOfType<VlangCallExpr>() != null) {
            return VlangColor.CT_METHOD_CALL
        }

        if (VlangPrimitiveTypes.isPrimitiveType(element.text) &&
            (element.parentNth<VlangCallExpr>(2) != null || element.parent is VlangTypeReferenceExpression) &&
            element.parent !is VlangAliasType
        ) {
            // highlight only u8(100) call, not a.u8()
            val call = element.parentNth<VlangCallExpr>(2)
            val callExpr = call?.expression as? VlangReferenceExpression
            if (callExpr?.getQualifier() == null) {
                return VlangColor.BUILTIN_TYPE
            }
        }

        if ((element.text == "chan" || element.text == "thread") && element.inside<VlangType>()) {
            return VlangColor.KEYWORD
        }

        if (parent is VlangGenericParameter) {
            return VlangColor.GENERIC_PARAMETER
        }

        if (element.elementType == VlangTypes.IDENTIFIER && parent is VlangReferenceExpressionBase) {
            return highlightReference(parent, parent.reference as VlangReference)
        }

        if (parent is VlangAttribute || parent is VlangAttributeIdentifier) {
            return highlightAttribute(element)
        }

        if (VlangSqlUtil.insideSql(element) && VlangSqlUtil.isSqlKeyword(element.text)) {
            return VlangColor.SQL_KEYWORD
        }

        return when (element.elementType) {
            VlangTypes.IDENTIFIER -> highlightIdentifier(element, parent)
            else                  -> null
        }
    }

    private fun highlightReference(element: VlangReferenceExpressionBase, reference: VlangReference): VlangColor? {
        val resolved = reference.multiResolve(false).firstOrNull()?.element ?: return null

        return when (resolved) {
            is VlangFunctionDeclaration       -> public(resolved, VlangColor.PUBLIC_FUNCTION, VlangColor.FUNCTION)
            is VlangMethodDeclaration         -> public(resolved, VlangColor.PUBLIC_FUNCTION, VlangColor.FUNCTION)
            is VlangInterfaceMethodDefinition -> public(resolved, VlangColor.INTERFACE_METHOD, VlangColor.INTERFACE_METHOD)
            is VlangStructDeclaration         -> if (!resolved.isUnion)
                public(resolved, VlangColor.PUBLIC_STRUCT, VlangColor.STRUCT)
            else
                public(resolved, VlangColor.PUBLIC_UNION, VlangColor.UNION)

            is VlangEnumDeclaration           -> public(resolved, VlangColor.PUBLIC_ENUM, VlangColor.ENUM)
            is VlangInterfaceDeclaration      -> public(resolved, VlangColor.PUBLIC_INTERFACE, VlangColor.INTERFACE)
            is VlangFieldDefinition           -> public(resolved, VlangColor.PUBLIC_FIELD, VlangColor.FIELD)
            is VlangEnumFieldDefinition       -> public(resolved, VlangColor.ENUM_FIELD, VlangColor.ENUM_FIELD)
            is VlangTypeAliasDeclaration      -> public(resolved, VlangColor.PUBLIC_TYPE_ALIAS, VlangColor.TYPE_ALIAS)
            is VlangParamDefinition           -> mutable(resolved, VlangColor.MUTABLE_PARAMETER, VlangColor.PARAMETER)
            is VlangReceiver                  -> mutable(resolved, VlangColor.MUTABLE_RECEIVER, VlangColor.RECEIVER)
            is VlangGlobalVariableDefinition  -> VlangColor.GLOBAL_VARIABLE
            is VlangVarDefinition             -> if (resolved.isCaptured(element)) {
                mutable(resolved, VlangColor.MUTABLE_CAPTURED_VARIABLE, VlangColor.CAPTURED_VARIABLE)
            } else {
                mutable(resolved, VlangColor.MUTABLE_VARIABLE, VlangColor.VARIABLE)
            }

            is VlangGenericParameter                -> VlangColor.GENERIC_PARAMETER
            is VlangModule.VlangPomTargetPsiElement -> VlangColor.MODULE
            is VlangImportAlias                     -> VlangColor.MODULE
            is VlangConstDefinition           -> {
                val identifier = element.getIdentifier()
                if (identifier != null && VlangCompletionUtil.isCompileTimeIdentifier(identifier)) {
                    return VlangColor.CT_CONSTANT
                }

                public(resolved, VlangColor.PUBLIC_CONSTANT, VlangColor.CONSTANT)
            }
            else                              -> null
        }
    }

    private fun highlightIdentifier(element: PsiElement, parent: VlangCompositeElement): VlangColor? {
        val grand = parent.parent
        if (grand is VlangCompositeElement && grand is PsiNameIdentifierOwner && grand.nameIdentifier == element) {
            return colorFor(grand)
        }

        return when {
            parent is VlangMethodName && parent.identifier == element            -> colorFor(parent.parent as VlangMethodDeclaration)
            parent is PsiNameIdentifierOwner && parent.nameIdentifier == element -> colorFor(parent)

            // pseudo keywords
            element.text in listOf(
                "_likely_",
                "_unlikely_",
                "sql",
                "map",
            )                                                                    -> VlangColor.KEYWORD

            else                                                                 -> null
        }
    }

    private fun colorFor(element: VlangCompositeElement): VlangColor? = when (element) {
        is VlangFunctionOrMethodDeclaration -> public(element, VlangColor.PUBLIC_FUNCTION, VlangColor.FUNCTION)
        is VlangConstDefinition             -> public(element, VlangColor.PUBLIC_CONSTANT, VlangColor.CONSTANT)
        is VlangTypeAliasDeclaration        -> public(element, VlangColor.PUBLIC_TYPE_ALIAS, VlangColor.TYPE_ALIAS)
        is VlangStructDeclaration           -> if (!element.isUnion)
            public(element, VlangColor.PUBLIC_STRUCT, VlangColor.STRUCT)
        else
            public(element, VlangColor.PUBLIC_UNION, VlangColor.UNION)

        is VlangInterfaceDeclaration        -> public(element, VlangColor.PUBLIC_INTERFACE, VlangColor.INTERFACE)
        is VlangEnumDeclaration             -> public(element, VlangColor.PUBLIC_ENUM, VlangColor.ENUM)

        is VlangFieldDefinition             -> public(element, VlangColor.PUBLIC_FIELD, VlangColor.FIELD)
        is VlangInterfaceMethodDefinition   -> public(element, VlangColor.INTERFACE_METHOD, VlangColor.INTERFACE_METHOD)
        is VlangEnumFieldDefinition         -> public(element, VlangColor.ENUM_FIELD, VlangColor.ENUM_FIELD)

        is VlangVarDefinition               -> mutable(element, VlangColor.MUTABLE_VARIABLE, VlangColor.VARIABLE)
        is VlangReceiver                    -> mutable(element, VlangColor.MUTABLE_RECEIVER, VlangColor.RECEIVER)
        is VlangParamDefinition             -> mutable(element, VlangColor.MUTABLE_PARAMETER, VlangColor.PARAMETER)
        is VlangGlobalVariableDefinition    -> VlangColor.GLOBAL_VARIABLE

        is VlangLabelDefinition             -> label(element)
        is VlangLabelRef                    -> VlangColor.LABEL

        else                                -> null
    }

    private fun label(element: VlangLabelDefinition): VlangColor {
        val parent = element.parent
        val search = ReferencesSearch.search(parent, parent.useScope)
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
