package org.vlang.ide.injection

import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.lang.injection.general.Injection
import com.intellij.lang.injection.general.LanguageInjectionPerformer
import com.intellij.psi.ElementManipulators
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost

class VlangLanguageInjectionPerformer : LanguageInjectionPerformer {
    override fun isPrimary() = true

    override fun performInjection(registrar: MultiHostRegistrar, injection: Injection, context: PsiElement): Boolean {
        val injectedLanguage = injection.injectedLanguage ?: return false
        registrar.startInjecting(injectedLanguage)
        registrar.addPlace(null, null, context as PsiLanguageInjectionHost, ElementManipulators.getValueTextRange(context))
        registrar.doneInjecting()
        return true
    }
}
