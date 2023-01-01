package org.vlang.markdown

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.lang.Language
import org.intellij.plugins.markdown.injection.CodeFenceLanguageProvider
import org.vlang.lang.VlangLanguage

class VlangFenceLanguageProvider : CodeFenceLanguageProvider {
    override fun getLanguageByInfoString(lang: String): Language? = if (lang == "v") { VlangLanguage } else { null }

    override fun getCompletionVariantsForInfoString(p0: CompletionParameters): MutableList<LookupElement> = mutableListOf()
}
