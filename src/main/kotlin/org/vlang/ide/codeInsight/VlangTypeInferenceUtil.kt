package org.vlang.ide.codeInsight

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangPsiImplUtil
import org.vlang.lang.psi.types.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.unwrapGenericInstantiation
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.unwrapPointer
import org.vlang.utils.inside
import org.vlang.utils.parentNth

object VlangTypeInferenceUtil {
    const val ARRAY_MAP_METHOD = "map"

    fun callerType(call: VlangCallExpr): VlangTypeEx? {
        val callExpression = call.expression as? VlangReferenceExpression ?: return null
        val caller = callExpression.getQualifier() as? VlangTypeOwner ?: return null
        val type = caller.getType(null)

        val nextLeaf = PsiTreeUtil.nextCodeLeaf(caller)
        val needUnwrap = nextLeaf.elementType == VlangTypes.SAFE_DOT

        return VlangPsiImplUtil.unwrapOptionOrResultTypeIf(type, needUnwrap)
    }

    fun stubThread(type: VlangTypeEx): Boolean {
        val inner = type.unwrapGenericInstantiation()
        return inner is VlangStructTypeEx && inner.qualifiedName() == "stubs.Thread"
    }

    fun stubThreadPool(type: VlangTypeEx): Boolean {
        val inner = type.unwrapGenericInstantiation()
        return inner is VlangStructTypeEx && inner.qualifiedName() == "stubs.ThreadPool"
    }

    fun builtinArrayOrPointerTo(type: VlangTypeEx): Boolean {
        if (type is VlangBuiltinArrayTypeEx) return true
        return type is VlangPointerTypeEx && type.inner is VlangBuiltinArrayTypeEx
    }

    fun builtinMapOrPointerTo(type: VlangTypeEx): Boolean {
        if (type is VlangBuiltinMapTypeEx) return true
        return type is VlangPointerTypeEx && type.inner is VlangBuiltinMapTypeEx
    }

    fun getContextType(element: PsiElement): VlangTypeEx? {
        if (element is VlangArrayCreation) {
            val firstElement = element.arrayCreationList?.expressionList?.firstOrNull()
            if (firstElement != null && firstElement !is VlangEnumFetch) {
                return firstElement.getType(null)
            }
        }

        val parentStructInit = element.parentNth<VlangLiteralValueExpression>(3)
        if (parentStructInit != null) {
            val withoutKeys = parentStructInit.elementList.any { it.key == null }
            if (withoutKeys) {
                val elem = parentStructInit.elementList.firstOrNull { PsiTreeUtil.isAncestor(it.value, element, false) }
                val key = elem?.key
                val fieldName = key?.fieldName?.text ?: return null
                val struct = parentStructInit.getType(null) as? VlangStructTypeEx ?: return null
                val structDecl = struct.resolve(element.project) ?: return null
                val structType = structDecl.structType
                return structType.fieldList.firstOrNull { it.name == fieldName }?.getType(null)
            }
        }

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

        if (element.parent is VlangAddExpr) {
            val addExpr = element.parent as VlangAddExpr
            if (addExpr.bitOr != null) {
                return getContextType(addExpr)
            }
        }

        if (element.parent is VlangBinaryExpr) {
            val binaryExpr = element.parent as VlangBinaryExpr
            val right = binaryExpr.right ?: return null
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

            if (function is VlangMethodDeclaration) {
                val receiverType = function.receiverType.toEx().unwrapPointer()
                if (receiverType is VlangEnumTypeEx && receiverType.name == VlangEnumTypeEx.FLAG_ENUM.name) {
                    return callerType(callExpr)
                }
            }

            val param = params.getOrNull(index) ?: return null
            val type = param.type.toEx()

            if (type is VlangMapTypeEx && element.inside<VlangMapInitExpr>()) {
                val keyValue = element.parentOfType<VlangKeyValue>()
                if (PsiTreeUtil.isAncestor(keyValue?.keyExpr, element, false)) {
                    return type.key
                }
                if (PsiTreeUtil.isAncestor(keyValue?.valueExpr, element, false)) {
                    return type.value
                }
                return type
            }

            return type
        }

        if (element.inside<VlangReturnStatement>()) {
            val function = element.parentOfType<VlangFunctionOrMethodDeclaration>() ?: return null
            return function.getSignature()?.result?.type?.toEx()
        }

        return null
    }
}
