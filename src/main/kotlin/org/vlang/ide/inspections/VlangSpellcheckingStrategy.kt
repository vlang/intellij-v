package org.vlang.ide.inspections

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.spellchecker.inspections.PlainTextSplitter
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy
import com.intellij.spellchecker.tokenizer.TokenConsumer
import com.intellij.spellchecker.tokenizer.Tokenizer
import org.vlang.lang.VlangLanguage
import org.vlang.lang.psi.VlangStringLiteral

class VlangSpellcheckingStrategy : SpellcheckingStrategy() {
    override fun isMyContext(element: PsiElement): Boolean {
        return element.language == VlangLanguage
    }

    override fun getTokenizer(element: PsiElement?): Tokenizer<*> {
        if (element is VlangStringLiteral) {
            return VlangStringLiteralTokenizer.INSTANCE
        }
        return super.getTokenizer(element)
    }

    class VlangStringLiteralTokenizer : Tokenizer<VlangStringLiteral>() {
        override fun tokenize(element: VlangStringLiteral, consumer: TokenConsumer) {
            val text = element.contents
            consumer.consumeToken(element, text, true, 1, TextRange.allOf(text), PlainTextSplitter.getInstance())
        }

        companion object {
            val INSTANCE = VlangStringLiteralTokenizer()
        }
    }
}
