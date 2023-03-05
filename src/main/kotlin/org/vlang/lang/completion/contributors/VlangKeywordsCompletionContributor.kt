package org.vlang.lang.completion.contributors

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.util.ProcessingContext
import org.vlang.lang.completion.VlangCompletionPatterns.insideForStatement
import org.vlang.lang.completion.VlangCompletionPatterns.insideSqlStatement
import org.vlang.lang.completion.VlangCompletionPatterns.onCompileTimeIfElse
import org.vlang.lang.completion.VlangCompletionPatterns.onExpression
import org.vlang.lang.completion.VlangCompletionPatterns.onIfElse
import org.vlang.lang.completion.VlangCompletionPatterns.onMatchElse
import org.vlang.lang.completion.VlangCompletionPatterns.onStatement
import org.vlang.lang.completion.VlangCompletionPatterns.onTopLevel
import org.vlang.lang.completion.VlangCompletionPatterns.onType
import org.vlang.lang.completion.VlangCompletionUtil.CONTEXT_KEYWORD_PRIORITY
import org.vlang.lang.completion.VlangCompletionUtil.KEYWORD_PRIORITY
import org.vlang.lang.completion.VlangCompletionUtil.StringInsertHandler
import org.vlang.lang.completion.VlangCompletionUtil.TemplateStringInsertHandler
import org.vlang.lang.completion.VlangCompletionUtil.showCompletion
import org.vlang.lang.completion.VlangCompletionUtil.toVlangLookupElement
import org.vlang.lang.completion.VlangCompletionUtil.withPriority
import org.vlang.lang.completion.VlangLookupElementProperties
import org.vlang.lang.completion.sort.withVlangSorter
import org.vlang.lang.sql.VlangSqlUtil
import org.vlang.lang.utils.VlangUnsafeUtil
import org.vlang.utils.isTestFile

