package org.vlang.ide.codeInsight

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.childrenOfType
import org.vlang.lang.psi.*
import org.vlang.lang.psi.types.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx

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

        // foo<int, string> -> foo
        val genericTs = qualifierType.extractInstantiationTs(expr.project)
        if (genericTs.isEmpty()) {
            return genericType
        }

        val specializationMap = genericTs.zip(qualifierType.specialization).toMap()
        return genericType.substituteGenerics(specializationMap)
    }

    private fun VlangGenericInstantiationEx.extractInstantiationTs(project: Project): List<String> {
        if (inner !is VlangResolvableTypeEx<*>) {
            return emptyList()
        }

        val innerResolved = inner.resolve(project) as? VlangTypeOwner ?: return emptyList()
        val resolvedType = innerResolved.childrenOfType<VlangGenericParametersOwner>().firstOrNull() ?: return emptyList()
        return extractGenericParameters(resolvedType)
    }

    private fun extractGenericParameters(resolvedType: VlangGenericParametersOwner) =
        resolvedType.genericParameters?.genericParameterList?.genericParameterList?.map { it.name!! } ?: emptyList()
}
