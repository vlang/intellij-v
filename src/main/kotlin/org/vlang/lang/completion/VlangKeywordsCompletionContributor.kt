package org.vlang.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.vlang.lang.VlangTypes
import org.vlang.lang.completion.VlangCompletionPatterns.identifier
import org.vlang.lang.completion.VlangCompletionPatterns.insideForStatement
import org.vlang.lang.completion.VlangCompletionPatterns.insideSqlStatement
import org.vlang.lang.completion.VlangCompletionPatterns.insideStruct
import org.vlang.lang.completion.VlangCompletionPatterns.topLevelPattern
import org.vlang.lang.completion.VlangCompletionUtil.KEYWORD_PRIORITY
import org.vlang.lang.psi.VlangLiteralValueExpression
import org.vlang.lang.psi.impl.VlangPsiImplUtil
import org.vlang.lang.sql.VlangSqlUtil

class VlangKeywordsCompletionContributor : CompletionContributor() {
    init {
        extend(CompletionType.BASIC, topLevelPattern(), TypeAliasCompletionProvider)
        extend(CompletionType.BASIC, topLevelPattern(), ConstCompletionProvider)
        extend(CompletionType.BASIC, topLevelPattern(), ImportKeywordCompletionProvider)
        extend(CompletionType.BASIC, identifier(), SelectCompletionProvider)
        extend(CompletionType.BASIC, identifier(), MatchCompletionProvider)
        extend(CompletionType.BASIC, identifier(), OrKeywordCompletionProvider)
        extend(CompletionType.BASIC, identifier(), AssertKeywordCompletionProvider)

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
            identifier(),
            PureBlockKeywordCompletionProvider("defer", "unsafe")
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
            KeywordsCompletionProvider(
                "else",
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
                "spawn",
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
                "dump",
                "sizeof",
                "typeof",
                "isreftype",
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
            SqlKeywordsCompletionProvider
        )
    }

    private object OrKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
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

    private object AssertKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
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

    private object SelectCompletionProvider : CompletionProvider<CompletionParameters>() {
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

    private object MatchCompletionProvider : CompletionProvider<CompletionParameters>() {
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

    private object TypeAliasCompletionProvider : CompletionProvider<CompletionParameters>() {
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

    private object ConstCompletionProvider : CompletionProvider<CompletionParameters>() {
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

    private object ImportKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            if (shouldSuppress(parameters, result)) return

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

    private object SqlKeywordsCompletionProvider : CompletionProvider<CompletionParameters>() {
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
                            .withInsertHandler(VlangCompletionUtil.TemplateStringInsertHandler("(\$END$)"))
                            .bold(), KEYWORD_PRIORITY.toDouble()
                    )
                )
            }
        }
    }

    companion object {
        fun shouldSuppress(parameters: CompletionParameters, result: CompletionResultSet): Boolean {
            val pos = parameters.position

            if (VlangCompletionUtil.shouldSuppressCompletion(pos)) {
                return true
            }

            if (pos.parentOfType<VlangLiteralValueExpression>() != null) {
                return true
            }

            if (VlangPsiImplUtil.prevDot(pos)) {
                return true
            }
            return false
        }
    }
}
