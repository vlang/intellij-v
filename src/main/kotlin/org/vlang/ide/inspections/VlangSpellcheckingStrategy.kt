package org.vlang.ide.inspections

import com.intellij.psi.PsiElement
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy
import org.vlang.lang.VlangLanguage

class VlangSpellcheckingStrategy : SpellcheckingStrategy() {
    override fun isMyContext(element: PsiElement): Boolean {
        return element.language == VlangLanguage.INSTANCE
    }
}
