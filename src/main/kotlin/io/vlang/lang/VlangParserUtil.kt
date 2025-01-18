package io.vlang.lang

import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.openapi.util.Key
import io.vlang.lang.VlangTypes.*
import io.vlang.lang.psi.VlangTokenTypes
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import java.util.*

object VlangParserUtil : GeneratedParserUtilBase() {
    private val MODES_KEY = Key.create<Object2IntOpenHashMap<String>>("MODES_KEY")
    private val MODES_LIST_KEY = Key.create<Stack<String>>("MODES_KEY")

    private fun getParsingModes(builder: PsiBuilder): Object2IntOpenHashMap<String> {
        var flags = builder.getUserData(MODES_KEY)
        if (flags == null) {
            flags = Object2IntOpenHashMap(100)
            flags.defaultReturnValue(0)
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
    fun isModeOn(builder: PsiBuilder, @Suppress("UNUSED") level: Int, mode: String?): Boolean {
        return getParsingModes(builder).getInt(mode) > 0
    }

    @JvmStatic
    fun withOn(builder: PsiBuilder, level: Int, mode: String, parser: Parser): Boolean {
        val nowStack = getParsingModesStack(builder)
        val prevStack = Stack<String>()
        prevStack.addAll(nowStack)

        enterMode(builder, level, mode)
        val result = parser.parse(builder, level)
        exitMode(builder, level, mode)

        nowStack.clear()
        nowStack.addAll(prevStack)

        return result
    }

    @JvmStatic
    fun withOff(
        builder: PsiBuilder,
        level: Int,
        parser: Parser,
        vararg modes: String,
    ): Boolean {
        val map = getParsingModes(builder)
        val prev = Object2IntOpenHashMap<String>()

        val nowStack = getParsingModesStack(builder)
        val prevStack = Stack<String>()
        prevStack.addAll(nowStack)

        for (mode in modes) {
            val p = map.getInt(mode)
            if (p > 0) {
                map[mode] = 0
                prev[mode] = p
            }

            if (nowStack.contains(mode)) {
                nowStack.remove(mode)
            }
        }

        val result = parser.parse(builder, level)
        @Suppress("JavaMapForEach")
        prev.forEach { mode, count ->
            map[mode] = count
        }

        nowStack.clear()
        nowStack.addAll(prevStack)

        return result
    }

    @Suppress("UNUSED")
    private fun withImpl(
        builder: PsiBuilder,
        level: Int,
        mode: String,
        onOff: Boolean,
        whenOn: Parser,
        whenOff: Parser,
    ): Boolean {
        val modes = getParsingModes(builder)
        val prev = modes.getInt(mode)
        val change = prev and 1 == 0 == onOff
        if (change) modes[mode] = prev shl 1 or if (onOff) 1 else 0
        val result: Boolean = (if (change) whenOn else whenOff).parse(builder, level)
        if (change) modes[mode] = prev
        return result
    }

    @Suppress("UNUSED")
    @JvmStatic
    fun isModeOff(builder: PsiBuilder, @Suppress("UNUSED") level: Int, mode: String?): Boolean {
        return getParsingModes(builder).getInt(mode) == 0
    }

    @JvmStatic
    fun isLastIs(builder: PsiBuilder, @Suppress("UNUSED") level: Int, mode: String?): Boolean {
        return getParsingModesStack(builder).peek() == mode
    }

    @Suppress("UNUSED")
    @JvmStatic
    fun isLastNotIs(builder: PsiBuilder, @Suppress("UNUSED") level: Int, mode: String?): Boolean {
        return getParsingModesStack(builder).peek() != mode
    }

    @JvmStatic
    fun prevIsType(builder: PsiBuilder, @Suppress("UNUSED") level: Int): Boolean {
        var tokenBefore = builder.rawLookup(-1)
        if (tokenBefore == VlangTokenTypes.WS) {
            tokenBefore = builder.rawLookup(-2)
        }
        if (tokenBefore == ASSIGN) {
            return false
        }
        val marker = builder.latestDoneMarker
        val type = marker?.tokenType
        return type == ARRAY_TYPE || type == FIXED_SIZE_ARRAY_TYPE || type == MAP_TYPE || type == STRUCT_TYPE
    }

    @JvmStatic
    fun prevIsNotType(builder: PsiBuilder, level: Int): Boolean {
        return !prevIsType(builder, level)
    }

    @JvmStatic
    fun prevIsNotFunType(builder: PsiBuilder, @Suppress("UNUSED") level: Int): Boolean {
        val marker = builder.latestDoneMarker
        val type = marker?.tokenType
        return type !== FUNCTION_TYPE
    }

    @JvmStatic
    fun keyOrValueExpression(builder: PsiBuilder, level: Int): Boolean {
        val m = enter_section_(builder)
        var r = VlangParser.Expression(builder, level + 1, -1)

        if (!r) {
            r = VlangParser.LiteralValueExpression(builder, level + 1)
        }

        val type = if (r && builder.tokenType === COLON)
            KEY
        else
            VALUE
        exit_section_(builder, m, type, r)
        return r
    }

    @JvmStatic
    fun gtGt(builder: PsiBuilder, @Suppress("UNUSED") level: Int): Boolean {
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
    fun leftBracket(builder: PsiBuilder, @Suppress("UNUSED") level: Int): Boolean {
        val marker = builder.mark()
        if (!consumeToken(builder, LBRACK)) {
            marker.rollbackTo()
            return false
        }
        // before [ should not be whitespace
        if (builder.rawLookup(-2) == VlangTokenTypes.WS) {
            marker.rollbackTo()
            return false
        }
        marker.collapse(LBRACK)
        return true
    }

    @JvmStatic
    fun shiftLeft(builder: PsiBuilder, level: Int): Boolean {
        if (isModeOn(builder, level, "noShiftLeft")) {
            return false
        }

        val marker = builder.mark()
        if (!consumeToken(builder, SHIFT_LEFT)) {
            marker.rollbackTo()
            return false
        }
        marker.collapse(SHIFT_LEFT)
        return true
    }

    @JvmStatic
    fun gtGtGt(builder: PsiBuilder, @Suppress("UNUSED") level: Int): Boolean {
        val marker = builder.mark()
        if (!consumeToken(builder, GREATER)) {
            marker.rollbackTo()
            return false
        }
        if (!consumeToken(builder, GREATER)) {
            marker.rollbackTo()
            return false
        }
        if (!consumeToken(builder, GREATER)) {
            marker.rollbackTo()
            return false
        }
        marker.collapse(UNSIGNED_SHIFT_RIGHT)
        return true
    }

    @JvmStatic
    fun remapToIdentifier(builder: PsiBuilder, @Suppress("UNUSED") level: Int): Boolean {
        if (builder.tokenType == IDENTIFIER) {
            return true
        }

        val currentToken = builder.tokenType
        val nextToken = builder.lookAhead(1)

        // If mode checkForAccessModifiers is set, and it is access modifier ( pub: pub mut: __global: )
        // than skip remapping
          if (isModeOn(builder, level, "checkForAccessModifiers") && ((currentToken in arrayOf(PUB, MUT, BUILTIN_GLOBAL) && nextToken == COLON)
            || (currentToken in arrayOf(PUB, MUT) && nextToken in arrayOf(PUB, MUT) && builder.lookAhead(2) == COLON))) {
            return false
        }

        if (currentToken in VlangTokenTypes.KEYWORDS.types || currentToken in VlangTokenTypes.BOOL_LITERALS.types) {
            builder.remapCurrentToken(IDENTIFIER)
            return true
        }
        return false
    }

//    @JvmStatic
//    fun identOrKeyword(builder: PsiBuilder, @Suppress("UNUSED") level: Int): Boolean {
//        val currentToken = builder.tokenType ?: return false
//        val m = builder.mark()
//        var r = false
//        if (currentToken in VlangTokenTypes.KEYWORDS.types || currentToken in VlangTokenTypes.BOOL_LITERALS.types) {
//            r = consumeToken(builder, currentToken)
//            m.collapse(IDENTIFIER)
//        } else {
//            m.rollbackTo()
//        }
//        return r
//    }

    @JvmStatic
    fun callExpr(builder: PsiBuilder, level: Int): Boolean {
        val m = builder.latestDoneMarker
        if (m != null) {
            val text = builder.originalText
            val identifier = text.subSequence(m.startOffset, m.endOffset).toString()
            if (identifier == "json.decode") {
                return VlangParser.JsonCallExpr(builder, level + 1)
            }
        }

        return VlangParser.CallExpr(builder, level + 1)
    }

    @JvmStatic
    fun callExprWithPropagate(builder: PsiBuilder, level: Int): Boolean {
        val m = builder.latestDoneMarker
        if (m != null) {
            val text = builder.originalText
            val identifier = text.subSequence(m.startOffset, m.endOffset).toString()
            if (identifier == "json.decode") {
                return VlangParser.JsonCallExpr(builder, level + 1)
            }
        }

        return VlangParser.CallExprWithPropagate(builder, level + 1)
    }

    @JvmStatic
    fun typeOrExpression(builder: PsiBuilder, level: Int): Boolean {
        val m = builder.mark()
        var r = VlangParser.Type(builder, level + 1)
        if (r) {
            if (builder.tokenType === COMMA || builder.tokenType === LBRACE) {
                m.drop()
                return true
            }
        }

        m.rollbackTo()
        val m1 = builder.mark()
        r = VlangParser.Expression(builder, level + 1, -1)

        if (r) {
            if (builder.tokenType === COMMA || builder.tokenType === LBRACE) {
                m1.drop()
                return true
            }
        }
        m1.rollbackTo()

        enterMode(builder, level, "noBraces")
        val r1 = VlangParser.Expression(builder, level + 1, -1)
        exitMode(builder, level, "noBraces")
        return r1
    }

    @Suppress("UNUSED")
    @JvmStatic
    fun typeOrExpressionForSizeOf(builder: PsiBuilder, level: Int): Boolean {
        val m = builder.mark()
        var r = VlangParser.Type(builder, level + 1)
        if (r) {
            if (builder.tokenType === RPAREN) {
                m.drop()
                return true
            }
        }

        m.rollbackTo()
        val m1 = builder.mark()
        r = VlangParser.Expression(builder, level + 1, -1)

        if (r) {
            if (builder.tokenType === RPAREN) {
                m1.drop()
                return true
            }
        }
        m1.rollbackTo()

        return r
    }

    private val identifierRegex = Regex("[a-zA-Z0-9_.]*")

    @JvmStatic
    fun checkNoColonIfMap(builder: PsiBuilder, level: Int): Boolean {
        if (!isLastIs(builder, level, "MAP_KEY_VALUE") || builder.latestDoneMarker?.tokenType == IDENTIFIER) {
            return true
        }

        val text = builder.originalText.substring(
            builder.latestDoneMarker?.startOffset ?: 0,
            builder.latestDoneMarker?.endOffset ?: 1
        )
        if (identifierRegex.matches(text)) {
            return true
        }

        return !consumeToken(builder, COLON)
    }

    @JvmStatic
    fun braceRuleMarker(builder: PsiBuilder, level: Int): Boolean {
        return !isLastIs(builder, level, "noBraces")
    }

    @JvmStatic
    fun beforeBlockExpression(builder: PsiBuilder, level: Int): Boolean {
        val m = builder.mark()
        val r = VlangParser.Expression(builder, level + 1, -1)
        if (r && nextTokenIs(builder, LBRACE)) {
            // So the parsing can continue
            m.drop()
            return true
        }

        m.rollbackTo()
        (builder as Builder).state.currentFrame.errorReportedAt = -1

        // Otherwise, we try to parse, but without executing the rules that end with {}
        enterMode(builder, level, "noBraces")
        val r1 = VlangParser.Expression(builder, level + 1, -1)
        exitMode(builder, level, "noBraces")
        return r1
    }

    @JvmStatic
    fun enterMode(builder: PsiBuilder, @Suppress("UNUSED") level: Int, mode: String): Boolean {
        val flags = getParsingModes(builder)
        flags.addTo(mode, 1)
        val stack = getParsingModesStack(builder)
        stack.push(mode)
        return true
    }

    private fun exitMode(builder: PsiBuilder, @Suppress("UNUSED") level: Int, mode: String, safe: Boolean, all: Boolean = false): Boolean {
        val flags = getParsingModes(builder)
        val count = flags.getInt(mode)
        if (count == 1) {
            flags.removeInt(mode)
        } else if (count > 1) {
            if (all) {
                flags.removeInt(mode)
            } else {
                flags[mode] = count - 1
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

    @Suppress("UNUSED")
    @JvmStatic
    fun exitAllModeSafe(builder: PsiBuilder, level: Int, mode: String): Boolean {
        return exitMode(builder, level, mode, safe = true, all = true)
    }

    @JvmStatic
    fun endOfLimit(builder: PsiBuilder, @Suppress("UNUSED") level: Int): Boolean {
        val tokenText = builder.tokenText
        return !(tokenText == "limit" || tokenText == "asc" || tokenText == "desc")
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