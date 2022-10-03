package org.vlang.lang

import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.openapi.util.Key
import gnu.trove.TObjectIntHashMap
import org.vlang.lang.VlangTypes.GREATER
import org.vlang.lang.VlangTypes.SHIFT_RIGHT
import java.util.*

object VlangParserUtil : GeneratedParserUtilBase() {
    private val MODES_KEY = Key.create<TObjectIntHashMap<String>>("MODES_KEY")
    private val MODES_LIST_KEY = Key.create<Stack<String>>("MODES_KEY")

    private fun getParsingModes(builder: PsiBuilder): TObjectIntHashMap<String> {
        var flags = builder.getUserData(MODES_KEY)
        if (flags == null) {
            flags = TObjectIntHashMap<String>()
            builder.putUserData(MODES_KEY, flags)
        }
        return flags
    }

    private fun getParsingModesStack(builder: PsiBuilder): Stack<String> {
        var flags = builder.getUserData(MODES_LIST_KEY)
        if (flags == null) {
            flags = Stack<String>()
            flags.push("DEFAULT_MODE")
            builder.putUserData(MODES_LIST_KEY, flags)
        }
        return flags
    }

//    fun consumeBlock(builder: PsiBuilder, level: Int): Boolean {
//        val file = builder.getUserDataUnprotected(FileContextUtil.CONTAINING_FILE_KEY)
//        val data = file?.getUserData(IndexingDataKeys.VIRTUAL_FILE)
//            ?: return false
//        var i = 0
//        val m = builder.mark()
//        do {
//            val type = builder.tokenType
//            if (type === VlangTypes.TYPE_ && nextIdentifier(builder)) { // don't count a.(type), only type <ident>
//                m.rollbackTo()
//                return false
//            }
//            i += if (type === VlangTypes.LBRACE) 1 else if (type === VlangTypes.RBRACE) -1 else 0
//            builder.advanceLexer()
//        } while (i > 0 && !builder.eof())
//        val result = i == 0
//        if (result) {
//            m.drop()
//        } else {
//            m.rollbackTo()
//        }
//        return result
//    }
//
//    private fun nextIdentifier(builder: PsiBuilder): Boolean {
//        var e: IElementType
//        var i = 0
//        while (builder.rawLookup(++i).also { e = it!! } === VlangParserDefinition.WS || e === VlangParserDefinition.NLS) {
//        }
//        return e === VlangTypes.IDENTIFIER
//    }
//
//    fun emptyImportList(builder: PsiBuilder?, level: Int): Boolean {
//        val marker = getCurrentMarker((if (builder is PsiBuilderAdapter) builder.delegate else builder)!!)
//        marker?.setCustomEdgeTokenBinders(WhitespacesBinders.GREEDY_LEFT_BINDER, null)
//        return true
//    }

    @JvmStatic
    fun isModeOn(builder: PsiBuilder, level: Int, mode: String?): Boolean {
        return getParsingModes(builder)[mode] > 0
    }

    @JvmStatic
    fun withOn(builder: PsiBuilder, level_: Int, mode: String, parser: Parser): Boolean {
        return withImpl(builder, level_, mode, true, parser, parser)
    }

    @JvmStatic
    fun withOff(
        builder: PsiBuilder,
        level_: Int,
        parser: Parser,
        vararg modes: String,
    ): Boolean {
        val map = getParsingModes(builder)
        val prev = TObjectIntHashMap<String>()

        val nowStack = getParsingModesStack(builder)
        val prevStack = Stack<String>()
        prevStack.addAll(nowStack)

        for (mode in modes) {
            val p = map[mode]
            if (p > 0) {
                map.put(mode, 0)
                prev.put(mode, p)
            }

            if (nowStack.contains(mode)) {
                nowStack.remove(mode)
            }
        }

        val result = parser.parse(builder, level_)
        prev.forEachEntry { mode, count ->
            map.put(mode, count)
            true
        }

        nowStack.clear()
        nowStack.addAll(prevStack)

        return result
    }

    private fun withImpl(
        builder: PsiBuilder,
        level_: Int,
        mode: String,
        onOff: Boolean,
        whenOn: Parser,
        whenOff: Parser,
    ): Boolean {
        val map = getParsingModes(builder)
        val prev = map[mode]
        val change = prev and 1 == 0 == onOff
        if (change) map.put(mode, prev shl 1 or if (onOff) 1 else 0)
        val result: Boolean = (if (change) whenOn else whenOff).parse(builder, level_)
        if (change) map.put(mode, prev)
        return result
    }

    @JvmStatic
    fun isModeOff(builder: PsiBuilder, level: Int, mode: String?): Boolean {
        return getParsingModes(builder)[mode] == 0
    }

    @JvmStatic
    fun isLastIs(builder: PsiBuilder, level: Int, mode: String?): Boolean {
        return getParsingModesStack(builder).peek() == mode
    }

