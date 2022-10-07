package org.vlang.ide.codeInsight

import org.vlang.lang.psi.VlangAttribute

object VlangAttributesUtil {
    fun isTranslated(attribute: VlangAttribute): Boolean {
        val expr = attribute.attributeExpressionList.firstOrNull() ?: return false
        return expr.text == "translated"
    }
}
