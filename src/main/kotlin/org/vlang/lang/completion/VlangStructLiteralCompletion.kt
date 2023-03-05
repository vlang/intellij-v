package org.vlang.lang.completion

import com.intellij.psi.PsiElement
import org.jetbrains.annotations.Contract
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangPsiImplUtil
import org.vlang.lang.psi.types.VlangArrayTypeEx
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangChannelTypeEx
import org.vlang.utils.parentNth

internal object VlangStructLiteralCompletion {
    fun allowedVariants(structFieldReference: VlangReferenceExpression?, refElement: PsiElement): Variants {
        if (structFieldReference == null) {
            return Variants.NONE
        }

        if (VlangPsiImplUtil.prevDot(refElement)) {
            return Variants.NONE
        }

        var value = structFieldReference.parent
        while (value is VlangUnaryExpr) {
            value = value.parent
        }
        if (value !is VlangValue) {
            return Variants.NONE
        }

        val element = parent<VlangElement>(value)
        if (element?.key != null) {
            return Variants.NONE
        }

        val possiblyLiteralValueExpression = structFieldReference.parentNth<VlangLiteralValueExpression>(3)
        if (possiblyLiteralValueExpression != null) {
            val type = possiblyLiteralValueExpression.getType(null)
            if (type is VlangArrayTypeEx) {
                // for []int{<caret>}, allow only fields
                return Variants.FIELD_NAME_ONLY
            }
            if (type is VlangChannelTypeEx) {
                // for chan int{<caret>}, allow only fields
                return Variants.FIELD_NAME_ONLY
            }
        }

        var hasValueInitializers = false
        var hasFieldValueInitializers = false

        val fieldInitializers: List<VlangElement> = getFieldInitializers(structFieldReference) ?: return Variants.NONE

        for (initializer in fieldInitializers) {
            if (initializer === element) {
                continue
            }
            val colon = initializer.colon
            hasFieldValueInitializers = hasFieldValueInitializers or (colon != null)
            hasValueInitializers = hasValueInitializers or (colon == null)
        }
        return if (hasFieldValueInitializers && !hasValueInitializers)
            Variants.FIELD_NAME_ONLY
        else if (!hasFieldValueInitializers && hasValueInitializers)
            Variants.VALUE_ONLY
        else
            Variants.BOTH
    }

    private fun getFieldInitializers(element: PsiElement): List<VlangElement>? {
        val literalValue = element.parentNth<VlangLiteralValueExpression>(3)
        if (literalValue == null) {
            val callExpr = VlangCodeInsightUtil.getCallExpr(element)
            val resolved = callExpr?.resolve() as? VlangSignatureOwner
            val params = resolved?.getSignature()?.parameters?.paramDefinitionList
            val paramTypes = params?.map { it.type.toEx() }

            if (paramTypes != null) {
                if (!VlangCodeInsightUtil.isAllowedParamsForTrailingStruct(params, paramTypes)) return null

                val startIndex = paramTypes.lastIndex

                val list = callExpr.argumentList.elementList
                if (startIndex == -1 || startIndex >= list.size) return emptyList()

                return list.subList(startIndex, list.size)
            }

            return emptyList()
        }

        return literalValue.elementList
    }

    fun alreadyAssignedFields(elements: List<VlangElement>): Set<String> {
        return elements.mapNotNull {
            val identifier = it.key?.fieldName?.getIdentifier()
            identifier?.text
        }.toSet()
    }

    @Contract("null,_->null")
    private inline fun <reified T> parent(of: PsiElement?): T? {
        val parent = of?.parent
        return if (parent is T) parent else null
    }

    /**
     * Describes struct literal completion variants that should be suggested.
     */
    internal enum class Variants {
        /**
         * Only struct field names should be suggested.
         * Indicates that field:value initializers are used in this struct literal.
         * For example, `Struct{field1: "", caret}`.
         */
        FIELD_NAME_ONLY,

        /**
         * Only values should be suggested.
         * Indicates that value initializers are used in this struct literal.
         * For example, `Struct{"", caret}`.
         */
        VALUE_ONLY,

        /**
         * Both struct field names and values should be suggested.
         * Indicates that there's no reliable way to determine whether field:value or value initializers are used.
         * Example 1: `Struct{caret}`.
         * Example 2: `Struct{field1:"", "", caret}`
         */
        BOTH,

        /**
         * Indicates that struct literal completion should not be available.
         */
        NONE
    }
}
