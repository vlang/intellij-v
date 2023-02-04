package org.vlang.ide.codeInsight

import org.vlang.lang.psi.*

object VlangDeprecationsUtil {
    data class DeprecationInfo(val element: VlangNamedElement, val message: String?, val after: String?) {
        fun generateMessage(): String {
            var msg = "'${element.name}' is deprecated"
            if (message != null) {
                msg += ": $message"
            }
            if (after != null) {
                msg += "<br>'${element.name}' will be deprecated after $after and removed after six months"
            }

            return msg
        }
    }

    fun getDeprecationInfo(element: VlangNamedElement): DeprecationInfo? {
        if (element !is VlangAttributeOwner) return null

        val attributes = element.attributes?.attributeList ?: return null
        return deprecationInfo(attributes, element)
    }

    fun getDeprecationInfo(file: VlangFile): DeprecationInfo? {
        val attributes = file.getFileAttributes()
        val element = file.getModule() ?: return null
        return deprecationInfo(attributes, element)
    }

    private fun deprecationInfo(
        attributes: List<VlangAttribute>,
        element: VlangNamedElement,
    ): DeprecationInfo? {
        val attr = VlangAttributesUtil.getAttributeByName(attributes, "deprecated") ?: return null
        val message = VlangAttributesUtil.getAttributeValue(attr)

        val deprecatedAfterAttr = VlangAttributesUtil.getAttributeByName(attributes, "deprecated_after")
        val deprecatedAfter = deprecatedAfterAttr?.let { VlangAttributesUtil.getAttributeValue(it) }

        return DeprecationInfo(element, message, deprecatedAfter)
    }

    fun isDeprecated(element: VlangNamedElement): Boolean {
        return getDeprecationInfo(element) != null
    }
}