    @JvmStatic
    fun isLastNotIs(builder: PsiBuilder, level: Int, mode: String?): Boolean {
        return getParsingModesStack(builder).peek() != mode
    }

    @JvmStatic
    fun prevIsType(builder: PsiBuilder, level: Int): Boolean {
        val marker = builder.latestDoneMarker
        val type = marker?.tokenType
        return type === VlangTypes.ARRAY_OR_SLICE_TYPE || type === VlangTypes.MAP_TYPE || type === VlangTypes.STRUCT_TYPE
    }

    @JvmStatic
    fun prevIsNotType(builder: PsiBuilder, level: Int): Boolean {
        return !prevIsType(builder, level)
    }

    @JvmStatic
    fun prevIsNotFunType(builder: PsiBuilder, level: Int): Boolean {
        val marker = builder.latestDoneMarker
        val type = marker?.tokenType
        return type !== VlangTypes.FUNCTION_TYPE
    }

    @JvmStatic
    fun keyOrValueExpression(builder: PsiBuilder, level: Int): Boolean {
        val m = enter_section_(builder)
        var r = VlangParser.Expression(builder, level + 1, -1)

        if (!r)
            r = VlangParser.LiteralValueExpression(builder, level + 1)
        val type = if (r && builder.tokenType === VlangTypes.COLON)
            VlangTypes.KEY
        else
            VlangTypes.VALUE
        exit_section_(builder, m, type, r)
        return r
    }

    @JvmStatic
    fun gtGt(builder: PsiBuilder, level: Int): Boolean {
        val marker = builder.mark()
        if (!consumeToken(builder, GREATER)) {
            marker.rollbackTo()
            return false
        }
        if (!consumeToken(builder, GREATER)) {
            marker.rollbackTo()
            return false
        }
        marker.collapse(SHIFT_RIGHT)
        return true
    }

    @JvmStatic
    fun typeOrExpression(builder: PsiBuilder, level: Int): Boolean {
        val m = builder.mark()
        var r = VlangParser.Type(builder, level + 1)
        if (r) {
            if (builder.tokenType === VlangTypes.COMMA || builder.tokenType === VlangTypes.LBRACE) {
                m.drop()
                return true
            }
        }

        m.rollbackTo()
        val m1 = builder.mark()
        r = VlangParser.Expression(builder, level + 1, -1)

        if (r) {
            if (builder.tokenType === VlangTypes.COMMA || builder.tokenType === VlangTypes.LBRACE) {
                m1.drop()
                return true
            }
        }
        m1.rollbackTo()

        return r
    }

    @JvmStatic
    fun enterMode(builder: PsiBuilder, level: Int, mode: String?): Boolean {
        val flags = getParsingModes(builder)
        if (!flags.increment(mode)) {
            flags.put(mode, 1)
        }
        val stack = getParsingModesStack(builder)
        stack.push(mode)
        return true
    }

    private fun exitMode(builder: PsiBuilder, level: Int, mode: String, safe: Boolean, all: Boolean = false): Boolean {
        val flags = getParsingModes(builder)
        val count = flags[mode]
        if (count == 1) {
            flags.remove(mode)
        } else if (count > 1) {
            if (all) {
                flags.remove(mode)
            } else {
                flags.put(mode, count - 1)
            }
        } else if (!safe) {
            builder.error("Could not exit inactive '" + mode + "' mode at offset " + builder.currentOffset)
        }

        val stack = getParsingModesStack(builder)
        if (stack.isEmpty() && !safe) {
            builder.error("Could not exit '" + mode + "' mode at offset " + builder.currentOffset + ": stack is empty")
            return true
        }

        val top = stack.peek()
        if (top == mode) {
            stack.pop()

            if (all) {
                while (!stack.isEmpty() && stack.peek() == mode) {
                    stack.pop()
                }
            }
        }

        return true
    }

    @JvmStatic
    fun exitMode(builder: PsiBuilder, level: Int, mode: String): Boolean {
        return exitMode(builder, level, mode, safe = false)
    }

    @JvmStatic
    fun exitModeSafe(builder: PsiBuilder, level: Int, mode: String): Boolean {
        return exitMode(builder, level, mode, safe = true)
    }

    @JvmStatic
    fun exitAllModeSafe(builder: PsiBuilder, level: Int, mode: String): Boolean {
        return exitMode(builder, level, mode, safe = true, all = true)
    }

//    fun isBuiltin(builder: PsiBuilder, level: Int): Boolean {
//        val marker = builder.latestDoneMarker ?: return false
//        val text = builder.originalText.subSequence(marker.startOffset, marker.endOffset).toString().trim { it <= ' ' }
//        return "make" == text || "new" == text
//    }
//
//    private fun getCurrentMarker(builder: PsiBuilder): PsiBuilder.Marker? {
//        try {
//            for (field in builder.javaClass.declaredFields) {
//                if ("MyList" == field.type.simpleName) {
//                    field.isAccessible = true
//                    return ContainerUtil.getLastItem(
//                        field[builder] as List<PsiBuilder.Marker>
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