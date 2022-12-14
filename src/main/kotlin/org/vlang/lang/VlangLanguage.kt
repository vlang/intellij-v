package org.vlang.lang

import com.intellij.lang.Language

object VlangLanguage : Language("vlang") {
    override fun isCaseSensitive() = true
    override fun getDisplayName() = "V"
}
