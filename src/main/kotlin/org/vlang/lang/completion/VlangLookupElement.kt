package org.vlang.lang.completion

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementDecorator

class VlangLookupElement(
    delegate: LookupElement,
    val props: VlangLookupElementProperties,
) : LookupElementDecorator<LookupElement>(delegate) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as VlangLookupElement

        return props == other.props
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + props.hashCode()
        return result
    }
}

data class VlangLookupElementProperties(
    val isLocal: Boolean = false,
    val isSameModule: Boolean = false,
    val isReceiverTypeCompatible: Boolean = false,
    val isTypeCompatible: Boolean = false,
    val isContextElement: Boolean = false,
    val isNotDeprecated: Boolean = true,
    val isContextMember: Boolean = false,
    val elementKind: ElementKind = ElementKind.OTHER,
) {
    enum class ElementKind {
        // Top Priority
        FIELD,
        METHOD,
        FUNCTION,
        STRUCT,
        INTERFACE,
        ENUM,
        CONSTANT,
        TYPE_ALIAS,
        INTERFACE_METHOD,
        ENUM_FIELD,
        IMPORT_ALIAS,
        GLOBAL,
        OTHER,
        // Least priority
    }
}
