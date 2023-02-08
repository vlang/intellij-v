package org.vlang.ide.documentation

import com.intellij.codeInsight.documentation.DocumentationManagerUtil
import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import io.ktor.util.*
import org.vlang.ide.documentation.DocumentationUtils.appendNotNull
import org.vlang.ide.documentation.DocumentationUtils.asAttribute
import org.vlang.ide.documentation.DocumentationUtils.asComment
import org.vlang.ide.documentation.DocumentationUtils.asDeclaration
import org.vlang.ide.documentation.DocumentationUtils.asField
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
import org.vlang.lang.doc.psi.VlangDocComment
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangAttributeReference
import org.vlang.lang.psi.impl.VlangModule
import org.vlang.lang.psi.types.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.utils.parentNth

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
            is VlangAnonStructTypeEx       -> return this.generateDoc(anchor)
            is VlangStructTypeEx           -> return this.generateDoc(anchor)
            is VlangEnumTypeEx             -> return this.generateDoc(anchor)
            is VlangInterfaceTypeEx        -> return this.generateDoc(anchor)
            is VlangFunctionTypeEx         -> return this.generateDoc(anchor)
            is VlangGenericTypeEx          -> return this.generateDoc()
            is VlangGenericInstantiationEx -> return this.generateDoc(anchor)
            is VlangTupleTypeEx            -> return this.generateDoc(anchor)
            is VlangPrimitiveTypeEx        -> return this.generateDoc(anchor)
            is VlangSumTypeEx              -> return this.generateDoc(anchor)
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

    fun VlangSumTypeEx.generateDoc(anchor: PsiElement): String {
        return buildString {
            append(generateFqnTypeDoc(readableName(anchor)))
        }
    }

    fun VlangGenericTypeEx.generateDoc(): String {
        return colorize(qualifiedName(), asGeneric)
    }

    fun VlangGenericInstantiationEx.generateDoc(anchor: PsiElement): String {
        return buildString {
            appendNotNull(inner.generateDoc(anchor))
            append("[")
            specialization.forEachIndexed { index, param ->
                if (index > 0) {
                    append(", ")
                }
                appendNotNull(param.generateDoc(anchor))
            }
            append("]")
        }
    }

    fun VlangTupleTypeEx.generateDoc(anchor: PsiElement): String {
        return buildString {
            append("(")
            types.forEachIndexed { index, param ->
                if (index > 0) {
                    append(", ")
                }
                appendNotNull(param.generateDoc(anchor))
            }
            append(")")
        }
    }

    fun VlangAnonStructTypeEx.generateDoc(anchor: PsiElement): String {
        val fields = resolve()?.fieldList ?: return colorize("{}", asType)
        return buildString {
            colorize("struct", asKeyword)
            append(" {")
            appendLine()
            append(
                fields.joinToString("\n") { field ->
                    buildString {
                        append("   ")
                        part(field.name, asField)
                        append(field.getType(null)?.generateDoc(anchor) ?: "<unknown>")
                    }
                }
            )
            append("\n}")
        }
    }

    fun VlangPrimitiveTypeEx.generateDoc(anchor: PsiElement): String {
        return colorize(readableName(anchor), asDeclaration)
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
            generateModuleDocumentation(containingFile.containingDirectory, this@generateDoc)
        }
    }

    fun VlangModule.VlangPomTargetPsiElement.generateDoc(): String {
        val name = target.name
        val parts = name.split(".")
        val coloredName = if (parts.size == 1) {
            colorize(parts.first(), asDeclaration)
        } else {
            parts.subList(0, parts.lastIndex).joinToString(".") {
                colorize(it, asIdentifier, false)
            } + "." + colorize(parts.last(), asDeclaration)
        }

        return buildString {
            append(DocumentationMarkup.DEFINITION_START)
            part("module", asKeyword)
            append(coloredName)
            append(DocumentationMarkup.DEFINITION_END)

            val context = target.directory.children.find { it is VlangFile }
            if (context != null) {
                generateModuleDocumentation(target.directory, context.firstChild)
            }
        }
    }

    private fun StringBuilder.generateModuleDocumentation(directory: PsiDirectory, context: PsiElement) {
        val readme = directory.findFile("README.md")
        if (readme != null) {
            var text = readme.text
            if (text.startsWith("#")) {
                text = text.substring(text.indexOf('\n') + 1)
            }

            append(DocumentationMarkup.CONTENT_START)
            append(documentationAsHtml(text, context))
            append(DocumentationMarkup.CONTENT_END)
        }
    }

    fun VlangResult.generateDoc(): String {
        val type = type.toEx()
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
            parts.add(colorize("mut", asKeyword, noHtml))
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
                part("pub", asKeyword)
            } else {
                part("module private", asKeyword)
            }
            if (isMutable) {
                part("mut", asKeyword)
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

        val parameters = this@generateDoc.parameters
        val names = parameters.mapNotNull { it.name }
        return names.joinToString(", ", prefix = "[".escapeHTML(), postfix = "]".escapeHTML()) {
            colorize(it, asGeneric)
        }
    }

    private fun VlangParamDefinition.generateDocForMethod(): String {
        return buildString {
            part(varModifiers.generateDoc())
            val name = name
            if (name != null) {
                colorize(name, asIdentifier)
                append(" ")
            }
            append(type.toEx().generateDoc(this@generateDocForMethod))
        }
    }

    private fun VlangAttribute.generateDoc(): String {
        val res = attributeExpressionList.joinToString("; ", colorize("[", asAttribute), colorize("]", asAttribute)) { expression ->
            val firstChild = expression.firstChild
            var textValue: String
            when (firstChild) {
                is VlangPlainAttribute -> {
                    textValue = if (firstChild.attributeKey.attributeIdentifier != null) {
                        colorize(firstChild.attributeKey.text, asAttribute)
                    } else {
                        firstChild.attributeKey.expression?.generateDoc() ?: ""
                    }

                    if (firstChild.attributeValue == null) {
                        // [unsafe]
                    } else {
                        // [sql: '']
                        textValue += ": "
                        val value = firstChild.attributeValue!!.expression?.generateDoc()
                        textValue += value ?: firstChild.attributeValue!!.text
                    }
                    textValue
                }

                is VlangIfAttribute    -> {
                    // [if debug]
                    textValue = colorize("if", asKeyword)
                    textValue += " "
                    textValue += colorize(firstChild.expression?.text ?: "", asIdentifier)
                    textValue
                }

                else                   -> colorize(firstChild.text, asAttribute)
            }
        }

        return res
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
            } else if (this@generateDoc is VlangFunctionDeclaration && this@generateDoc.isCompileTime) {
                part("compile time", asKeyword)
            }

            part("fn", asKeyword)
            colorize(name ?: "anon", asDeclaration)
            appendNotNull(genericParameters.generateDoc())
            part(parameters.generateDoc())
            appendNotNull(returnType?.generateDoc())
            append(DocumentationMarkup.DEFINITION_END)

            generateCommentsPart(this@generateDoc)
        }
    }

    fun VlangStructDeclaration.generateDoc(): String {
        return buildString {
            generateModuleName(containingFile)
            append(DocumentationMarkup.DEFINITION_START)

            if (!isAttribute) {
                line(attributes?.generateDoc())
            }

            generateVisibilityPart(this@generateDoc)

            when {
                isAttribute -> part("attribute", asKeyword)
                isUnion     -> part("union", asKeyword)
                else        -> part("struct", asKeyword)
            }

            val name = if (isAttribute) {
                VlangAttributeReference.convertPascalCaseToSnakeCase(name)
            } else {
                name
            }

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

            if (isCompileTime) {
                part("compile time", asKeyword)
            }

            part("const", asKeyword)
            part(name, asIdentifier)
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
            part(name, asIdentifier)
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
                part("it", asIdentifier)

                val original = originalElement.parent as? VlangTypeOwner
                val originalType = original?.getType(null)
                append(
                    originalType?.generateDoc(this@generateDoc) ?: DocumentationUtils.colorize("unknown", asDeclaration)
                )
                append(DocumentationMarkup.DEFINITION_END)
                return@buildString
            }

            // special case for 'a' or 'b' in array sort method
            if (name == null && (originalElement?.text == "a" || originalElement?.text == "b")) {
                part("variable", asKeyword)
                part(originalElement.text, asIdentifier)

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
            part(name ?: "it", asIdentifier)
            if (isVariadic) {
                append("...")
            }
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
            part(name, asIdentifier)
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
            appendNotNull(returnType?.generateDoc())

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
            part(name, asField)
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
        val typeList = aliasType?.typeUnionList?.typeList?.map { it.toEx() } ?: emptyList()

        if (typeList.size > 1) {
            return buildString {
                generateModuleName(containingFile)
                append(DocumentationMarkup.DEFINITION_START)

                line(attributes?.generateDoc())
                generateVisibilityPart(this@generateDoc)

                part("sum type", asKeyword)
                colorize(name, asIdentifier)
                appendNotNull(genericParameters.generateDoc())
                append(" ")

                part("=")

                val types = typeList.joinToString(" | ") {
                    it.generateDoc(this@generateDoc)
                }
                append(types)

                append(DocumentationMarkup.DEFINITION_END)
                generateCommentsPart(this@generateDoc)
            }
        }

        return buildString {
            generateModuleName(containingFile)
            append(DocumentationMarkup.DEFINITION_START)

            line(attributes?.generateDoc())
            generateVisibilityPart(this@generateDoc)

            part("type alias", asKeyword)
            colorize(name, asIdentifier)
            appendNotNull(genericParameters.generateDoc())
            append(" ")

            val rightType = typeList.firstOrNull()
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
            part(name, asField)

            val valueDoc = parent?.expression?.generateDoc()
            if (valueDoc != null) {
                part("=")
                append(valueDoc)

                val constantValue = constantValue()
                if (constantValue != null) {
                    colorize(" // ", asComment)
                    part(constantValue.toString(), asComment)
                }
            } else {
                val constantValue = constantValue()
                if (constantValue != null) {
                    val isFlag = parent?.parentNth<VlangEnumDeclaration>(2)?.isFlag ?: false

                    part("=")
                    if (isFlag) {
                        part("0b" + constantValue.toString(2), asNumber)
                        colorize("// ", asComment)
                        part(constantValue.toString(), asComment)
                    } else {
                        part(constantValue.toString(), asNumber)
                    }
                }
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
            part(name, asIdentifier)
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
                    append(firstLine.take(20).escapeHTML())
                    if (lines.size > 1 || firstLine.length > 20) {
                        append("...")
                    }
                }
            }
        }
    }

    private fun StringBuilder.generateVisibilityPart(element: VlangNamedElement) {
        if (element.isPublic()) {
            part("pub", asKeyword)
        } else {
            part("")
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

            val dir = containingFile.containingDirectory!!
            val module = VlangModule.fromDirectory(dir)
            val moduleNameParts = module.name.split(".")

            var moduleNameAtIndex = ""
            for (moduleNamePart in moduleNameParts) {
                if (moduleNameAtIndex.isNotEmpty()) {
                    moduleNameAtIndex += "."
                    append(".")
                }
                moduleNameAtIndex += moduleNamePart
                DocumentationManagerUtil.createHyperlink(this, module.toPsi(), "#$moduleNameAtIndex", moduleNamePart, true)
            }

            append(DocumentationMarkup.CONTENT_END)
        }
    }
}
