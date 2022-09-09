package org.vlang.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.patterns.*
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.*
import org.vlang.lang.utils.LabelUtil

class VlangContextCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            onStatementBeginning(VlangTypes.IDENTIFIER)
                .withSuperParent(
                    3,
                    psiElement(VlangFieldInitializationKeyValueList::class.java),
                ),
            VlangStructFieldsCompletionProvider()
        )

        extend(
            CompletionType.BASIC,
            insideWithLabelStatement(VlangTypes.IDENTIFIER),
            LabelCompletionProvider()
        )
    }

    private class LabelCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            resultSet: CompletionResultSet,
        ) {
            val element = parameters.originalFile.findElementAt(parameters.offset)
                ?: return

            val labels = LabelUtil.collectContextLabelNames(element)

            labels.forEach {
                resultSet.addElement(
                    PrioritizedLookupElement.withPriority(
                        LookupElementBuilder.create(it), 25.0
                    )
                )
            }
        }
    }

    class VlangStructFieldsCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            resultSet: CompletionResultSet,
        ) {
            val element = parameters.originalFile.findElementAt(parameters.offset) ?: return
            val refs = element.parentOfType<VlangTypeInitExpr>()?.typeDecl?.typeReferenceExpressionList ?: return

            refs
                .mapNotNull { it.reference.resolve() }
                .filterIsInstance<VlangStructDeclaration>()
                .forEach { decl ->
                    val groups = decl.structType.fieldsGroupList
                    val fields = groups.flatMap { it.fieldDeclarationList }.flatMap { it.fieldNameList }

                    for (field in fields) {
                        resultSet.addElement(
                            PrioritizedLookupElement.withPriority(
                                LookupElementBuilder.create(field.getIdentifier().text)
                                    .withIcon(AllIcons.Nodes.Field),
                                25.0
                            )
                        )
                    }
                }
        }
    }

    private fun onStatementBeginning(vararg tokenTypes: IElementType): PsiElementPattern.Capture<PsiElement?> {
        return psiElement().withElementType(TokenSet.create(*tokenTypes))
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
}
