package org.vlang.ide.documentation

import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import io.ktor.util.*
import org.vlang.ide.documentation.DocumentationUtils.appendNotNull
import org.vlang.ide.documentation.DocumentationUtils.asAttribute
import org.vlang.ide.documentation.DocumentationUtils.asDeclaration
import org.vlang.ide.documentation.DocumentationUtils.asKeyword
import org.vlang.ide.documentation.DocumentationUtils.asNumber
import org.vlang.ide.documentation.DocumentationUtils.asParameter
import org.vlang.ide.documentation.DocumentationUtils.asString
import org.vlang.ide.documentation.DocumentationUtils.asType
import org.vlang.ide.documentation.DocumentationUtils.colorize
import org.vlang.ide.documentation.DocumentationUtils.line
import org.vlang.ide.documentation.DocumentationUtils.part
import org.vlang.lang.VlangTypes
import org.vlang.lang.completion.VlangCompletionUtil
import org.vlang.lang.psi.*
import org.vlang.lang.psi.types.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx

object DocumentationGenerator {
    fun VlangType.generateDoc(anchor: PsiElement): String {
        val typeEx = toEx()
        when (typeEx) {
            is VlangArrayTypeEx     -> return typeEx.generateDoc(anchor)
            is VlangMapTypeEx       -> return typeEx.generateDoc(anchor)
            is VlangSharedTypeEx    -> return typeEx.generateDoc(anchor)
            is VlangPointerTypeEx   -> return typeEx.generateDoc(anchor)
            is VlangOptionTypeEx    -> return typeEx.generateDoc(anchor)
            is VlangStructTypeEx    -> return typeEx.generateDoc(anchor)
            is VlangUnionTypeEx     -> return typeEx.generateDoc(anchor)
            is VlangEnumTypeEx      -> return typeEx.generateDoc(anchor)
            is VlangInterfaceTypeEx -> return typeEx.generateDoc(anchor)
        }
        return colorize(typeEx.readableName(anchor).escapeHTML(), asType)
    }

    fun VlangStructTypeEx.generateDoc(anchor: PsiElement): String {
        return buildString {
            append(generateFqnTypeDoc(readableName(anchor)))
        }
    }

    fun VlangUnionTypeEx.generateDoc(anchor: PsiElement): String {
        return buildString {
            append(generateFqnTypeDoc(readableName(anchor)))
        }
    }

    fun VlangEnumTypeEx.generateDoc(anchor: PsiElement): String {
        return buildString {
            append(generateFqnTypeDoc(readableName(anchor)))
        }
    }

    fun VlangInterfaceTypeEx.generateDoc(anchor: PsiElement): String {
        return buildString {
            append(generateFqnTypeDoc(readableName(anchor)))
        }
    }

    fun VlangArrayTypeEx.generateDoc(anchor: PsiElement): String {
        return buildString {
            append("[]")
            appendNotNull(inner?.raw()?.generateDoc(anchor))
        }
    }

    fun VlangMapTypeEx.generateDoc(anchor: PsiElement): String {
        return buildString {
            append("map[")
            appendNotNull(key.raw()?.generateDoc(anchor))
            append("]")
            appendNotNull(value.raw()?.generateDoc(anchor))
        }
    }

    fun VlangSharedTypeEx.generateDoc(anchor: PsiElement): String {
        return buildString {
            append("shared ")
            appendNotNull(inner?.raw()?.generateDoc(anchor))
        }
    }

    fun VlangPointerTypeEx.generateDoc(anchor: PsiElement): String {
        return buildString {
            append("&")
            appendNotNull(inner?.raw()?.generateDoc(anchor))
        }
    }

    fun VlangOptionTypeEx.generateDoc(anchor: PsiElement): String {
        return buildString {
            append("?")
            appendNotNull(inner?.raw()?.generateDoc(anchor))
        }
    }

    private fun generateFqnTypeDoc(fqn: String): String {
        val parts = fqn.split(".")
        if (parts.size == 1) {
            return colorize(parts[0], asDeclaration)
        }

        return parts.subList(0, parts.size - 1).joinToString(".") + "." + colorize(parts.last(), asDeclaration)
    }

    fun VlangResult.generateDoc(): String {
        val typeInner = type as? VlangTupleType
        if (typeInner != null) {
            return buildString {
                append("(")
                append(
                    typeInner.typeListNoPin.typeList.joinToString(", ") {
                        it.generateDoc(this@generateDoc)
                    }
                )
                append(")")
            }
        }

        return type.generateDoc(this)
    }

