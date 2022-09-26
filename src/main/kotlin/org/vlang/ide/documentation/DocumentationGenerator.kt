package org.vlang.ide.documentation

import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import io.ktor.util.*
import org.vlang.ide.documentation.DocumentationUtils.appendNotNull
import org.vlang.ide.documentation.DocumentationUtils.asAttribute
import org.vlang.ide.documentation.DocumentationUtils.asDeclaration
import org.vlang.ide.documentation.DocumentationUtils.asKeyword
import org.vlang.ide.documentation.DocumentationUtils.asParameter
import org.vlang.ide.documentation.DocumentationUtils.colorize
import org.vlang.ide.documentation.DocumentationUtils.line
import org.vlang.ide.documentation.DocumentationUtils.part
import org.vlang.lang.psi.*

object DocumentationGenerator {
    fun VlangType.generateDoc(): String {
        when (this) {
            is VlangArrayOrSliceType -> return generateDoc()
            is VlangPointerType      -> return generateDoc()
            is VlangNullableType     -> return generateDoc()
        }
        return colorize(text.escapeHTML(), asDeclaration)
    }

    fun VlangArrayOrSliceType.generateDoc(): String {
        return buildString {
            append("[]")
            append(type?.generateDoc())
        }
    }

    fun VlangPointerType.generateDoc(): String {
        return buildString {
            append("&")
            append(type?.generateDoc())
        }
    }

    fun VlangNullableType.generateDoc(): String {
        return buildString {
            append("?")
            append(type?.generateDoc())
        }
    }

    fun VlangResult.generateDoc(): String {
        return when {
            parameters != null    -> {
                ""
            }

            type != null          -> {
                type!!.generateDoc()
            }

            typeListNoPin != null -> {
                buildString {
                    append("(")
                    append(
                        typeListNoPin!!.typeList.joinToString(", ") {
                            it.generateDoc() ?: "any"
                        }
                    )
                    append(")")
                }
            }

            else                  -> ""
        }
    }

    private fun VlangVarModifiers.generateDoc(noHtml: Boolean = false): String {
        var child = firstChild
        val sb = StringBuilder()
        while (child != null) {
            if (child is PsiWhiteSpace) {
                child = child.nextSibling
                continue
            }

            when (child.text) {
                "mut"    -> sb.colorize("mutable", asKeyword, noHtml)
                "shared" -> sb.colorize(child.text, asKeyword, noHtml)
            }
            sb.append(" ")
            child = child.nextSibling
        }

        return sb.toString()
    }

    private fun VlangSymbolVisibility.generateDoc(): String {
        val parts = mutableListOf<String>()
        if (pub != null) {
            parts.add(colorize("public", asKeyword))
        } else {
            parts.add(colorize("private", asKeyword))
        }
        if (builtinGlobal != null) {
            parts.add(colorize("builtin global", asKeyword))
        }

        return parts.joinToString(" ")
    }

    private fun VlangParameters.generateDoc(): String {
        val params = parameterDeclarationList.flatMap { decl -> decl.paramDefinitionList.map { it to decl.type } }

        if (params.isEmpty()) {
            return "()"
        }

        if (params.size == 1) {
            val (param, type) = params.first()

            return buildString {
                append("(")
                append(param.generateDoc(type))
                append(")")
            }
        }

        val modifierMaxWidth = params.mapNotNull { it.first.varModifiers?.generateDoc(noHtml = true)?.length }.maxOrNull() ?: 0
        val paramNameMaxWidth = params.maxOfOrNull { it.first.name.length } ?: 0

        return buildString {
            append("(")
            append("\n")
            append(
                params.joinToString(",\n") { (param, type) ->
                    buildString {
                        append("   ")
                        if (param.varModifiers != null) {
                            val modifiersRawLength = param.varModifiers?.generateDoc(noHtml = true)?.length ?: 0
                            val modifiers = param.varModifiers?.generateDoc()
                            append(modifiers)
                            append("".padEnd(modifierMaxWidth - modifiersRawLength))
                        }
                        colorize(param.name, asParameter)
                        append("".padEnd(paramNameMaxWidth - param.name.length))
                        append(" ")
                        append(type.generateDoc())
                    }
                }
            )
            append("\n")
            append(")")
        }
    }

    private fun VlangParamDefinition.generateDoc(type: VlangType): String {
        return buildString {
            if (varModifiers != null) {
                append(varModifiers?.generateDoc())
            }
            colorize(name, asParameter)
            append(" ")
            append(type.generateDoc())
        }
    }

    private fun VlangAttribute.generateDoc(): String {
        return colorize(text, asAttribute)
    }

    private fun VlangAttributes.generateDoc(): String {
        return attributeList.joinToString("\n") { attr ->
            attr.generateDoc()
        }
    }

    private fun VlangReceiver.generateDoc(): String {
        return buildString {
            append("(")
            appendNotNull(varModifiers?.generateDoc())
            part(name, asDeclaration)
            appendNotNull(type?.generateDoc())
            append(")")
        }
    }

    fun VlangFunctionOrMethodDeclaration.generateDoc(): String? {
        val containingFile = containingFile as? VlangFile ?: return null
        val parameters = getSignature()?.parameters ?: return null
        val returnType = getSignature()?.result

        val builder = StringBuilder()

        builder.apply {
            val moduleName = containingFile.getModuleName()
            if (moduleName != null) {
                append(DocumentationMarkup.CONTENT_START)
                append(DocumentationMarkup.GRAYED_START)
                append("<code>")
                append(moduleName)
                append("</code>")
                append(" module")
                append(DocumentationMarkup.GRAYED_END)
                append(DocumentationMarkup.CONTENT_END)
            }

            append(DocumentationMarkup.DEFINITION_START)
            line(getAttributes()?.generateDoc())
            part(getSymbolVisibility()?.generateDoc() ?: DocumentationUtils.colorize("private", asKeyword))

            if (this@generateDoc is VlangMethodDeclaration) {
                part(receiver.generateDoc())
            }

            part("fn", asKeyword)
            colorize(name ?: "anon", asDeclaration)
            part(parameters.generateDoc())
            append(returnType?.generateDoc() ?: DocumentationUtils.colorize("void", asDeclaration))
            append(DocumentationMarkup.DEFINITION_END)

            val comments = CommentsConverter.toHtml(getCommentsForElement(this@generateDoc))
            if (comments.isNotEmpty()) {
                append(DocumentationMarkup.CONTENT_START)
                append(comments)
                append(DocumentationMarkup.CONTENT_END)
            }
        }

        return builder.toString()
    }

    private fun getCommentsForElement(element: PsiElement?): List<PsiComment> {
        var comments = getCommentsInner(element)
        if (comments.isEmpty()) {
            if (element is VlangVarDefinition || element is VlangConstDefinition) {
                val parent = element.parent // spec
                comments = getCommentsInner(parent)
                if (comments.isEmpty() && parent != null) {
                    return getCommentsInner(parent.parent) // declaration
                }
            } else if (element is VlangTypeAliasDeclaration) {
                return getCommentsInner(element.parent) // type declaration
            }
        }
        return comments
    }

    private fun getCommentsInner(element: PsiElement?): List<PsiComment> {
        if (element == null) {
            return emptyList()
        }
        val result = mutableListOf<PsiComment>()
        var e: PsiElement?
        e = element.prevSibling
        while (e != null) {
            if (e is PsiWhiteSpace) {
                if (e.getText().contains("\n\n")) return result
                e = e.getPrevSibling()
                continue
            }
            if (e is PsiComment) {
                result.add(0, e)
            } else {
                return result
            }
            e = e.prevSibling
        }
        return result
    }
}
