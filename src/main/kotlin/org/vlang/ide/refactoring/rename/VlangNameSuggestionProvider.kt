package org.vlang.ide.refactoring.rename

import ai.grazie.utils.capitalize
import com.google.common.collect.Sets
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.NameUtil
import com.intellij.psi.codeStyle.SuggestedNameInfo
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.rename.NameSuggestionProvider
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangLangUtil
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.unwrapAlias
import org.vlang.lang.psi.types.VlangMapTypeEx

object VlangNameSuggestionProvider : NameSuggestionProvider {
    enum class Target {
        Variable,
        Function,
        Type,
        Other;

        fun isValidName(name: String): Boolean {
            return when (this) {
                Variable -> VlangNamesValidator.isValidVariableName(name)
                Function -> VlangNamesValidator.isValidFunctionName(name)
                Type     -> VlangNamesValidator.isValidTypeName(name)
                Other    -> VlangNamesValidator.isValidVariableName(name)
            }
        }
    }

    override fun getSuggestedNames(
        element: PsiElement,
        nameSuggestionContext: PsiElement?,
        result: MutableSet<String>,
    ): SuggestedNameInfo? {
        if (element is VlangNamedElement) {
            result.addAll(getSuggestedNames(element))
        }
        return SuggestedNameInfo.NULL_INFO
    }

    fun getSuggestedNames(element: VlangTypeOwner): Set<String> {
        val result = mutableSetOf<String>()
        val excluded = getExcludedNamesInContext(element)

        if (element is VlangVarDefinition) {
            val initializer = element.initializer
            if (initializer != null) {
                val fromInitializer = addSuggestionsForExpression(excluded, initializer, Target.Function)
                addIfValid(result, excluded, fromInitializer, Target.Function)
            }

            val parent = element.parent
            if (parent is VlangRangeClause) {
                val expression = parent.expression
                val type = expression?.getType(null)?.unwrapAlias()

                val variables = parent.variablesList
                if (variables.size == 2) {
                    val first = variables.first()
                    if (PsiTreeUtil.isAncestor(first, element, false)) {
                        val isMap = type is VlangMapTypeEx
                        if (isMap) {
                            result.add("key")
                        } else {
                            result.add("i")
                            result.add("index")
                        }
                        return result
                    }
                }

                if (expression != null) {
                    val fromExpression = addSuggestionsForExpression(excluded, expression, Target.Function, true)
                    addIfValid(result, excluded, fromExpression, Target.Function)
                }
            }
        }

        return result
    }

    private fun getExcludedNamesInContext(element: VlangTypeOwner): Set<String> {
        val names = VlangLangUtil.findNamesInScope(element)
        return names.toSet()
    }

    private fun addSuggestionsForExpression(
        excluded: Set<String>,
        element: VlangTypeOwner,
        target: Target,
        unpluralize: Boolean = false,
    ): MutableSet<String> {
        val suggestions = mutableSetOf<String>()
        if (element is VlangCallExpr) {
            getSuggestionsForCall(element, target).forEach { addIfValid(suggestions, excluded, it, target) }
        }

        val fromReference = getSuggestionsFromReference(element, false, target)
            .map { if (unpluralize) unpluralize(it) else it }
        addIfValid(suggestions, excluded, fromReference, target)
        return suggestions
    }

    private fun getSuggestionsForCall(callExpr: VlangCallExpr, target: Target): List<MutableSet<String>> {
        val res = getSuggestionsFromCallReference(callExpr, false, target)
        if (res.isNotEmpty()) return listOf(res.toMutableSet())

        return emptyList()
    }

    private fun getSuggestionsFromCallReference(callExpr: VlangCallExpr, capitalize: Boolean, target: Target): List<String> {
        val identifier = callExpr.identifier ?: return emptyList()
        val referenceName = identifier.text
        return getSuggestionsFromName(referenceName, capitalize, false, target)
    }

    private fun getSuggestionsFromReference(
        expr: VlangTypeOwner,
        capitalize: Boolean,
        target: Target,
    ): List<String> {
        return if (expr is VlangReferenceExpression)
            getSuggestionsFromName(expr.getText(), capitalize, false, target)
        else
            emptyList()
    }

    private fun getSuggestionsFromName(
        name: String?,
        capitalize: Boolean,
        pluralize: Boolean,
        target: Target,
    ): List<String> {
        if (name.isNullOrEmpty()) return emptyList()
        val suggestions = LinkedHashSet(
            NameUtil.getSuggestionsByName(
                name, "", "", target == Target.Type, false, pluralize
            )
        )
        return suggestions.map {
            if (capitalize) it.capitalize() else it
        }
    }

    private fun addIfValid(
        result: MutableSet<String>,
        excluded: Set<String>,
        candidates: Iterable<String>,
        target: Target,
    ) {
        val allExcluded = Sets.union(result, excluded)
        for (candidate in candidates) {
            if (VlangNamesValidator.isKeyword(candidate, null)) {
                result.add(candidate + "_")
                continue
            }
            if (isValidName(candidate, allExcluded, target)) {
                result.add(candidate)
            }
        }
    }

    private fun isValidName(name: String, excluded: Set<String>, target: Target): Boolean {
        return name.isNotEmpty() &&
                "_" != name &&
                !excluded.contains(name) &&
                VlangNamesValidator.isIdentifier(name, null) &&
                target.isValidName(name)
    }

    private fun unpluralize(name: String): String {
        val parts = name.split("_")
        if (parts.size == 1) {
            return StringUtil.unpluralize(name) ?: name
        }

        return parts.joinToString("_") {
            StringUtil.unpluralize(it) ?: it
        }
    }
}
