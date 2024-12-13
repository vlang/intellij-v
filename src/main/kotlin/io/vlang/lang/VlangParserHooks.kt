package io.vlang.lang

import com.intellij.lang.impl.PsiBuilderImpl
import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.openapi.diagnostic.thisLogger

class VlangParserHooks {

    companion object {

        val logger = thisLogger()

        @JvmField
        val MY_HOOK = GeneratedParserUtilBase.Hook<String> { builder, marker, param ->
            val m = marker as? PsiBuilderImpl.ProductionMarker
            val start = m?.startOffset ?: builder.currentOffset
            val end = m?.endOffset ?: start
            val prefix = "[$start, $end]" + (if (m == null) "" else " " + m.tokenType)
            builder.mark().error("$prefix: $param")
            marker
        }

    }
}