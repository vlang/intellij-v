package org.vlang.ide.codeInsight

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.*
import org.vlang.lang.psi.types.VlangArrayTypeEx
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangBuiltinArrayTypeEx
import org.vlang.lang.psi.types.VlangPointerTypeEx
import org.vlang.lang.psi.types.VlangTypeEx
import org.vlang.utils.inside
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
//        if (element is VlangArrayCreation) {
//            val type = element.arrayCreationList?.expressionList?.firstOrNull()?.getType(null)
//            if (type != null) {
//                return type
//            }
//        }

        if (element.parent is VlangValue) {
            val parentElement = element.parentNth<VlangElement>(2)
            val key = parentElement?.key
            if (key?.fieldName != null) {
                val resolved = key.fieldName?.resolve() as? VlangFieldDefinition
                val declaration = resolved?.parent as? VlangFieldDeclaration
                val resolvedType = declaration?.type?.toEx()
                if (resolvedType != null) {
                    if (element is VlangArrayCreation && resolvedType is VlangArrayTypeEx) {
                        return resolvedType.inner
                    }

                    return resolvedType
                }
            }
        }

        if (element.parent is VlangDefaultFieldValue) {
            val fieldDeclaration = element.parentOfType<VlangFieldDeclaration>() ?: return null
            return fieldDeclaration.type.toEx()
        }

        if (element.parent is VlangArrayCreationList) {
            return getContextType(element.parent.parent)
        }

        if (element.parent is VlangBinaryExpr) {
            val binaryExpr = element.parent as VlangBinaryExpr
            val right = binaryExpr.right!!
            if (binaryExpr.right != null && right.isEquivalentTo(element)) {
                val left = binaryExpr.left
                val leftType = left.getType(null)
                if (leftType is VlangArrayTypeEx) {
                    return leftType.inner
                }
                return leftType
            }
        }

        if (element.parent is VlangAssignmentStatement) {
            val assign = element.parent as VlangAssignmentStatement
            // TODO: support multi assign
            val assignExpression = assign.leftHandExprList.expressionList.firstOrNull() as? VlangTypeOwner ?: return null
            return assignExpression.getType(null)
        }

        if (element.parent is VlangMatchArm) {
            val parentMatch = element.parentOfType<VlangMatchExpression>()
            if (parentMatch != null) {
                val matchExpression = parentMatch.expression as? VlangTypeOwner ?: return null
                return matchExpression.getType(null)
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

        if (element.inside<VlangReturnStatement>()) {
            val function = element.parentOfType<VlangFunctionOrMethodDeclaration>() ?: return null
            return function.getSignature()?.result?.type?.toEx()
        }

        return null
    }
}
