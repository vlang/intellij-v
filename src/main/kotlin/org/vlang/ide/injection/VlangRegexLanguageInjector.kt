package org.vlang.ide.injection

import com.intellij.psi.ElementManipulators
import com.intellij.psi.InjectedLanguagePlaces
import com.intellij.psi.LanguageInjector
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.PsiTreeUtil
import org.intellij.lang.regexp.RegExpLanguage
import org.vlang.lang.psi.*
import org.vlang.utils.parentNth

class VlangRegexLanguageInjector : LanguageInjector {
    override fun getLanguagesToInject(host: PsiLanguageInjectionHost, injectionPlacesRegistrar: InjectedLanguagePlaces) {
        if (host !is VlangStringLiteral) return

        // string with interpolation
        if (PsiTreeUtil.findChildOfType(host, VlangLongStringTemplateEntry::class.java) != null) {
            return
        }

        var callExpr = host.parentNth<VlangCallExpr>(4)

        val variableDeclaration = host.parentNth<VlangVarDeclaration>(1)
        if (variableDeclaration != null) {
            val variable = variableDeclaration.varDefinitionList.firstOrNull()
            val variableName = variable?.name ?: return
            if (!possiblyRegex(variableName)) {
                return
            }

            val firstUsage = ReferencesSearch.search(variable).allowParallelProcessing().findFirst() ?: return

            callExpr = firstUsage.element.parentNth(4) ?: return
        }

        if (callExpr == null) return

        val called = callExpr.resolve() as? VlangNamedElement ?: return
        if (called.name == "regex_opt" || called.name == "compile_opt") {
            injectionPlacesRegistrar.addPlace(RegExpLanguage.INSTANCE, ElementManipulators.getValueTextRange(host), null, null)
        }
    }

    companion object {
        fun possiblyRegex(name: String): Boolean {
            val variants = listOf("regex", "regexp", "re", "query")
            return variants.any {
                name.startsWith(it) || name.endsWith(it)
            }
        }
    }
}
