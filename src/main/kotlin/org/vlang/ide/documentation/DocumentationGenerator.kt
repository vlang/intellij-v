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
import org.vlang.ide.documentation.DocumentationUtils.asGeneric
import org.vlang.ide.documentation.DocumentationUtils.asIdentifier
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
import org.vlang.lang.doc.psi.VlangDocComment
import org.vlang.lang.psi.*
import org.vlang.lang.psi.types.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx

object DocumentationGenerator {
    fun VlangTypeEx.generateDoc(anchor: PsiElement): String {
        when (this) {
            is VlangAliasTypeEx            -> return this.generateDoc(anchor)
            is VlangArrayTypeEx            -> return this.generateDoc(anchor)
            is VlangMapTypeEx              -> return this.generateDoc(anchor)
            is VlangSharedTypeEx           -> return this.generateDoc(anchor)
            is VlangPointerTypeEx          -> return this.generateDoc(anchor)
            is VlangOptionTypeEx           -> return this.generateDoc(anchor)
            is VlangChannelTypeEx          -> return this.generateDoc(anchor)
            is VlangResultTypeEx           -> return this.generateDoc(anchor)
            is VlangUnionTypeEx            -> return this.generateDoc(anchor)
            is VlangStructTypeEx           -> return this.generateDoc(anchor)
            is VlangEnumTypeEx             -> return this.generateDoc(anchor)
            is VlangInterfaceTypeEx        -> return this.generateDoc(anchor)
            is VlangFunctionTypeEx         -> return this.generateDoc(anchor)
            is VlangGenericTypeEx          -> return this.generateDoc(anchor)
            is VlangGenericInstantiationEx -> return this.generateDoc(anchor)
        }
        return colorize(this.readableName(anchor).escapeHTML(), asType)
    }

