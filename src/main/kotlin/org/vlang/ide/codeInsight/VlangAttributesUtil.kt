package org.vlang.ide.codeInsight

import org.vlang.lang.psi.VlangAttribute
import org.vlang.lang.psi.VlangFunctionOrMethodDeclaration
import org.vlang.lang.psi.VlangStructDeclaration

object VlangAttributesUtil {
    fun isTranslated(attribute: VlangAttribute): Boolean {
        val expr = attribute.attributeExpressionList.firstOrNull() ?: return false
        return expr.text == "translated"
    }

    fun isNoReturn(attribute: VlangAttribute): Boolean {
        val expr = attribute.attributeExpressionList.firstOrNull() ?: return false
        return expr.text == "noreturn"
    }

    fun isMinifiedStruct(struct: VlangStructDeclaration): Boolean {
        return struct.attributes?.attributeList?.any {
            it.attributeExpressionList.any { expr -> expr.textMatches("minify") }
        } ?: false
    }

    fun isParamsStruct(struct: VlangStructDeclaration): Boolean {
        return struct.attributes?.attributeList?.any {
            it.attributeExpressionList.any { expr -> expr.textMatches("params") }
        } ?: false
    }

    fun isUnsafeFunction(function: VlangFunctionOrMethodDeclaration): Boolean {
        return function.attributes?.attributeList?.any {
            it.attributeExpressionList.any { expr -> expr.textMatches("unsafe") }
        } ?: false
    }

    fun isAttributeStruct(attribute: VlangAttribute): Boolean {
        val expr = attribute.attributeExpressionList.firstOrNull() ?: return false
        return expr.text == "attribute"
    }

    fun isFlagEnum(attribute: VlangAttribute): Boolean {
        val expr = attribute.attributeExpressionList.firstOrNull() ?: return false
        return expr.text == "flag"
    }

    fun isPrimaryField(attribute: VlangAttribute): Boolean {
        val expr = attribute.attributeExpressionList.firstOrNull() ?: return false
        return expr.text == "primary"
    }
}
