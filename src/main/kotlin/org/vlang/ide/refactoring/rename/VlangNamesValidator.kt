package org.vlang.ide.refactoring.rename

import com.intellij.lang.refactoring.NamesValidator
import com.intellij.openapi.project.Project
import com.intellij.psi.tree.IElementType
import org.vlang.lang.VlangTypes
import org.vlang.lang.lexer.VlangLexer
import org.vlang.lang.psi.VlangTokenTypes

object VlangNamesValidator : NamesValidator {
    override fun isKeyword(name: String, project: Project?): Boolean {
        return VlangTokenTypes.KEYWORDS.contains(tokenType(name))
    }

    override fun isIdentifier(name: String, project: Project?): Boolean {
        return tokenType(name) == VlangTypes.IDENTIFIER
    }

    private fun tokenType(text: String): IElementType? {
        val lexer = VlangLexer()
        lexer.start(text)
        return if (lexer.tokenEnd == text.length) lexer.tokenType else null
    }

    fun isValidVariableName(name: String): Boolean = name.indexOfFirst { it.isUpperCase() } == -1
    fun isValidFunctionName(name: String): Boolean = name.indexOfFirst { it.isUpperCase() } == -1
    fun isValidTypeName(name: String): Boolean = name.first().isUpperCase()
}