    fun VlangAliasTypeEx.generateDoc(anchor: PsiElement): String {
        return buildString {
            append(generateFqnTypeDoc(readableName(anchor)))
        }
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
            appendNotNull(inner.generateDoc(anchor))
        }
    }

    fun VlangMapTypeEx.generateDoc(anchor: PsiElement): String {
        return buildString {
            colorize("map", asKeyword)
            append("[")
            appendNotNull(key.generateDoc(anchor))
            append("]")
            appendNotNull(value.generateDoc(anchor))
        }
    }

    fun VlangSharedTypeEx.generateDoc(anchor: PsiElement): String {
        return buildString {
            colorize("shared ", asKeyword)
            appendNotNull(inner.generateDoc(anchor))
        }
    }

    fun VlangPointerTypeEx.generateDoc(anchor: PsiElement): String {
        return buildString {
            append("&")
            appendNotNull(inner.generateDoc(anchor))
        }
    }

    fun VlangOptionTypeEx.generateDoc(anchor: PsiElement): String {
        return buildString {
            append("?")
            appendNotNull(inner?.generateDoc(anchor))
        }
    }

    fun VlangResultTypeEx.generateDoc(anchor: PsiElement): String {
        return buildString {
            append("!")
            appendNotNull(inner?.generateDoc(anchor))
        }
    }

    fun VlangChannelTypeEx.generateDoc(anchor: PsiElement): String {
        return buildString {
            colorize("chan ", asKeyword)
            appendNotNull(inner.generateDoc(anchor))
        }
    }

    fun VlangFunctionTypeEx.generateDoc(anchor: PsiElement): String {
        return buildString {
            colorize("fn", asKeyword)
            append(" (")
            params.forEachIndexed { index, param ->
                if (index > 0) {
                    append(", ")
                }
                appendNotNull(param.generateDoc(anchor))
            }
            append(")")
            if (result != null) {
                append(" ")
                appendNotNull(result.generateDoc(anchor))
            }
        }
    }

    fun VlangGenericTypeEx.generateDoc(anchor: PsiElement): String {
        return colorize(qualifiedName(), asGeneric)
    }

    fun VlangGenericInstantiationEx.generateDoc(anchor: PsiElement): String {
        return buildString {
            appendNotNull(inner.generateDoc(anchor))
            append("&lt;")
            specialization.forEachIndexed { index, param ->
                if (index > 0) {
                    append(", ")
                }
                appendNotNull(param.generateDoc(anchor))
            }
            append(">")
        }
    }

    private fun generateFqnTypeDoc(fqn: String): String {
        val parts = fqn.split(".")
        if (parts.size == 1) {
            return colorize(parts[0], asDeclaration)
        }

        return parts.subList(0, parts.size - 1).joinToString(".") + "." + colorize(parts.last(), asDeclaration)
    }

    fun VlangModuleClause.generateDoc(): String {
        return buildString {
            append(DocumentationMarkup.DEFINITION_START)
            part("module", asKeyword)
            colorize(name, asDeclaration)
            append(DocumentationMarkup.DEFINITION_END)
            generateModuleCommentsPart(this@generateDoc)
        }
    }

    private fun StringBuilder.generateModuleCommentsPart(element: VlangModuleClause) {
        val commentsList = CommentsConverter.getCommentsForModule(element)
        if (commentsList.any { it is VlangDocComment }) {
            append(DocumentationMarkup.CONTENT_START)
            for (comment in commentsList) {
                if (comment is VlangDocComment) {
                    append(comment.documentationAsHtml())
                    append("\n")
                }
            }
            append(DocumentationMarkup.CONTENT_END)
            return
        }

        val comments = CommentsConverter.toHtml(commentsList)
        if (comments.isNotEmpty()) {
            append(DocumentationMarkup.CONTENT_START)
            append(comments)
            append(DocumentationMarkup.CONTENT_END)
        }
    }

    fun VlangResult.generateDoc(): String {
        val type = type.toEx()
        if (type is VlangTupleTypeEx) {
            return buildString {
                append("(")
                append(
                    type.types.joinToString(", ") {
                        it.generateDoc(this@generateDoc)
                    }
                )
                append(")")
            }
        }

        return type.generateDoc(this)
    }

    private fun VlangVarModifiers?.generateDoc(noHtml: Boolean = false): String {
        if (this == null) {
            return ""
        }

        val modifiers = varModifierList
        val isVolatile = modifiers.any { it.volatile != null }
        val isStatic = modifiers.any { it.static != null }
        val isMutable = modifiers.any { it.mut != null }
        val isShared = modifiers.any { it.shared != null }

        val parts = mutableListOf<String>()

        if (isVolatile) {
            parts.add(colorize("volatile", asKeyword, noHtml))
        }
        if (isStatic) {
            parts.add(colorize("static", asKeyword, noHtml))
        }
        if (isMutable) {
            parts.add(colorize("mutable", asKeyword, noHtml))
        }
        if (isShared) {
            parts.add(colorize("shared", asKeyword, noHtml))
        }

        return parts.joinToString(" ")
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
                        part(modifiers)
                        val pad = modifierMaxWidth - modifiersRawLength
                        append("".padEnd(pad))
                        if (modifiers.isEmpty()) {
                            append(" ")
                        }
                        val name = param.name
                        if (name != null) {
                            colorize(name, asParameter)
                        }
                        val nameLength = name?.length ?: 0
                        append("".padEnd(paramNameMaxWidth - nameLength))
                        append(" ")
                        append(param.type.toEx().generateDoc(this@generateDoc))
                    }
                } + ","
            )
            append("\n")
            append(")")
        }
    }

    private fun VlangGenericParameters?.generateDoc(): String? {
        if (this == null) return null

        val parameters = this@generateDoc.genericParameterList.genericParameterList
        val names = parameters.mapNotNull { it.name }
        return names.joinToString(", ", prefix = "<".escapeHTML(), postfix = ">".escapeHTML()) {
            colorize(it, asGeneric)
        }
    }

    private fun VlangParamDefinition.generateDocForMethod(): String {
        return buildString {
            part(varModifiers.generateDoc())
            val name = name
            if (name != null) {
                colorize(name, asParameter)
                append(" ")
            }
            append(type.toEx().generateDoc(this@generateDocForMethod))
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
            part(varModifiers?.generateDoc())
            part(name, asParameter)
            appendNotNull(type.toEx().generateDoc(this@generateMethodDoc))
            append(")")
        }
    }

    fun VlangFunctionOrMethodDeclaration.generateDoc(): String? {
        val genericParameters = genericParameters
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
            appendNotNull(genericParameters.generateDoc())
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
            appendNotNull(structType.genericParameters.generateDoc())
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
            appendNotNull(interfaceType.genericParameters.generateDoc())
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

    fun VlangParamDefinition.generateDoc(originalElement: PsiElement?): String {
        return buildString {
            generateModuleName(containingFile)
            append(DocumentationMarkup.DEFINITION_START)

            // special case for 'it' in array methods
            if (name == null && originalElement?.text == "it") {
                part("variable", asKeyword)
                part("it", asDeclaration)

                val original = originalElement.parent as? VlangTypeOwner
                val originalType = original?.getType(null)
                append(
                    originalType?.generateDoc(this@generateDoc) ?: DocumentationUtils.colorize("unknown", asDeclaration)
                )
                append(DocumentationMarkup.DEFINITION_END)
                return@buildString
            }

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

            part(varModifiers?.generateDoc())

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
            colorize(name ?: "anon", asDeclaration)
            part(parameters.generateDoc())
            append(returnType?.generateDoc() ?: DocumentationUtils.colorize("void", asDeclaration))

            colorize(" of ", asIdentifier)
            colorize(owner.name ?: "anon", asDeclaration)

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
            append(owner.toEx().generateDoc(this@generateDoc))
            append(".")
            part(name, asDeclaration)
            append(type.toEx().generateDoc(this@generateDoc))

            val valueDoc = parent.defaultFieldValue?.expression?.generateDoc()
            if (valueDoc != null) {
                part(" =")
                append(valueDoc)
            }

            append(DocumentationMarkup.DEFINITION_END)
            generateCommentsPart(this@generateDoc)
        }
    }

    fun VlangTypeAliasDeclaration.generateDoc(): String {
        val genericParameters = aliasType?.genericParameters
        return buildString {
            generateModuleName(containingFile)
            append(DocumentationMarkup.DEFINITION_START)

            part("type alias", asKeyword)
            colorize(name, asDeclaration)
            appendNotNull(genericParameters.generateDoc())
            append(" ")

            val rightType = aliasType?.typeUnionList?.typeList?.firstOrNull()?.toEx()
            if (rightType != null) {
                part("=")
                append(rightType.generateDoc(this@generateDoc))
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
            append(owner.toEx().generateDoc(this@generateDoc))
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

            part(varModifiers?.generateDoc())
            part("receiver", asKeyword)
            part(name, asDeclaration)
            append(type.toEx().generateDoc(this@generateDoc))
            append(DocumentationMarkup.DEFINITION_END)

            generateCommentsPart(this@generateDoc)
        }
    }

    fun VlangGenericParameter.generateDoc() = buildString {
        generateModuleName(containingFile)
        append(DocumentationMarkup.DEFINITION_START)

        part("generic parameter", asKeyword)
        part(name, asDeclaration)
        append(DocumentationMarkup.DEFINITION_END)
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
        val commentsList = CommentsConverter.getCommentsForElement(element)
        if (commentsList.any { it is VlangDocComment }) {
            append(DocumentationMarkup.CONTENT_START)
            for (comment in commentsList) {
                if (comment is VlangDocComment) {
                    append(comment.documentationAsHtml())
                    append("\n")
                }
            }
            append(DocumentationMarkup.CONTENT_END)
            return
        }

        val comments = CommentsConverter.toHtml(commentsList)
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
