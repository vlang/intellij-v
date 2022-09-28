package org.vlang.ide.documentation

import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.elementType
import io.ktor.util.*
import org.vlang.ide.documentation.DocumentationUtils.appendNotNull
import org.vlang.ide.documentation.DocumentationUtils.asAttribute
import org.vlang.ide.documentation.DocumentationUtils.asDeclaration
import org.vlang.ide.documentation.DocumentationUtils.asKeyword
import org.vlang.ide.documentation.DocumentationUtils.asNumber
import org.vlang.ide.documentation.DocumentationUtils.asParameter
import org.vlang.ide.documentation.DocumentationUtils.asString
import org.vlang.ide.documentation.DocumentationUtils.colorize
import org.vlang.ide.documentation.DocumentationUtils.line
import org.vlang.ide.documentation.DocumentationUtils.part
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.*

object DocumentationGenerator {
    fun VlangType.generateDoc(): String {
        when (this) {
            is VlangArrayOrSliceType -> return generateDoc()
            is VlangPointerType      -> return generateDoc()
            is VlangNullableType     -> return generateDoc()
            is VlangStructType       -> return generateDoc()
        }
        return colorize(text.escapeHTML(), asDeclaration)
    }

    fun VlangStructType.generateDoc(): String {
        return buildString {
            colorize(identifier?.text ?: "anon", asDeclaration)
        }
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
                            it.generateDoc()
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

    private fun VlangReceiver.generateMethodDoc(): String {
        return buildString {
            append("(")
            appendNotNull(varModifiers?.generateDoc())
            part(name, asDeclaration)
            appendNotNull(type.generateDoc())
            append(")")
        }
    }

    fun VlangFunctionOrMethodDeclaration.generateDoc(): String? {
        val parameters = getSignature()?.parameters ?: return null
        val returnType = getSignature()?.result

        return buildString {
            generateModuleName(containingFile)
            append(DocumentationMarkup.DEFINITION_START)
            line(getAttributes()?.generateDoc())
            generateSymbolVisibilityDoc(getSymbolVisibility())

            if (this@generateDoc is VlangMethodDeclaration) {
                part(receiver.generateMethodDoc())
            }

            part("fn", asKeyword)
            colorize(name ?: "anon", asDeclaration)
            part(parameters.generateDoc())
            append(returnType?.generateDoc() ?: DocumentationUtils.colorize("void", asDeclaration))
            append(DocumentationMarkup.DEFINITION_END)

            generateCommentsPart(this@generateDoc)
        }
    }

    fun VlangStructDeclaration.generateDoc(): String {
        return buildString {
            generateModuleName(containingFile)
            append(DocumentationMarkup.DEFINITION_START)
            line(attributes?.generateDoc())
            generateSymbolVisibilityDoc(getSymbolVisibility())

            part("struct", asKeyword)
            colorize(name, asDeclaration)
            append(DocumentationMarkup.DEFINITION_END)

            generateCommentsPart(this@generateDoc)
        }
    }

    fun VlangUnionDeclaration.generateDoc(): String {
        return buildString {
            generateModuleName(containingFile)
            append(DocumentationMarkup.DEFINITION_START)
            line(attributes?.generateDoc())
            generateSymbolVisibilityDoc(getSymbolVisibility())

            part("union", asKeyword)
            colorize(name, asDeclaration)
            append(DocumentationMarkup.DEFINITION_END)

            generateCommentsPart(this@generateDoc)
        }
    }

    fun VlangEnumDeclaration.generateDoc(): String {
        return buildString {
            generateModuleName(containingFile)
            append(DocumentationMarkup.DEFINITION_START)
            line(attributes?.generateDoc())
            generateSymbolVisibilityDoc(getSymbolVisibility())

            part("enum", asKeyword)
            colorize(name, asDeclaration)
            append(DocumentationMarkup.DEFINITION_END)

            generateCommentsPart(this@generateDoc)
        }
    }

    fun VlangInterfaceDeclaration.generateDoc(): String {
        return buildString {
            generateModuleName(containingFile)
            append(DocumentationMarkup.DEFINITION_START)
            line(attributes?.generateDoc())
            generateSymbolVisibilityDoc(getSymbolVisibility())

            part("interface", asKeyword)
            colorize(name, asDeclaration)
            append(DocumentationMarkup.DEFINITION_END)

            generateCommentsPart(this@generateDoc)
        }
    }

    fun VlangConstDefinition.generateDoc(): String {
        return buildString {
            generateModuleName(containingFile)
            append(DocumentationMarkup.DEFINITION_START)
            val parent = parent as? VlangConstDeclaration
            generateSymbolVisibilityDoc(parent?.symbolVisibility)

            part("const", asKeyword)
            part(name, asDeclaration)
            part("=")
            append(expression?.generateDoc() ?: "unknown")
            append(DocumentationMarkup.DEFINITION_END)

            generateCommentsPart(this@generateDoc)
        }
    }

    fun VlangVarDefinition.generateDoc(): String {
        return buildString {
            generateModuleName(containingFile)
            append(DocumentationMarkup.DEFINITION_START)
            val parent = parent as? VlangVarDeclaration
            val type = parent?.expressionList?.firstOrNull()?.getType(null) // TODO: support multi assign

            val modifiersDoc = varModifiers?.generateDoc()
            if (modifiersDoc.isNullOrEmpty()) {
                part("var", asKeyword)
            } else {
                append(modifiersDoc)
            }
            part(name, asDeclaration)
            append(type?.generateDoc() ?: DocumentationUtils.colorize("unknown", asDeclaration))
            append(DocumentationMarkup.DEFINITION_END)

            generateCommentsPart(this@generateDoc)
        }
    }

    fun VlangReceiver.generateDoc(): String {
        return buildString {
            generateModuleName(containingFile)
            append(DocumentationMarkup.DEFINITION_START)

            val modifiersDoc = varModifiers?.generateDoc()
            if (!modifiersDoc.isNullOrEmpty()) {
                append(modifiersDoc)
            }
            part("receiver", asKeyword)
            part(name, asDeclaration)
            append(type.generateDoc())
            append(DocumentationMarkup.DEFINITION_END)

            generateCommentsPart(this@generateDoc)
        }
    }

    fun VlangExpression.generateDoc(): String {
        return buildString {
            when (this@generateDoc) {
                is VlangStringLiteral -> {
                    colorize(text, asString)
                }

                is VlangLiteral       -> {
                    when (elementType) {
                        VlangTypes.TRUE  -> colorize(text, asKeyword)
                        VlangTypes.FALSE -> colorize(text, asKeyword)
                        VlangTypes.CHAR  -> colorize(text, asString)
                        else             -> colorize(text, asNumber)
                    }
                }

                else                  -> append(text.lines().firstOrNull()?.take(20) ?: "unknown")
            }
        }
    }

    private fun StringBuilder.generateSymbolVisibilityDoc(visibility: VlangSymbolVisibility?) {
        part(visibility?.generateDoc() ?: DocumentationUtils.colorize("private", asKeyword))
    }

    private fun StringBuilder.generateCommentsPart(element: PsiElement?) {
        val comments = CommentsConverter.toHtml(CommentsConverter.getCommentsForElement(element))
        if (comments.isNotEmpty()) {
            append(DocumentationMarkup.CONTENT_START)
            append(comments)
            append(DocumentationMarkup.CONTENT_END)
        }
    }

    private fun StringBuilder.generateModuleName(containingFile: PsiFile) {
        if (containingFile !is VlangFile) {
            return
        }

        val moduleName = containingFile.getModuleName()
        if (moduleName != null) {
            append(DocumentationMarkup.CONTENT_START)
            append(DocumentationMarkup.GRAYED_START)
            append("Module: ")
            append(DocumentationMarkup.GRAYED_END)
            append("<a href='#'>")
            append(moduleName)
            append("</a>")
            append(DocumentationMarkup.CONTENT_END)
        }
    }
}
