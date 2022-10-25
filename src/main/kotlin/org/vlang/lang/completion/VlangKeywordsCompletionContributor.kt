package org.vlang.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.patterns.*
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.vlang.lang.VlangTypes
import org.vlang.lang.completion.VlangCompletionUtil.KEYWORD_PRIORITY
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangPsiImplUtil
import org.vlang.lang.sql.VlangSqlUtil

class VlangKeywordsCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            topLevelPattern(),
            KeywordsCompletionProvider(
                "fn",
                "module",
                "pub",
                "static",
                "__global",
                needSpace = true
            )
        )
        extend(
            CompletionType.BASIC,
            topLevelPattern(),
            ClassLikeSymbolCompletionProvider(
                "struct",
                "enum",
                "union",
                "interface",
            )
        )
        extend(
            CompletionType.BASIC,
            topLevelPattern(),
            TypeAliasCompletionProvider()
        )
        extend(
            CompletionType.BASIC,
            topLevelPattern(),
            ConstCompletionProvider()
        )
        extend(
            CompletionType.BASIC,
            topLevelPattern(),
            ImportKeywordCompletionProvider()
        )
        extend(
            CompletionType.BASIC,
            identifier(),
            PureBlockKeywordCompletionProvider("defer", "unsafe")
        )
        extend(
            CompletionType.BASIC,
            identifier(),
            SelectCompletionProvider()
        )
        extend(
            CompletionType.BASIC,
            identifier(),
            MatchCompletionProvider()
        )
        extend(
            CompletionType.BASIC,
            identifier(),
            OrKeywordCompletionProvider()
        )
        extend(
            CompletionType.BASIC,
            identifier(),
            AssertKeywordCompletionProvider()
        )
        extend(
            CompletionType.BASIC,
            insideStruct(),
            StructKeywordsCompletionProvider("pub", "mut", "shared")
        )
        extend(
            CompletionType.BASIC,
            identifier(),
            ConditionBlockKeywordCompletionProvider("if", "lock", "rlock", "sql")
        )
        extend(
            CompletionType.BASIC,
            identifier(),
            ReturnKeywordCompletionProvider()
        )
        extend(
            CompletionType.BASIC,
            identifier(),
            KeywordsCompletionProvider(
                "else",
                "isreftype",
                "atomic",
                "for",
                "mut",
                "shared",
                "volatile",
                needSpace = true,
            )
        )
        extend(
            CompletionType.BASIC,
            identifier(),
            CompletionAfterKeywordsCompletionProvider(
                "as",
                "go",
                "in",
                "is",
                "goto",
            )
        )
        extend(
            CompletionType.BASIC,
            identifier(),
            KeywordsCompletionProvider(
                "none",
                "true",
                "false",
                "nil",
                "static",
                needSpace = false,
            )
        )
        extend(
            CompletionType.BASIC,
            identifier(),
            FunctionsLikeCompletionProvider(
                "sizeof",
                "typeof",
                "__offsetof",
            )
        )
        extend(
            CompletionType.BASIC,
            insideForStatement(VlangTypes.IDENTIFIER),
            KeywordsCompletionProvider("continue", "break")
        )
        extend(
            CompletionType.BASIC,
            insideSqlStatement(VlangTypes.IDENTIFIER),
            SqlKeywordsCompletionProvider()
        )
    }

    private inner class OrKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            if (shouldSuppress(parameters, result)) return

            result.addElement(
                PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create("or")
                        .withTailText(" {...}")
                        .withInsertHandler(VlangCompletionUtil.StringInsertHandler(" {  }", 3))
                        .bold(), KEYWORD_PRIORITY.toDouble()
                )
            )

            result.addElement(
                PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create("or")
                        .withTailText(" { panic(err) }")
                        .withInsertHandler(
                            VlangCompletionUtil.TemplateStringInsertHandler(
                                " { panic(\$err$) }", false,
                                "err" to ConstantNode("err")
                            )
                        )
                        .bold(),
                    KEYWORD_PRIORITY.toDouble()
                )
            )
        }
    }

    private inner class AssertKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            if (shouldSuppress(parameters, result)) return

            result.addElement(
                PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create("assert")
                        .withTailText(" expr")
                        .withInsertHandler { ctx, item ->
                            VlangCompletionUtil.StringInsertHandler(" ", 1).handleInsert(ctx, item)
                            VlangCompletionUtil.showCompletion(ctx.editor)
                        }
                        .bold(), KEYWORD_PRIORITY.toDouble()
                )
            )

            result.addElement(
                PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create("assert")
                        .withTailText(" expr, 'message'")
                        .withInsertHandler(
                            VlangCompletionUtil.TemplateStringInsertHandler(
                                " \$expr$, '\$message$'", false,
                                "expr" to ConstantNode("expr"),
                                "message" to ConstantNode("message"),
                            )
                        )
                        .bold(),
                    KEYWORD_PRIORITY.toDouble()
                )
            )
        }
    }

    private inner class StructKeywordsCompletionProvider(private vararg val keywords: String) : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            if (shouldSuppress(parameters, result)) return

            result.addAllElements(
                keywords.map {
                    PrioritizedLookupElement.withPriority(
                        LookupElementBuilder.create(it)
                            .withInsertHandler(VlangCompletionUtil.StringInsertHandler(" ", 1))
                            .bold(), KEYWORD_PRIORITY.toDouble()
                    )
                }
            )
        }
    }

    private inner class ReturnKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            if (shouldSuppress(parameters, result)) return

            val signatureOwner = parameters.position.parentOfType<VlangSignatureOwner>() ?: return

            val resultType = signatureOwner.getSignature()?.result

            result.addElement(
                PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create("return")
                        .withInsertHandler { ctx, item ->
                            if (resultType != null) {
                                VlangCompletionUtil.StringInsertHandler(" ", 1).handleInsert(ctx, item)
                                VlangCompletionUtil.showCompletion(ctx.editor)
                            }
                        }
                        .bold(), KEYWORD_PRIORITY.toDouble()
                )
            )
        }
    }

    private inner class PureBlockKeywordCompletionProvider(private vararg val keywords: String) :
        CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            if (shouldSuppress(parameters, result)) return

            result.addAllElements(
                keywords.map {
                    PrioritizedLookupElement.withPriority(
                        LookupElementBuilder.create(it)
                            .withTailText(" {...}")
                            .withInsertHandler(VlangCompletionUtil.StringInsertHandler(" {  }", 3))
                            .bold(), KEYWORD_PRIORITY.toDouble()
                    )
                }
            )
        }
    }

    private inner class SelectCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            if (shouldSuppress(parameters, result)) return

            result.addElement(
                PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create("select")
                        .withTailText(" {...}")
                        .withInsertHandler(
                            VlangCompletionUtil.TemplateStringInsertHandler(
                                " {\n \$expr$ { \$END$ }\n}", true,
                                "expr" to ConstantNode("expr"),
                            ),
                        )
                        .bold(), KEYWORD_PRIORITY.toDouble()
                )
            )
        }
    }

    private inner class MatchCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            if (shouldSuppress(parameters, result)) return

            result.addElement(
                PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create("match")
                        .withTailText(" {...}")
                        .withInsertHandler(
                            VlangCompletionUtil.TemplateStringInsertHandler(
                                " \$expr$ {\n \$cond$ { \$END$ }\n}", true,
                                "expr" to ConstantNode("expr"),
                                "cond" to ConstantNode("cond"),
                            ),
                        )
                        .bold(), KEYWORD_PRIORITY.toDouble()
                )
            )
        }
    }

    private inner class ConditionBlockKeywordCompletionProvider(private vararg val keywords: String) :
        CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            if (shouldSuppress(parameters, result)) return

            result.addAllElements(
                keywords.map {
                    PrioritizedLookupElement.withPriority(
                        LookupElementBuilder.create(it)
                            .withTailText(" expr {...}")
                            .withInsertHandler(
                                VlangCompletionUtil.TemplateStringInsertHandler(
                                    " \$expr$ {\n\$END$\n}", true,
                                    "expr" to ConstantNode("expr")
                                )
                            )
                            .bold(), KEYWORD_PRIORITY.toDouble()
                    )
                }
            )
        }
    }

    private inner class ClassLikeSymbolCompletionProvider(private vararg val keywords: String) :
        CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            if (shouldSuppress(parameters, result)) return

            result.addAllElements(
                keywords.map {
                    PrioritizedLookupElement.withPriority(
                        LookupElementBuilder.create(it)
                            .withTailText(" Name {...}")
                            .withInsertHandler(
                                VlangCompletionUtil.TemplateStringInsertHandler(
                                    " \$name$ {\n\$END$\n}", true,
                                    "name" to ConstantNode("Name")
                                )
                            )
                            .bold(), KEYWORD_PRIORITY.toDouble()
                    )
                }
            )
        }
    }

    private inner class TypeAliasCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            if (shouldSuppress(parameters, result)) return

            result.addElement(
                PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create("type")
                        .withTailText(" Name = type")
                        .withInsertHandler(
                            VlangCompletionUtil.TemplateStringInsertHandler(
                                " \$name$ = \$type$", true,
                                "name" to ConstantNode("Name"),
                                "type" to ConstantNode("int"),
                            )
                        )
                        .bold(), KEYWORD_PRIORITY.toDouble()
                )
            )
        }
    }

    private inner class ConstCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            if (shouldSuppress(parameters, result)) return

            result.addElement(
                PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create("const")
                        .withTailText(" name = value")
                        .withInsertHandler(
                            VlangCompletionUtil.TemplateStringInsertHandler(
                                " \$name$ = \$value$", true,
                                "name" to ConstantNode("name"),
                                "value" to ConstantNode("0"),
                            )
                        )
                        .bold(), KEYWORD_PRIORITY.toDouble()
                )
            )
        }
    }

    private class ImportKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            result.addElement(
                PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create("import")
                        .withInsertHandler { ctx, _ ->
                            val document = ctx.document
                            val editor = ctx.editor
                            val offset = editor.caretModel.offset
                            document.insertString(offset, " ")
                            editor.caretModel.moveToOffset(offset + 1)

                            VlangCompletionUtil.showCompletion(editor)
                        }
                        .bold(), KEYWORD_PRIORITY.toDouble()
                )
            )
        }
    }

    private inner class KeywordsCompletionProvider(
        private vararg val keywords: String,
        private val needSpace: Boolean = false,
    ) : CompletionProvider<CompletionParameters>() {

        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet,
        ) {
            if (shouldSuppress(parameters, result)) return

            for (keyword in keywords) {
                result.addElement(
                    PrioritizedLookupElement.withPriority(
                        LookupElementBuilder.create(keyword)
                            .withInsertHandler(VlangCompletionUtil.StringInsertHandler(" ", 1, needSpace))
                            .bold(), KEYWORD_PRIORITY.toDouble()
                    )
                )
            }
        }
    }

    private inner class SqlKeywordsCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet,
        ) {
            for (keyword in VlangSqlUtil.sqlKeywords) {
                result.addElement(
                    PrioritizedLookupElement.withPriority(
                        LookupElementBuilder.create(keyword)
                            .withInsertHandler { ctx, item ->
                                VlangCompletionUtil.StringInsertHandler(" ", 1).handleInsert(ctx, item)
                                VlangCompletionUtil.showCompletion(ctx.editor)
                            }
                            .bold(), KEYWORD_PRIORITY.toDouble()
                    )
                )
            }
        }
    }

    private inner class CompletionAfterKeywordsCompletionProvider(private vararg val keywords: String) :
        CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet,
        ) {
            if (shouldSuppress(parameters, result)) return

            for (keyword in keywords) {
                result.addElement(
                    PrioritizedLookupElement.withPriority(
                        LookupElementBuilder.create(keyword)
                            .withInsertHandler { ctx, item ->
                                VlangCompletionUtil.StringInsertHandler(" ", 1).handleInsert(ctx, item)
                                VlangCompletionUtil.showCompletion(ctx.editor)
                            }
                            .bold(), KEYWORD_PRIORITY.toDouble()
                    )
                )
            }
        }
    }

    private inner class FunctionsLikeCompletionProvider(private vararg val keywords: String) : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet,
        ) {
            if (shouldSuppress(parameters, result)) return

            for (keyword in keywords) {
                result.addElement(
                    PrioritizedLookupElement.withPriority(
                        LookupElementBuilder.create(keyword)
                            .withTailText("()")
                            .withInsertHandler(VlangCompletionUtil.StringInsertHandler("()", 1))
                            .bold(), KEYWORD_PRIORITY.toDouble()
                    )
                )
            }
        }
    }

    private fun insideForStatement(tokenType: IElementType): ElementPattern<out PsiElement?> {
        return insideBlockPattern(tokenType)
            .inside(
                false, psiElement(VlangForStatement::class.java),
                psiElement(VlangFunctionDeclaration::class.java)
            ).andNot(
                insideWithLabelStatement(tokenType)
            )
    }

    private fun insideSqlStatement(tokenType: IElementType): ElementPattern<out PsiElement?> {
        return onStatementBeginning(tokenType)
            .inside(
                false, psiElement(VlangSqlExpression::class.java),
                psiElement(VlangFunctionDeclaration::class.java)
            )
    }

    private fun insideWithLabelStatement(tokenType: IElementType): ElementPattern<out PsiElement?> {
        return onStatementBeginning(tokenType)
            .inside(
                false,
                StandardPatterns.or(
                    psiElement(VlangTypes.CONTINUE_STATEMENT),
                    psiElement(VlangTypes.BREAK_STATEMENT),
                    psiElement(VlangTypes.GOTO_STATEMENT)
                ),
                psiElement(VlangFunctionDeclaration::class.java)
            )
    }

    private fun identifier() = insideBlockPattern(VlangTypes.IDENTIFIER)
        .andNot(
            insideWithLabelStatement(VlangTypes.IDENTIFIER)
        )

    private fun insideStruct() = onStatementBeginning(VlangTypes.IDENTIFIER)
        .inside(
            StandardPatterns.or(
                psiElement(VlangStructDeclaration::class.java),
                psiElement(VlangUnionDeclaration::class.java),
            )
        )

    private fun insideBlockPattern(tokenType: IElementType): PsiElementPattern.Capture<PsiElement?> {
        return onStatementBeginning(tokenType)
            .inside(VlangBlock::class.java)
    }

    private fun topLevelPattern(): PsiElementPattern.Capture<PsiElement?> {
        return onStatementBeginning(VlangTypes.IDENTIFIER).withParent(
            StandardPatterns.or(
                psiElement().withSuperParent(3, vlangFileWithModule()),
                psiElement().withSuperParent(3, VlangFile::class.java),
            )
        )
    }

    private fun vlangFileWithModule(): PsiFilePattern.Capture<VlangFile?> {
        val collection = StandardPatterns.collection(PsiElement::class.java)
        val packageIsFirst = collection.first(psiElement(VlangTypes.MODULE_CLAUSE))
        return PlatformPatterns.psiFile(VlangFile::class.java).withChildren(
            collection.filter(
                StandardPatterns.not(psiElement().whitespaceCommentEmptyOrError()),
                packageIsFirst
            )
        )
    }

    private fun onStatementBeginning(vararg tokenTypes: IElementType): PsiElementPattern.Capture<PsiElement?> {
        return psiElement().withElementType(TokenSet.create(*tokenTypes))
    }

    private fun shouldSuppress(parameters: CompletionParameters, result: CompletionResultSet): Boolean {
        val pos = parameters.position

        if (VlangCompletionUtil.shouldSuppressCompletion(pos)) {
            result.stopHere()
            return true
        }

        if (pos.parentOfType<VlangLiteralValueExpression>() != null) {
            result.stopHere()
            return true
        }

        if (VlangPsiImplUtil.prevDot(pos)) {
            result.stopHere()
            return true
        }
        return false
    }
}
