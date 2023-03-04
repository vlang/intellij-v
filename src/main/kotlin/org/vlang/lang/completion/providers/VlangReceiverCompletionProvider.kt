package org.vlang.lang.completion.providers

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.vlang.lang.completion.VlangCompletionUtil
import org.vlang.lang.completion.VlangCompletionUtil.toVlangLookupElement
import org.vlang.lang.completion.VlangLookupElementProperties
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangElementFactory
import org.vlang.lang.psi.impl.VlangScopeProcessor
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.unwrapPointer

object VlangReceiverCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val set = VlangCompletionUtil.withCamelHumpPrefixMatcher(result)
        val pos = parameters.position
        val referenceExpression = pos.parentOfType<VlangReferenceExpression>() ?: return
        if (referenceExpression.getQualifier() != null) {
            return
        }

        val methodDeclaration = referenceExpression.parentOfType<VlangMethodDeclaration>() ?: return
        val element = referenceExpression.parentOfType<VlangElement>()
        if (element != null) {
            if (element.key == null || element.key != null && PsiTreeUtil.isAncestor(element.key, referenceExpression, true)) {
                return
            }
        }

        val receiver = methodDeclaration.receiver
        val receiverName = receiver.name ?: return
        val receiverType = receiver.type.toEx().unwrapPointer().name()

        val containingFile = pos.containingFile as VlangFile
        val moduleQualifiedName = containingFile.getModuleQualifiedName()
        val moduleName = containingFile.getModuleName() ?: return

        val text = """
            module aaa
            
            import $moduleQualifiedName
            
            fn foo() {
               $receiverName := $moduleName.$receiverType()
               $receiverName.${referenceExpression.getIdentifier().text}
            }
        """.trimIndent()

        val pseudoReferenceExpression = VlangElementFactory.createReference(pos.project, text).parent as VlangReferenceExpression
        val reference = pseudoReferenceExpression.reference

        reference.processResolveVariants(object : VlangScopeProcessor() {
            override fun isCompletion() = true

            override fun execute(element: PsiElement, state: ResolveState): Boolean {
                val named = element as? VlangNamedElement ?: return true
                val name = named.name ?: return true
                val lookup = "$receiverName.$name"

                val lookupElement = when (element) {
                    is VlangFieldDefinition   -> {
                        VlangCompletionUtil.createFieldLookupElement(
                            named,
                            lookup,
                            insertHandler = VlangCompletionUtil.FieldInsertHandler(),
                        )
                    }

                    is VlangMethodDeclaration -> {
                        VlangCompletionUtil.createMethodLookupElement(
                            named,
                            lookup,
                            insertHandler = VlangCompletionUtil.FunctionInsertHandler(element, null),
                        )
                    }

                    else                      -> return true
                }

                set.addElement(
                    lookupElement.toVlangLookupElement(
                        properties = VlangLookupElementProperties(
                            isContextElement = true,
                        )
                    )
                )
                return true
            }
        })
    }
}
