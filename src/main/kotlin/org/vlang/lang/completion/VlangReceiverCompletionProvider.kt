package org.vlang.lang.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangElementFactory
import org.vlang.lang.psi.impl.VlangPsiImplUtil.unwrapPointerOrOptionOrResultType
import org.vlang.lang.psi.impl.VlangScopeProcessor
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx

// TODO:
class VlangReceiverCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val pos = parameters.position
        val referenceExpression = pos.parentOfType<VlangReferenceExpression>() ?: return
        if (referenceExpression.getQualifier() != null) {
            return
        }

        val methodDeclaration = referenceExpression.parentOfType<VlangMethodDeclaration>() ?: return
        val receiver = methodDeclaration.receiver
        val receiverName = receiver.name ?: return
        val receiverType = unwrapPointerOrOptionOrResultType(receiver.type.toEx())?.name() ?: return

        val containingFile = pos.containingFile as VlangFile
        val moduleQualifiedName = containingFile.getModuleQualifiedName() ?: return
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

                val lookupElement = if (element is VlangFieldDefinition) {
                    VlangCompletionUtil.createFieldLookupElement(
                        named,
                        lookup,
                        priority = VlangCompletionUtil.CONTEXT_COMPLETION_PRIORITY + 1
                    )
                } else if (element is VlangMethodDeclaration) {
                    VlangCompletionUtil.createMethodLookupElement(
                        named,
                        lookup,
                        priority = VlangCompletionUtil.CONTEXT_COMPLETION_PRIORITY + 1
                    )
                } else {
                    return true
                }

                result.addElement(lookupElement)
                return true
            }
        })
    }
}
