package org.vlang.ide.codeInsight

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.*
import org.vlang.lang.psi.types.VlangBuiltinArrayTypeEx
import org.vlang.lang.psi.types.VlangPointerTypeEx
import org.vlang.lang.psi.types.VlangTypeEx
import org.vlang.utils.parentNth

object VlangTypeInferenceUtil {
    const val ARRAY_MAP_METHOD = "map"

    fun callerType(call: VlangCallExpr): VlangType? {
        val callExpression = call.expression as? VlangReferenceExpression ?: return null
        val caller = callExpression.getQualifier() as? VlangReferenceExpression ?: return null
        return caller.getType(null)
    }

    fun builtInArrayOrPointerTo(type: VlangTypeEx<*>): Boolean {
        if (type is VlangBuiltinArrayTypeEx) return true
        return type is VlangPointerTypeEx && type.inner is VlangBuiltinArrayTypeEx
    }

    fun getContextType(element: PsiElement): VlangType? {
        val callExpr = VlangCodeInsightUtil.getCallExpr(element)
        if (callExpr != null) {
            val index = callExpr.paramIndexOf(element)
            if (index == -1) return null

            val function = callExpr.resolve() as? VlangFunctionOrMethodDeclaration ?: return null
            val params = function.getSignature()?.parameters?.parametersListWithTypes ?: return null

            val param = params.getOrNull(index) ?: return null
            return param.second?.resolveType() ?: return null
        }

        if (element.parent is VlangValue) {
            val literalValueElement = element.parentNth<VlangElement>(2) ?: return null
            val key = literalValueElement.key ?: return null
            val resolved = key.fieldName?.resolve() as? VlangFieldDefinition ?: return null
            val declaration = resolved.parent as? VlangFieldDeclaration ?: return null
            return declaration.type
        }

        if (element.parent is VlangDefaultFieldValue) {
            val fieldDeclaration = element.parentOfType<VlangFieldDeclaration>() ?: return null
            return fieldDeclaration.type
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
