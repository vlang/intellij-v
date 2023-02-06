package org.vlang.ide.codeInsight

import com.intellij.psi.util.childrenOfType
import org.vlang.lang.psi.*

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

    fun getAttributeByName(attributes: List<VlangAttribute>, name: String): VlangAttributeExpression? {
        attributes.forEach {
            it.attributeExpressionList.forEach exprs@{ expr ->
                val plain = expr.plainAttribute ?: return@exprs
                val key = plain.attributeKey
                if (key.text == name) {
                    return expr
                }
            }
        }

        return null
    }

    fun getAttributeValue(expr: VlangAttributeExpression): String? {
        val plain = expr.plainAttribute ?: return null
        val value = plain.attributeValue ?: return null
        val literal = value.childrenOfType<VlangStringLiteral>()
            .firstOrNull() ?: return null
        return literal.text.trim('"').trim('\'')
    }
}
