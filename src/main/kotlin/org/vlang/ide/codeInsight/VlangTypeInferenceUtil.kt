package org.vlang.ide.codeInsight

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangBuiltinArrayTypeEx
import org.vlang.lang.psi.types.VlangPointerTypeEx
import org.vlang.lang.psi.types.VlangTypeEx
import org.vlang.utils.parentNth

object VlangTypeInferenceUtil {
    const val ARRAY_MAP_METHOD = "map"

    fun callerType(call: VlangCallExpr): VlangTypeEx? {
        val callExpression = call.expression as? VlangReferenceExpression ?: return null
        val caller = callExpression.getQualifier() as? VlangTypeOwner ?: return null
        return caller.getType(null)
    }

    fun builtinArrayOrPointerTo(type: VlangTypeEx): Boolean {
        if (type is VlangBuiltinArrayTypeEx) return true
        return type is VlangPointerTypeEx && type.inner is VlangBuiltinArrayTypeEx
    }

    fun getContextType(element: PsiElement): VlangTypeEx? {
        if (element.parent is VlangValue) {
            val parentElement = element.parentNth<VlangElement>(2)
            val key = parentElement?.key
            if (key?.fieldName != null) {
                val resolved = key.fieldName?.resolve() as? VlangFieldDefinition
                val declaration = resolved?.parent as? VlangFieldDeclaration
                val resolvedType = declaration?.type
                if (resolvedType != null) {
                    return resolvedType.toEx()
                }
            }
        }

        val callExpr = VlangCodeInsightUtil.getCallExpr(element)
        if (callExpr != null) {
            val index = callExpr.paramIndexOf(element)
            if (index == -1) return null

            val function = callExpr.resolve() as? VlangFunctionOrMethodDeclaration ?: return null
            val params = function.getSignature()?.parameters?.paramDefinitionList ?: return null

            val param = params.getOrNull(index) ?: return null
            return param.type.toEx()
        }

        if (element.parent is VlangDefaultFieldValue) {
            val fieldDeclaration = element.parentOfType<VlangFieldDeclaration>() ?: return null
            return fieldDeclaration.type.toEx()
        }

        if (element.parent is VlangBinaryExpr) {
            val binaryExpr = element.parent as VlangBinaryExpr
            if (binaryExpr.right != null && binaryExpr.right!!.isEquivalentTo(element)) {
                val left = binaryExpr.left
                return left.getType(null)
            }
        }

        if (element.parent is VlangAssignmentStatement) {
            val assign = element.parent as VlangAssignmentStatement
            // TODO: support multi assign
            val assignExpression = assign.leftHandExprList.expressionList.firstOrNull() as? VlangReferenceExpression ?: return null
            return assignExpression.getType(null)
        }

        if (element.parent is VlangMatchArm) {
            val parentMatch = element.parentOfType<VlangMatchExpression>()
            if (parentMatch != null) {
                val matchExpression = parentMatch.expression as? VlangReferenceExpression ?: return null
                return matchExpression.getType(null)
            }
        }

        return null
    }
}