class VlangKeywordsCompletionContributor : CompletionContributor() {
    init {
        // Top Level
        extend(CompletionType.BASIC, onTopLevel(), TypeAliasCompletionProvider)
        extend(CompletionType.BASIC, onTopLevel(), ConstCompletionProvider)
        extend(CompletionType.BASIC, onTopLevel(), CompletionAfterContextKeywordsCompletionProvider("import"))

        extend(
            CompletionType.BASIC,
            onTopLevel(),
            ContextKeywordsCompletionProvider(
                "fn",
                "module",
                "pub",
                "static",
                "__global",
                needSpace = true,
            )
        )
        extend(
            CompletionType.BASIC,
            onTopLevel(),
            ClassLikeSymbolCompletionProvider(
                "struct",
                "enum",
                "union",
                "interface",
            )
        )

        // Expression
        extend(CompletionType.BASIC, onExpression(), SelectCompletionProvider)
        extend(CompletionType.BASIC, onExpression(), MatchCompletionProvider)
        extend(CompletionType.BASIC, onExpression(), OrKeywordCompletionProvider)

        extend(
            CompletionType.BASIC,
            onExpression(),
            PureBlockKeywordCompletionProvider("unsafe")
        )
        extend(
            CompletionType.BASIC,
            onExpression(),
            ConditionBlockKeywordCompletionProvider("if", "\$if", "lock", "rlock", "sql")
        )
        extend(
            CompletionType.BASIC,
            onExpression(),
            KeywordsCompletionProvider(
                "mut",
                "shared",
                "volatile",
                needSpace = true,
            )
        )
        extend(
            CompletionType.BASIC,
            onExpression(),
            CompletionAfterKeywordsCompletionProvider(
                "as",
                "go",
                "in",
                "is",
                "spawn",
            )
        )
        extend(
            CompletionType.BASIC,
            onExpression(),
            NilKeywordCompletionProvider
        )
        extend(
            CompletionType.BASIC,
            onExpression(),
            KeywordsCompletionProvider(
                "none",
                "true",
                "false",
                "static",
                needSpace = false,
            )
        )
        extend(
            CompletionType.BASIC,
            onExpression(),
            FunctionsLikeCompletionProvider(
                "dump",
                "sizeof",
                "typeof",
                "isreftype",
                "__offsetof",
            )
        )

        // Statement
        extend(CompletionType.BASIC, onStatement(), AssertKeywordCompletionProvider)

        extend(
            CompletionType.BASIC,
            onStatement(),
            PureBlockKeywordCompletionProvider("defer")
        )
        extend(
            CompletionType.BASIC,
            onStatement(),
            CompletionAfterKeywordsCompletionProvider("goto")
        )

        // On Type
        extend(
            CompletionType.BASIC,
            onType(),
            KeywordsCompletionProvider(
                "atomic",
                "thread", // context keyword
                needSpace = true,
            )
        )

        // Other
        extend(
            CompletionType.BASIC,
            onIfElse(),
            ElseIfKeywordCompletionProvider(),
        )
        extend(
            CompletionType.BASIC,
            onCompileTimeIfElse(),
            CompileTimeElseIfKeywordCompletionProvider(),
        )
        extend(
            CompletionType.BASIC,
            onMatchElse(),
            MatchElseKeywordCompletionProvider(),
        )
        extend(
            CompletionType.BASIC,
            insideForStatement(),
            ContextKeywordsCompletionProvider("continue", "break")
        )
        extend(
            CompletionType.BASIC,
            insideSqlStatement(),
            SqlKeywordsCompletionProvider
        )
    }

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        super.fillCompletionVariants(parameters, withVlangSorter(parameters, result))
    }

    private val elseElement = LookupElementBuilder.create("else")
        .bold()
        .withTailText(" {...}")
        .withInsertHandler(StringInsertHandler(" {  }", 3))
        .withPriority(KEYWORD_PRIORITY)

    private val compileTimeElseElement = LookupElementBuilder.create("\$else")
        .bold()
        .withTailText(" {...}")
        .withInsertHandler(StringInsertHandler(" {  }", 3))
        .withPriority(KEYWORD_PRIORITY)

    private object OrKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val orWithEmptyBlock = LookupElementBuilder.create("or")
                .withTailText(" {...}")
                .withInsertHandler(StringInsertHandler(" {  }", 3))
                .bold()
                .withPriority(KEYWORD_PRIORITY)

            val orWithPanic = LookupElementBuilder.create("or")
                .withTailText(" { panic(err) }")
                .withInsertHandler(
                    TemplateStringInsertHandler(
                        " { panic(\$err$) }", false,
                        "err" to ConstantNode("err")
                    )
                )
                .bold()
                .withPriority(KEYWORD_PRIORITY)

            result.addElement(orWithEmptyBlock)
            result.addElement(orWithPanic)
        }
    }

    private object AssertKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val insideTestFile = parameters.originalFile.virtualFile?.isTestFile ?: false
            val priority = if (insideTestFile) CONTEXT_KEYWORD_PRIORITY else KEYWORD_PRIORITY

            val plainAssert = LookupElementBuilder.create("assert")
                .withTailText(" expr")
                .withInsertHandler { ctx, item ->
                    StringInsertHandler(" ", 1).handleInsert(ctx, item)
                    showCompletion(ctx.editor)
                }
                .bold()
                .withPriority(priority)
                .toVlangLookupElement(VlangLookupElementProperties(
                    isContextElement = insideTestFile
                ))

            val assertWithMessage = LookupElementBuilder.create("assert")
                .withTailText(" expr, 'message'")
                .withInsertHandler(
                    TemplateStringInsertHandler(
                        " \$expr$, '\$message$'", false,
                        "expr" to ConstantNode("expr"),
                        "message" to ConstantNode("message"),
                    )
                )
                .bold()
                .withPriority(priority)
                .toVlangLookupElement(VlangLookupElementProperties(
                    isContextElement = insideTestFile
                ))

            result.addElement(plainAssert)
            result.addElement(assertWithMessage)
        }
    }

    private object SelectCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val selectElement = LookupElementBuilder.create("select")
                .withTailText(" {...}")
                .withInsertHandler(
                    TemplateStringInsertHandler(
                        " {\n \$expr$ { \$END$ }\n}", true,
                        "expr" to ConstantNode("expr"),
                    ),
                )
                .bold()
                .withPriority(KEYWORD_PRIORITY)

            result.addElement(selectElement)
        }
    }

    private object MatchCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val matchElement = LookupElementBuilder.create("match")
                .withTailText(" {...}")
                .withInsertHandler(
                    TemplateStringInsertHandler(
                        " \$expr$ {\n \$cond$ { \$END$ }\n}", true,
                        "expr" to ConstantNode("expr"),
                        "cond" to ConstantNode("cond"),
                    ),
                )
                .bold()
                .withPriority(KEYWORD_PRIORITY)

            result.addElement(matchElement)
        }
    }

    private object TypeAliasCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val typeElement = LookupElementBuilder.create("type")
                .withTailText(" Name = type")
                .withInsertHandler(
                    TemplateStringInsertHandler(
                        " \$name$ = \$type$", true,
                        "name" to ConstantNode("Name"),
                        "type" to ConstantNode("int"),
                    )
                )
                .bold()
                .withPriority(KEYWORD_PRIORITY)

            result.addElement(typeElement)
        }
    }

    private object ConstCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val constElement = LookupElementBuilder.create("const")
                .withTailText(" name = value")
                .withInsertHandler(
                    TemplateStringInsertHandler(
                        " \$name$ = \$value$", true,
                        "name" to ConstantNode("name"),
                        "value" to ConstantNode("0"),
                    )
                )
                .bold()
                .withPriority(KEYWORD_PRIORITY)

            result.addElement(constElement)
        }
    }

    private object SqlKeywordsCompletionProvider :
        CompletionAfterKeywordsCompletionProvider(*VlangSqlUtil.sqlKeywords.toTypedArray())

    private inner class ElseIfKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val elseIfElement = LookupElementBuilder.create("else if")
                .bold()
                .withTailText(" expr {...}")
                .withInsertHandler(
                    TemplateStringInsertHandler(" \$expr$ { \$END$ }", true, "expr" to ConstantNode("expr"))
                )
                .withPriority(KEYWORD_PRIORITY)

            result.addElement(elseElement)
            result.addElement(elseIfElement)
        }
    }

    private inner class CompileTimeElseIfKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val elseIfElement = LookupElementBuilder.create("\$else \$if")
                .bold()
                .withTailText(" expr {...}")
                .withInsertHandler(
                    TemplateStringInsertHandler(" \$expr$ { \$END$ }", true, "expr" to ConstantNode("expr"))
                )
                .withPriority(KEYWORD_PRIORITY)

            result.addElement(compileTimeElseElement)
            result.addElement(elseIfElement)
        }
    }

    private inner class MatchElseKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            result.addElement(elseElement)
        }
    }

    open class ContextKeywordsCompletionProvider(
        vararg keywords: String,
        needSpace: Boolean = false,
    ) : KeywordsCompletionProvider(
        *keywords,
        needSpace = needSpace,
        properties =  VlangLookupElementProperties(
            isContextElement = true,
        ),
    )

    open class KeywordsCompletionProvider(
        private vararg val keywords: String,
        private val needSpace: Boolean = false,
        private val properties: VlangLookupElementProperties = VlangLookupElementProperties(),
    ) : CompletionProvider<CompletionParameters>() {

        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet,
        ) {
            val elements = keywords.map { keyword ->
                LookupElementBuilder.create(keyword)
                    .withInsertHandler(StringInsertHandler(" ", 1, needSpace))
                    .bold()
                    .withPriority(KEYWORD_PRIORITY)
                    .toVlangLookupElement(properties)
            }
            result.addAllElements(elements)
        }
    }

    private inner class PureBlockKeywordCompletionProvider(private vararg val keywords: String) :
        CompletionProvider<CompletionParameters>() {

        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val elements = keywords.map { keyword ->
                LookupElementBuilder.create(keyword)
                    .withTailText(" {...}")
                    .withInsertHandler(StringInsertHandler(" {  }", 3))
                    .bold()
                    .withPriority(KEYWORD_PRIORITY)
            }

            result.addAllElements(elements)
        }
    }

    private object NilKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val insideUnsafe = VlangUnsafeUtil.insideUnsafe(parameters.position)

            val element = LookupElementBuilder.create("nil")
                .bold()
                .withInsertHandler { ctx, _ ->
                    if (insideUnsafe) return@withInsertHandler

                    val editor = ctx.editor
                    val document = ctx.document
                    document.insertString(ctx.startOffset, "unsafe { ")
                    document.insertString(ctx.tailOffset, " }")
                    editor.caretModel.moveToOffset(ctx.tailOffset)
                }
                .withPriority(KEYWORD_PRIORITY)

            result.addElement(element)
        }
    }

    private inner class ConditionBlockKeywordCompletionProvider(private vararg val keywords: String) :
        CompletionProvider<CompletionParameters>() {

        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val elements = keywords.map {
                LookupElementBuilder.create(it)
                    .withTailText(" expr {...}")
                    .withInsertHandler(
                        TemplateStringInsertHandler(
                            " \$expr$ {\n\$END$\n}", true,
                            "expr" to ConstantNode("expr")
                        )
                    )
                    .bold()
                    .withPriority(KEYWORD_PRIORITY)
            }

            result.addAllElements(elements)
        }
    }

    private inner class ClassLikeSymbolCompletionProvider(private vararg val keywords: String) :
        CompletionProvider<CompletionParameters>() {

        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val elements = keywords.map { keyword ->
                LookupElementBuilder.create(keyword)
                    .withTailText(" Name {...}")
                    .withInsertHandler(
                        TemplateStringInsertHandler(
                            " \$name$ {\n\$END$\n}", true,
                            "name" to ConstantNode("Name")
                        )
                    )
                    .bold()
                    .withPriority(KEYWORD_PRIORITY)
                    .toVlangLookupElement(
                        VlangLookupElementProperties(
                            isContextElement = true,
                        )
                    )
            }

            result.addAllElements(elements)
        }
    }

    class CompletionAfterContextKeywordsCompletionProvider(vararg keywords: String) :
        CompletionAfterKeywordsCompletionProvider(*keywords, properties = VlangLookupElementProperties(isContextElement = true))

    open class CompletionAfterKeywordsCompletionProvider(
        private vararg val keywords: String,
        private val properties: VlangLookupElementProperties = VlangLookupElementProperties(),
    ) :
        CompletionProvider<CompletionParameters>() {

        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val elements = keywords.map { keyword ->
                LookupElementBuilder.create(keyword)
                    .withInsertHandler { ctx, item ->
                        StringInsertHandler(" ", 1).handleInsert(ctx, item)
                        showCompletion(ctx.editor)
                    }
                    .bold()
                    .withPriority(KEYWORD_PRIORITY)
                    .toVlangLookupElement(properties)
            }

            result.addAllElements(elements)
        }
    }

    private inner class FunctionsLikeCompletionProvider(private vararg val keywords: String) :
        CompletionProvider<CompletionParameters>() {

        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val elements = keywords.map { keyword ->
                LookupElementBuilder.create(keyword)
                    .withTailText("()")
                    .withInsertHandler(TemplateStringInsertHandler("(\$END$)"))
                    .bold()
                    .withPriority(KEYWORD_PRIORITY)
            }

            result.addAllElements(elements)
        }
    }
}
