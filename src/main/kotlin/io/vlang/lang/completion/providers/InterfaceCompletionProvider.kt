package io.vlang.lang.completion.providers

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import com.intellij.util.Processors
import io.vlang.lang.completion.VlangCompletionUtil
import io.vlang.lang.completion.providers.ReferenceCompletionProvider.MyScopeProcessor
import io.vlang.lang.psi.VlangFile
import io.vlang.lang.psi.VlangInterfaceDeclaration
import io.vlang.lang.psi.VlangNamedElement
import io.vlang.lang.psi.VlangReferenceExpressionBase
import io.vlang.lang.psi.VlangStructDeclaration
import io.vlang.lang.psi.impl.VlangCachedReference
import io.vlang.lang.psi.impl.VlangReference
import io.vlang.lang.search.VlangGotoUtil
import io.vlang.lang.search.VlangSuperSearch

object InterfaceCompletionProvider : CompletionProvider<CompletionParameters>() {

//    override fun addCompletions(
//        parameters: CompletionParameters,
//        context: ProcessingContext,
//        result: CompletionResultSet,
//    ) {
//        val pos = parameters.position
//        val project = pos.project
//        val file = pos.containingFile as VlangFile
//        val struct = pos.parentOfType<VlangStructDeclaration>() ?: return
//
//        val set = VlangCompletionUtil.withCamelHumpPrefixMatcher(result)
//
//        val alreadyImplementedTypes = getAlreadyImplementedTypes(struct)
//        val processor = Processor<VlangNamedElement> { element ->
//            ProgressManager.checkCanceled()
//            if (element !is VlangInterfaceDeclaration) return@Processor false
//            if (alreadyImplementedTypes.contains(element)) return@Processor false
//            val fromFile = element.containingFile as? VlangFile ?: return@Processor false
//            val isDirectlyAccessible = VlangCodeInsightUtil.isDirectlyAccessible(fromFile, file)
//            if (element.isPublic() || isDirectlyAccessible) {
//                val icon = AllIcons.Nodes.Interface
//                val name = element.name
//                if (name.isEmpty()) {
//                    return@Processor false
//                }
//
//                val moduleName = fromFile.getModuleName()
//
//                val qualifiedName = when {
//                    isDirectlyAccessible -> name
//                    moduleName != null   -> "${moduleName.substringAfterLast('.')}.$name"
//                    else                 -> name
//                }
//
//                val lookupElement = PrioritizedLookupElement.withPriority(
//                    LookupElementBuilder.createWithSmartPointer(qualifiedName, element)
//                        .withRenderer(ClassLikeRenderer(icon, moduleName))
//                        .withInsertHandler(ClassLikeInsertHandler(moduleName)), priority.toDouble()
//                )
//
//                set.addElement(lookupElement)
//            }
//            true
//        }
//
//        StubIndex.getInstance().processElements(
//            VlangClassLikeIndex.KEY, "interface", project, GlobalSearchScope.allScope(project), null,
//            VlangNamedElement::class.java, processor
//        )
//
//    }

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet,
    ) {
        val element = parameters.position
        val set = VlangCompletionUtil.withCamelHumpPrefixMatcher(result)

        val file = element.containingFile as? VlangFile ?: return
        val referenceExpression = element.parentOfType<VlangReferenceExpressionBase>() ?: return
        val ref = referenceExpression.reference
        if (ref is VlangReference) {
            ref.processResolveVariants(MyScopeProcessor(parameters, set, file, ReferenceCompletionProvider.interfacesOnlyAccept))
        } else if (ref is VlangCachedReference<*>) {
            ref.processResolveVariants(MyScopeProcessor(parameters, set, file, ReferenceCompletionProvider.interfacesOnlyAccept))
        }
    }

    private fun getAlreadyImplementedTypes(struct: VlangStructDeclaration): Set<VlangInterfaceDeclaration> {
        val set = mutableSetOf<VlangInterfaceDeclaration>()
        VlangSuperSearch.execute(
            VlangGotoUtil.param(struct),
            Processors.cancelableCollectProcessor(set as Collection<VlangNamedElement>)
        )
        return set
    }
}
