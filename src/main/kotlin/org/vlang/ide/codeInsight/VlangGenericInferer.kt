package org.vlang.ide.codeInsight

import com.intellij.psi.PsiElement
import org.vlang.lang.psi.*
import org.vlang.lang.psi.types.VlangAliasTypeEx
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangGenericInstantiationEx
import org.vlang.lang.psi.types.VlangPointerTypeEx
import org.vlang.lang.psi.types.VlangTypeEx

object VlangGenericInferer {
    fun inferGenericCall(call: VlangCallExpr, caller: VlangSignatureOwner, resultType: VlangTypeEx): VlangTypeEx? {
        // foo.bar<T>()
        // ^^^ qualifier
        val qualifier = (call.expression as? VlangReferenceExpression)?.getQualifier() as? VlangTypeOwner
        val qualifierType = qualifier?.getType(null)
        // foo := Foo<int>
        val instantiation = extractInstantiation(qualifierType)
        if (instantiation != null) {
            // foo.bar<T>()
            return inferGenericMethodCall(call, caller, resultType, instantiation)
        }

        return inferSimpleCall(caller, call, resultType)
    }

    private fun extractInstantiation(qualifierType: VlangTypeEx?): VlangGenericInstantiationEx? {
        if (qualifierType is VlangGenericInstantiationEx) {
            return qualifierType
        }

        if (qualifierType is VlangAliasTypeEx) {
            return extractInstantiation(qualifierType.inner)
        }

        if (qualifierType is VlangPointerTypeEx) {
            return extractInstantiation(qualifierType.inner)
        }

        return null
    }

    private fun inferSimpleCall(
        caller: VlangSignatureOwner,
        call: VlangCallExpr,
        resultType: VlangTypeEx,
        additionalSpecializationMap: Map<String, VlangTypeEx> = emptyMap()
    ): VlangTypeEx {
        val genericParameters = caller.genericParameters?.genericParameterList?.genericParameterList ?: emptyList()
        val arguments = call.parameters
        val genericArguments = call.genericArguments?.typeListNoPin?.typeList?.map { it.toEx() }
        val parameters = caller.getSignature()?.parameters?.paramDefinitionList
        val parametersTypes = parameters?.map { it.getType(null) } ?: emptyList()
        val argumentTypes = arguments.map { it.getType(null) }

        // foo<int, string>()
        //    ^^^^^^^^^^^^^ explicit generic arguments
        if (genericArguments != null) {
            // T: int
            // U: string
            val explicitSpecializationNameMap = genericParameters.map { it.name!! }.zip(genericArguments).toMap()
            return resultType.substituteGenerics(explicitSpecializationNameMap + additionalSpecializationMap)
        }

        // foo(100, 'hello')
        val reifier = VlangGenericReifier()
        reifier.reifyGenericTs(parametersTypes, argumentTypes)
        // T: int
        // U: string
        return resultType.substituteGenerics(reifier.implicitSpecializationNameMap + additionalSpecializationMap)
    }

    private fun inferGenericMethodCall(
        call: VlangCallExpr,
        caller: VlangSignatureOwner,
        resultType: VlangTypeEx,
        qualifierType: VlangGenericInstantiationEx,
    ): VlangTypeEx {
        val qualifierGenericTs = qualifierType.extractInstantiationTs(call.project)
        if (qualifierGenericTs.isEmpty()) {
            return resultType
        }
        val qualifierSpecializationMap = qualifierGenericTs.zip(qualifierType.specialization).toMap()

        return inferSimpleCall(caller, call, resultType, qualifierSpecializationMap)
    }

    /**
     * struct Foo<T, U> {
     *   a T
     *   b U
     * }
     *
     * foo := Foo<int, string>{}
     * foo.a -> int
     * foo.b -> string
     */
    fun inferGenericFetch(resolved: PsiElement, expr: VlangReferenceExpression, genericType: VlangTypeEx): VlangTypeEx? {
        if (resolved !is VlangFieldDefinition) {
            return genericType
        }

        // foo.a
        // ^^^ type of foo
        val qualifierType = (expr.getQualifier() as? VlangTypeOwner)?.getType(null) ?: return null
        if (qualifierType !is VlangGenericInstantiationEx) {
            return genericType
        }

        // foo<int, string> ->
        //   T: int
        //   U: string
        val specializationMap = qualifierType.specializationMap(expr.project)
        if (specializationMap.isEmpty()) {
            return genericType
        }

        return genericType.substituteGenerics(specializationMap)
    }
}
