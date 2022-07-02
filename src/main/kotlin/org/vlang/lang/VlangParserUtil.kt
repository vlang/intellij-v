package org.vlang.lang

import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.openapi.util.Key
import gnu.trove.TObjectIntHashMap

object VlangParserUtil : GeneratedParserUtilBase() {
    private val MODES_KEY = Key.create<TObjectIntHashMap<String>>("MODES_KEY")
//    private fun getParsingModes(builder_: PsiBuilder): TObjectIntHashMap<String?> {
//        var flags = builder_.getUserDataUnprotected<TObjectIntHashMap<String?>>(MODES_KEY)!!
//        if (flags == null) builder_.putUserDataUnprotected(MODES_KEY, TObjectIntHashMap<String>().also { flags = it })
//        return flags
//    }
//
//    fun consumeBlock(builder_: PsiBuilder, level: Int): Boolean {
//        val file = builder_.getUserDataUnprotected(FileContextUtil.CONTAINING_FILE_KEY)
//        val data = file?.getUserData(IndexingDataKeys.VIRTUAL_FILE)
//            ?: return false
//        var i = 0
//        val m = builder_.mark()
//        do {
//            val type = builder_.tokenType
//            if (type === VlangTypes.TYPE_ && nextIdentifier(builder_)) { // don't count a.(type), only type <ident>
//                m.rollbackTo()
//                return false
//            }
//            i += if (type === VlangTypes.LBRACE) 1 else if (type === VlangTypes.RBRACE) -1 else 0
//            builder_.advanceLexer()
//        } while (i > 0 && !builder_.eof())
//        val result = i == 0
//        if (result) {
//            m.drop()
//        } else {
//            m.rollbackTo()
//        }
//        return result
//    }
//
//    private fun nextIdentifier(builder_: PsiBuilder): Boolean {
//        var e: IElementType
//        var i = 0
//        while (builder_.rawLookup(++i).also { e = it!! } === VlangParserDefinition.WS || e === VlangParserDefinition.NLS) {
//        }
//        return e === VlangTypes.IDENTIFIER
//    }
//
//    fun emptyImportList(builder_: PsiBuilder?, level: Int): Boolean {
//        val marker = getCurrentMarker((if (builder_ is PsiBuilderAdapter) builder_.delegate else builder_)!!)
//        marker?.setCustomEdgeTokenBinders(WhitespacesBinders.GREEDY_LEFT_BINDER, null)
//        return true
//    }
//
//    fun isModeOn(builder_: PsiBuilder, level: Int, mode: String?): Boolean {
//        return getParsingModes(builder_)[mode] > 0
//    }
//
//    fun withOn(builder_: PsiBuilder, level_: Int, mode: String, parser: GeneratedParserUtilBase.Parser): Boolean {
//        return withImpl(builder_, level_, mode, true, parser, parser)
//    }
//
//    fun withOff(
//        builder_: PsiBuilder,
//        level_: Int,
//        parser: GeneratedParserUtilBase.Parser,
//        vararg modes: String
//    ): Boolean {
//        val map = getParsingModes(builder_)
//        val prev = TObjectIntHashMap<String>()
//        for (mode in modes) {
//            val p = map[mode]
//            if (p > 0) {
//                map.put(mode, 0)
//                prev.put(mode, p)
//            }
//        }
//        val result: Boolean = parser.parse(builder_, level_)
//        prev.forEachEntry { mode: String?, p: Int ->
//            map.put(mode, p)
//            true
//        }
//        return result
//    }
//
//    private fun withImpl(
//        builder_: PsiBuilder,
//        level_: Int,
//        mode: String,
//        onOff: Boolean,
//        whenOn: GeneratedParserUtilBase.Parser,
//        whenOff: GeneratedParserUtilBase.Parser
//    ): Boolean {
//        val map = getParsingModes(builder_)
//        val prev = map[mode]
//        val change = prev and 1 == 0 == onOff
//        if (change) map.put(mode, prev shl 1 or if (onOff) 1 else 0)
//        val result: Boolean = (if (change) whenOn else whenOff).parse(builder_, level_)
//        if (change) map.put(mode, prev)
//        return result
//    }
//
//    fun isModeOff(builder_: PsiBuilder, level: Int, mode: String?): Boolean {
//        return getParsingModes(builder_)[mode] == 0
//    }
//
//    fun prevIsType(builder_: PsiBuilder, level: Int): Boolean {
//        val marker = builder_.latestDoneMarker
//        val type = marker?.tokenType
//        return type === VlangTypes.ARRAY_OR_SLICE_TYPE || type === VlangTypes.MAP_TYPE || type === VlangTypes.STRUCT_TYPE
//    }
//
//    fun keyOrValueExpression(builder_: PsiBuilder, level: Int): Boolean {
//        val m: PsiBuilder.Marker = GeneratedParserUtilBase.enter_section_(builder_)
//        var r: Boolean = VlangParser.Expression(builder_, level + 1, -1)
//        if (!r) r = VlangParser.LiteralValue(builder_, level + 1)
//        val type: IElementType = if (r && builder_.tokenType === VlangTypes.COLON) VlangTypes.KEY else VlangTypes.VALUE
//        GeneratedParserUtilBase.exit_section_(builder_, m, type, r)
//        return r
//    }
//
//    fun enterMode(builder_: PsiBuilder, level: Int, mode: String?): Boolean {
//        val flags = getParsingModes(builder_)
//        if (!flags.increment(mode)) flags.put(mode, 1)
//        return true
//    }
//
//    private fun exitMode(builder_: PsiBuilder, level: Int, mode: String, safe: Boolean): Boolean {
//        val flags = getParsingModes(builder_)
//        val count = flags[mode]
//        if (count == 1) flags.remove(mode) else if (count > 1) flags.put(
//            mode,
//            count - 1
//        ) else if (!safe) builder_.error("Could not exit inactive '" + mode + "' mode at offset " + builder_.currentOffset)
//        return true
//    }
//
//    fun exitMode(builder_: PsiBuilder, level: Int, mode: String): Boolean {
//        return exitMode(builder_, level, mode, false)
//    }
//
//    fun exitModeSafe(builder_: PsiBuilder, level: Int, mode: String): Boolean {
//        return exitMode(builder_, level, mode, true)
//    }
//
//    fun isBuiltin(builder_: PsiBuilder, level: Int): Boolean {
//        val marker = builder_.latestDoneMarker ?: return false
//        val text = builder_.originalText.subSequence(marker.startOffset, marker.endOffset).toString().trim { it <= ' ' }
//        return "make" == text || "new" == text
//    }
//
//    private fun getCurrentMarker(builder_: PsiBuilder): PsiBuilder.Marker? {
//        try {
//            for (field in builder_.javaClass.declaredFields) {
//                if ("MyList" == field.type.simpleName) {
//                    field.isAccessible = true
//                    return ContainerUtil.getLastItem(
//                        field[builder_] as List<PsiBuilder.Marker>
//                    )
//                }
//            }
//        } catch (ignored: Exception) {
//        }
//        return null
//    }
//
//    fun nextTokenIsSmart(builder: PsiBuilder?, token: IElementType?): Boolean {
//        return GeneratedParserUtilBase.nextTokenIsFast(
//            builder,
//            token
//        ) || GeneratedParserUtilBase.ErrorState.get(builder).completionState != null
//    }
//
//    fun nextTokenIsSmart(builder: PsiBuilder?, vararg tokens: IElementType?): Boolean {
//        return GeneratedParserUtilBase.nextTokenIsFast(builder, *tokens) || GeneratedParserUtilBase.ErrorState.get(
//            builder
//        ).completionState != null
//    }
}