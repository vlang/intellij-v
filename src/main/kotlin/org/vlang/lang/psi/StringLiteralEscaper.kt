package org.vlang.lang.psi

import com.intellij.openapi.util.TextRange
import com.intellij.psi.LiteralTextEscaper
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl

class StringLiteralEscaper<T : PsiLanguageInjectionHost>(host: T) : LiteralTextEscaper<T>(host) {
    private var outSourceOffsets: IntArray = IntArray(0)

    override fun decode(rangeInsideHost: TextRange, outChars: StringBuilder): Boolean {
        val subText = rangeInsideHost.substring(myHost!!.text)
        outSourceOffsets = IntArray(subText.length + 1)
        return PsiLiteralExpressionImpl.parseStringCharacters(subText, outChars, outSourceOffsets)
    }

    override fun getOffsetInHost(offsetInDecoded: Int, rangeInsideHost: TextRange): Int {
        val result = if (offsetInDecoded < outSourceOffsets.size) outSourceOffsets[offsetInDecoded] else -1
        return if (result == -1) -1 else result.coerceAtMost(rangeInsideHost.length) + rangeInsideHost.startOffset
    }

    override fun isOneLine() = false
}
