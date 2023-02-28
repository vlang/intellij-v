package org.vlang.ide.documentation

import com.intellij.lang.documentation.DocumentationSettings
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil
import io.ktor.util.*
import org.vlang.ide.colors.VlangColor

object DocumentationUtils {
    private fun loadKey(key: TextAttributesKey): TextAttributes =
        EditorColorsManager.getInstance().globalScheme.getAttributes(key)!!

    val asOperator = loadKey(VlangColor.OPERATOR.textAttributesKey)
    val asKeyword = loadKey(VlangColor.KEYWORD.textAttributesKey)
    val asIdentifier = loadKey(DefaultLanguageHighlighterColors.IDENTIFIER)
    val asModule = loadKey(VlangColor.MODULE.textAttributesKey)
    val asDeclaration = loadKey(VlangColor.FUNCTION.textAttributesKey)
    val asBuiltin = loadKey(VlangColor.BUILTIN_TYPE.textAttributesKey)
    val asType = loadKey(VlangColor.BUILTIN_TYPE.textAttributesKey)
    val asParameter = loadKey(VlangColor.PARAMETER.textAttributesKey)
    val asAttribute = loadKey(VlangColor.ATTRIBUTE.textAttributesKey)
    val asString = loadKey(VlangColor.STRING.textAttributesKey)
    val asStringInterpolation = loadKey(VlangColor.STRING_INTERPOLATION.textAttributesKey)
    val asNumber = loadKey(VlangColor.NUMBER.textAttributesKey)
    val asGeneric = loadKey(VlangColor.GENERIC_PARAMETER.textAttributesKey)
    val asField = loadKey(VlangColor.FIELD.textAttributesKey)
    val asComment = loadKey(VlangColor.LINE_COMMENT.textAttributesKey)

    @Suppress("UnstableApiUsage")
    fun StringBuilder.colorize(text: String, attrs: TextAttributes, noHtml: Boolean = false) {
        if (noHtml) {
            append(text)
            return
        }

        HtmlSyntaxInfoUtil.appendStyledSpan(
            this, attrs, text.escapeHTML(),
            DocumentationSettings.getHighlightingSaturation(false)
        )
    }

    fun StringBuilder.part(text: String?) {
        if (text.isNullOrEmpty()) {
            return
        }
        append(text)
        append(" ")
    }

    fun StringBuilder.part(text: String?, attrs: TextAttributes) {
        if (text == null) {
            return
        }
        colorize(text, attrs)
        append(" ")
    }

    fun StringBuilder.appendNotNull(text: String?) {
        if (text == null) {
            return
        }
        append(text)
    }

    fun StringBuilder.line(text: String?) {
        if (text == null) {
            return
        }
        append(text)
        append("\n")
    }

    fun colorize(text: String, attrs: TextAttributes, noHtml: Boolean = false): String {
        val sb = StringBuilder()
        sb.colorize(text, attrs, noHtml)
        return sb.toString()
    }
}
