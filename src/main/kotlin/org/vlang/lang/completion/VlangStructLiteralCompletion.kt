package org.vlang.lang.completion

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import org.jetbrains.annotations.Contract
import org.vlang.lang.psi.VlangElement
import org.vlang.lang.psi.VlangLiteralValueExpression
import org.vlang.lang.psi.VlangReferenceExpression
import org.vlang.lang.psi.VlangValue

internal object VlangStructLiteralCompletion {
    fun allowedVariants(structFieldReference: VlangReferenceExpression?): Variants {
        val value = parent<VlangValue>(structFieldReference)
        val element = parent<VlangElement>(value)
        if (element?.key != null) {
            return Variants.NONE
        }

        val parentLiteralType = element?.parentOfType<VlangLiteralValueExpression>()
        val type = parentLiteralType?.getType(null) ?: return Variants.NONE

        var hasValueInitializers = false
        var hasFieldValueInitializers = false

        val literalValue = parent<VlangLiteralValueExpression>(element)
        val fieldInitializers: List<VlangElement> = literalValue?.elementList ?: emptyList()

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

    fun alreadyAssignedFields(literal: VlangLiteralValueExpression?): Set<String> {
        if (literal == null) {
            return emptySet()
        }

        return literal.elementList.mapNotNull {
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