    private fun VlangVarModifiers?.generateDoc(short: Boolean = false, noHtml: Boolean = false): String {
        if (this == null) {
            return ""
        }

        val modifiers = varModifierList
        val isVolatile = modifiers.any { it.volatile != null }
        val isStatic = modifiers.any { it.static != null }
        val isMutable = modifiers.any { it.mut != null }
        val isShared = modifiers.any { it.shared != null }

        return buildString {
            if (isVolatile) {
                colorize("volatile", asKeyword, noHtml)
                append(" ")
            }
            if (isStatic) {
                colorize("static", asKeyword, noHtml)
                append(" ")
            }
            if (isMutable) {
                colorize(if (short) "mut" else "mutable", asKeyword, noHtml)
                append(" ")
            }
            if (isShared) {
                colorize("shared", asKeyword, noHtml)
                append(" ")
            }
        }.trim()
    }

    private fun VlangMemberModifiers?.generateDoc(element: VlangNamedElement): String {
        val isShared = this?.memberModifierList?.find { it.text == "shared" } != null

        val isMutable = (element as? VlangMutabilityOwner)?.isMutable() ?: false

        return buildString {
            if (element.isPublic()) {
                part("public", asKeyword)
            } else {
                part("module private", asKeyword)
            }
            if (isMutable) {
                part("mutable", asKeyword)
            }
            if (isShared) {
                part("shared", asKeyword)
            }
            if (element.isGlobal()) {
                part("global", asKeyword)
            }
        }
    }

    private fun VlangParameters.generateDoc(): String {
        val params = paramDefinitionList

        if (params.isEmpty()) {
            return "()"
        }

        if (params.size == 1) {
            val param = params.first()
            return buildString {
                append("(")
                append(param.generateDocForMethod())
                append(")")
            }
        }

        val modifierMaxWidth = params.mapNotNull { it.varModifiers.generateDoc(noHtml = true).length }.maxOrNull() ?: 0
        val paramNameMaxWidth = params.maxOfOrNull { it.name?.length ?: 0 } ?: 0

        return buildString {
            append("(")
            append("\n")
            append(
                params.joinToString(",\n") { param ->
                    buildString {
                        val modifiersRawLength = param.varModifiers.generateDoc(noHtml = true).length
                        val modifiers = param.varModifiers.generateDoc()

                        append("   ")
                        append(modifiers)
                        append("".padEnd(modifierMaxWidth - modifiersRawLength))
                        val name = param.name
                        if (name != null) {
                            colorize(name, asParameter)
                        }
                        val nameLength = name?.length ?: 0
                        append("".padEnd(paramNameMaxWidth - nameLength))
                        append(" ")
                        append(param.type.generateDoc(this@generateDoc))
                    }
                }
            )
            append("\n")
            append(")")
        }
    }

    private fun VlangParamDefinition.generateDocForMethod(): String {
        return buildString {
            part(varModifiers.generateDoc(short = true))
            val name = name
            if (name != null) {
                colorize(name, asParameter)
                append(" ")
            }
            append(type.generateDoc(this@generateDocForMethod))
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
            appendNotNull(type?.generateDoc(this@generateMethodDoc))
            append(")")
        }
    }

    fun VlangFunctionOrMethodDeclaration.generateDoc(): String? {
        val parameters = getSignature()?.parameters ?: return null
        val returnType = getSignature()?.result

        return buildString {
            generateModuleName(containingFile)
            append(DocumentationMarkup.DEFINITION_START)
            line(attributes?.generateDoc())
            generateVisibilityPart(this@generateDoc)

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

            generateVisibilityPart(this@generateDoc)

            part("struct", asKeyword)
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
            generateVisibilityPart(this@generateDoc)

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
            generateVisibilityPart(this@generateDoc)

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
            generateVisibilityPart(this@generateDoc)

            part("const", asKeyword)
            part(name, asDeclaration)
            part(getType(null)?.generateDoc(this@generateDoc) ?: DocumentationUtils.colorize("unknown", asDeclaration))
            part("=")
            append(expression?.generateDoc() ?: "unknown")
            append(DocumentationMarkup.DEFINITION_END)

            generateCommentsPart(this@generateDoc)
        }
    }

    fun VlangVarDefinition.generateDoc(original: PsiElement?): String {
        return buildString {
            generateModuleName(containingFile)
            append(DocumentationMarkup.DEFINITION_START)
            val type = getType(null)

            val modifiersDoc = varModifiers?.generateDoc()
            part(modifiersDoc)

            if (original != null && isCaptured(original)) {
                part("captured", asKeyword)
            }

            part("var", asKeyword)
            part(name, asDeclaration)
            append(type?.generateDoc(this@generateDoc) ?: DocumentationUtils.colorize("unknown", asDeclaration))
            append(DocumentationMarkup.DEFINITION_END)

            generateCommentsPart(this@generateDoc)
        }
    }

    fun VlangParamDefinition.generateDoc(): String {
        return buildString {
            generateModuleName(containingFile)
            append(DocumentationMarkup.DEFINITION_START)
            val type = getType(null)

            part(varModifiers.generateDoc())

            part("parameter", asKeyword)
            part(name ?: "it", asDeclaration)
            append(type?.generateDoc(this@generateDoc) ?: DocumentationUtils.colorize("unknown", asDeclaration))
            append(DocumentationMarkup.DEFINITION_END)

            generateCommentsPart(this@generateDoc)
        }
    }

