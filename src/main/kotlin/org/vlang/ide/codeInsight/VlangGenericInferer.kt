package org.vlang.ide.codeInsight

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.*
import org.vlang.lang.psi.types.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx

object VlangGenericInferer {
    fun inferGenericCall(
        argumentsOwner: VlangGenericArgumentsOwner,
        parametersOwner: VlangGenericParametersOwner,
        resultType: VlangTypeEx,
    ): VlangTypeEx {
        val genericTsMap = inferGenericTsMap(argumentsOwner, parametersOwner)
        return resultType.substituteGenerics(genericTsMap)
    }

    fun inferGenericTsMap(
        argumentsOwner: VlangGenericArgumentsOwner,
        parametersOwner: VlangGenericParametersOwner,
    ): Map<String, VlangTypeEx> {
        // only call can have qualifier
        if (argumentsOwner is VlangCallExpr) {
            // foo.bar<T>()
            // ^^^ qualifier
            val qualifier = (argumentsOwner.expression as? VlangReferenceExpression)?.getQualifier() as? VlangTypeOwner
            val qualifierType = qualifier?.getType(null)
            // foo := Foo<int>
            val instantiation = extractInstantiation(qualifierType)
            if (instantiation != null) {
                // foo.bar<T>()
                val qualifierSpecializationMap = inferQualifierGenericTsMap(argumentsOwner.project, instantiation)
                return inferSimpleGenericTsMap(argumentsOwner, parametersOwner, qualifierSpecializationMap)
            }
        }

        return inferSimpleGenericTsMap(argumentsOwner, parametersOwner)
    }

    fun getGenericParameters(
        argumentsOwner: VlangGenericArgumentsOwner,
        parametersOwner: VlangGenericParametersOwner,
    ): List<String> {
        // only call can have qualifier
        if (argumentsOwner is VlangCallExpr) {
            // foo.bar<T>()
            // ^^^ qualifier
            val qualifier = (argumentsOwner.expression as? VlangReferenceExpression)?.getQualifier() as? VlangTypeOwner
            val qualifierType = qualifier?.getType(null)
            // foo := Foo<int>
            val instantiation = extractInstantiation(qualifierType)
            if (instantiation != null) {
                // foo.bar<T>()
                val instantiationParameters = instantiation.extractInstantiationTs(argumentsOwner.project)
                val genericParameters = getGenericParameters(parametersOwner)
                val parameters = genericParameters.toMutableSet()
                parameters.addAll(instantiationParameters)
                return parameters.toList()
            }
        }

        return getGenericParameters(parametersOwner)
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

        if (qualifierType is VlangOptionTypeEx) {
            return extractInstantiation(qualifierType.inner)
        }

        if (qualifierType is VlangResultTypeEx) {
            return extractInstantiation(qualifierType.inner)
        }

        return null
    }

    private fun inferSimpleGenericTsMap(
        argumentsOwner: VlangGenericArgumentsOwner,
        genericParametersOwner: VlangGenericParametersOwner,
        additionalSpecializationMap: Map<String, VlangTypeEx> = emptyMap(),
    ): Map<String, VlangTypeEx> {
        val genericParameters = getGenericParameters(genericParametersOwner)

        // No data for inference, call is not generic.
        if (genericParameters.isEmpty() && additionalSpecializationMap.isEmpty()) {
            return emptyMap()
        }

        val genericArguments = argumentsOwner.genericArguments?.typeArguments?.map { it.toEx() }

        // foo<int, string>()
        //    ^^^^^^^^^^^^^ explicit generic arguments
        // Array<string>{}
        //      ^^^^^^^^ explicit generic arguments
        if (genericArguments != null) {
            // T: int
            // U: string
            val explicitSpecializationNameMap = genericParameters.zip(genericArguments).toMap()
            return explicitSpecializationNameMap + additionalSpecializationMap
        }

        // We can reify generic Ts only for calls, not for struct initialization.
        if (genericParametersOwner !is VlangSignatureOwner || argumentsOwner !is VlangCallExpr) {
            return emptyMap()
        }

        val arguments = argumentsOwner.parameters
        val parameters = genericParametersOwner.getSignature()?.parameters?.paramDefinitionList
        val parametersTypes = parameters?.map { it.getType(null) } ?: emptyList()
        val argumentTypes = arguments.map { it.getType(null) }

        // foo(100, 'hello')
        val reifier = VlangGenericReifier()
        reifier.reifyGenericTs(parametersTypes, argumentTypes)
        // T: int
        // U: string
        return reifier.implicitSpecializationNameMap + additionalSpecializationMap
    }

    private fun inferQualifierGenericTsMap(
        project: Project,
        qualifierType: VlangGenericInstantiationEx,
    ): Map<String, VlangTypeEx> {
        val qualifierGenericTs = qualifierType.extractInstantiationTs(project)
        if (qualifierGenericTs.isEmpty()) {
            return emptyMap()
        }
        return qualifierGenericTs.zip(qualifierType.specialization).toMap()
    }

    /**
     * ```
     * struct Foo<T, U> {
     *   a T
     *   b U
     * }
     *
     * foo := Foo<int, string>{}
     * foo.a -> int
     * foo.b -> string
     * ```
     */
    fun inferGenericFetch(
        resolved: PsiElement,
        expr: VlangReferenceExpression?,
        genericType: VlangTypeEx,
    ): VlangTypeEx {
        if (resolved !is VlangFieldDefinition) {
            return genericType
        }

        // foo.a
        // ^^^ type of foo
        var qualifierType = (expr?.getQualifier() as? VlangTypeOwner)?.getType(null)
        if (qualifierType == null) {
            // possibly generic struct initialization
            // Foo[int]{ x: 100 }
            //           ^ type for this is int
            val literalValue = expr?.parentOfType<VlangLiteralValueExpression>() ?:  return genericType
            qualifierType = literalValue.getType(null)
        }

        val instantiation = extractInstantiation(qualifierType)
        if (instantiation !is VlangGenericInstantiationEx) {
            return genericType
        }

        // foo<int, string> ->
        //   T: int
        //   U: string
        val specializationMap = instantiation.specializationMap(resolved.project)
        if (specializationMap.isEmpty()) {
            return genericType
        }

        return genericType.substituteGenerics(specializationMap)
    }

    // Not real "generic", some kind of pseudo generic for builtin cases
    fun inferGenericIt(expr: VlangReferenceExpression): VlangTypeEx? {
        val isItVariable = VlangCodeInsightUtil.isItVariable(expr.getIdentifier())

        var callExpr = VlangCodeInsightUtil.getCallExpr(expr) ?: return null
        while (PsiTreeUtil.isAncestor(callExpr.expression, expr, false)) {
            callExpr = VlangCodeInsightUtil.getCallExpr(callExpr) ?: return null
        }

        val methodsName = if (isItVariable) arrayOf("filter", "map", "any", "all") else arrayOf("sort")
        while (!VlangCodeInsightUtil.isArrayMethodCall(callExpr, *methodsName)) {
            callExpr = VlangCodeInsightUtil.getCallExpr(callExpr) ?: return null
        }

        val type = VlangTypeInferenceUtil.callerType(callExpr)
        val elementType = if (type is VlangArrayTypeEx) {
            type.inner
        } else {
            null
        }

        return elementType
    }

    private fun getGenericParameters(caller: VlangGenericParametersOwner) =
        caller.genericParameters?.parameters?.map { it.name!! } ?: emptyList()
}