    fun VlangGlobalVariableDefinition.generateDoc(): String {
        return buildString {
            generateModuleName(containingFile)
            append(DocumentationMarkup.DEFINITION_START)
            val type = getType(null)

            val modifiersDoc = varModifiers?.generateDoc()
            if (!modifiersDoc.isNullOrEmpty()) {
                append(modifiersDoc)
            }

            part("global var", asKeyword)
            part(name, asDeclaration)
            append(type?.generateDoc(this@generateDoc) ?: DocumentationUtils.colorize("unknown", asDeclaration))
            append(DocumentationMarkup.DEFINITION_END)

            generateCommentsPart(this@generateDoc.parent)
        }
    }

    fun VlangInterfaceMethodDefinition.generateDoc(): String {
        val parameters = getSignature().parameters
        val returnType = getSignature().result

        return buildString {
            generateModuleName(containingFile)
            append(DocumentationMarkup.DEFINITION_START)
            val parent = parent as? VlangInterfaceMethodDeclaration
            val parentGroup = parent?.parent as? VlangMembersGroup
            val owner = parent?.parentOfType<VlangNamedElement>()!!

            line(parent.attribute?.generateDoc())

            val modifiersDoc = parentGroup?.memberModifiers.generateDoc(this@generateDoc)
            append(modifiersDoc)

            part("interface method", asKeyword)
            colorize(owner.name ?: "anon", asDeclaration)
            append(".")
            colorize(name ?: "anon", asDeclaration)
            part(parameters.generateDoc())
            append(returnType?.generateDoc() ?: DocumentationUtils.colorize("void", asDeclaration))

            append(DocumentationMarkup.DEFINITION_END)
            generateCommentsPart(this@generateDoc)
        }
    }

    fun VlangFieldDefinition.generateDoc(): String {
        return buildString {
            generateModuleName(containingFile)
            append(DocumentationMarkup.DEFINITION_START)
            val parent = parent as? VlangFieldDeclaration
            val parentGroup = parent?.parent as? VlangFieldsGroup
            val owner = parent?.parentOfType<VlangType>()!!
            val type = parent.type

            line(parent.attribute?.generateDoc())

            val modifiersDoc = parentGroup?.memberModifiers.generateDoc(this@generateDoc)
            append(modifiersDoc)

            part("field", asKeyword)
            append(owner.generateDoc(this@generateDoc))
            append(".")
            part(name, asDeclaration)
            append(type?.generateDoc(this@generateDoc) ?: DocumentationUtils.colorize("unknown", asDeclaration))

            val valueDoc = parent.defaultFieldValue?.expression?.generateDoc()
            if (valueDoc != null) {
                part(" =")
                append(valueDoc)
            }

            append(DocumentationMarkup.DEFINITION_END)
            generateCommentsPart(this@generateDoc)
        }
    }

    fun VlangEnumFieldDefinition.generateDoc(): String {
        return buildString {
            generateModuleName(containingFile)
            append(DocumentationMarkup.DEFINITION_START)
            val parent = parent as? VlangEnumFieldDeclaration
            val owner = parentOfType<VlangType>()!!

            part("enum field", asKeyword)
            append(owner.generateDoc(this@generateDoc))
            append(".")
            part(name, asDeclaration)

            val valueDoc = parent?.expression?.generateDoc()
            if (valueDoc != null) {
                part("=")
                append(valueDoc)
            }

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
            append(type?.generateDoc(this@generateDoc))
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

                else                  -> {
                    val lines = text.lines()
                    if (lines.isEmpty()) {
                        append("no value")
                        return@buildString
                    }

                    val firstLine = lines.first()
                    append(firstLine.take(20))
                    if (lines.size > 1 || firstLine.length > 20) {
                        append("...")
                    }
                }
            }
        }
    }

    private fun StringBuilder.generateVisibilityPart(element: VlangNamedElement) {
        if (element.isPublic()) {
            part("public", asKeyword)
        } else {
            part("private", asKeyword)
        }
    }

    fun generateCompileTimeConstantDoc(element: VlangReferenceExpression): String? {
        val name = element.getIdentifier().text.removePrefix("@")
        val description = VlangCompletionUtil.compileTimeConstants[name] ?: return null
        return buildString {
            generateModuleName(element.containingFile)
            append(DocumentationMarkup.DEFINITION_START)
            part("compile-time constant", asKeyword)
            part(name, asDeclaration)
            append(DocumentationMarkup.DEFINITION_END)
            append(DocumentationMarkup.CONTENT_START)
            append(description)
            append(DocumentationMarkup.CONTENT_END)
        }
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
