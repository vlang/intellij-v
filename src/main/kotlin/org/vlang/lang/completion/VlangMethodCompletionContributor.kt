package org.vlang.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.util.ProcessingContext
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.impl.VlangLangUtil
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.utils.parentNth

class VlangMethodCompletionContributor : CompletionContributor() {
    init {
        extend(CompletionType.BASIC, psiElement(VlangTypes.IDENTIFIER), MethodNameCompletionProvider)
    }

    private object MethodNameCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            if (VlangKeywordsCompletionContributor.shouldSuppress(parameters, result)) return

            val pos = parameters.position

            val isTopLevel = pos.parentNth<VlangFile>(4) != null
            if (!isTopLevel) {
                return
            }

            val containingFile = pos.containingFile as VlangFile
            val structs = containingFile.getStructs()
            if (structs.count() != 1) {
                return
            }

            val struct = structs.first()
            val isGeneric = struct.structType.genericParameters != null
            val structName = struct.name
            val methods = VlangLangUtil.getMethodList(pos.project, struct.structType.toEx())
            val receiverName = methods.firstOrNull()?.receiver?.name ?: structName[0].lowercaseChar()
            val receiverType = structName + if (isGeneric) struct.structType.genericParameters!!.text else ""

            val containMethodMethod = methods.any { it.name == "method" }
            val generatedName = if (containMethodMethod) "method1" else "method"

            val allMethodsTakeValue = if (methods.isNotEmpty()) methods.none { it.receiver.takeReference() } else false
            val receiverReference = if (allMethodsTakeValue) "" else "&"

            val insertHandler =
                VlangCompletionUtil.TemplateStringInsertHandler(
                    " ($receiverName $receiverReference$receiverType) \$name$(\$params$) \$return_type$ {\n\$END$\n}", true,
                    "name" to ConstantNode(generatedName),
                    "params" to ConstantNode(""),
                    "return_type" to ConstantNode("")
                )

            result.addElement(
                PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create("fn")
                        .withPresentableText("fn ($receiverName $receiverReference$receiverType) method()")
                        .withIcon(AllIcons.Actions.RealIntentionBulb)
                        .withInsertHandler(insertHandler),
                    VlangCompletionUtil.CONTEXT_COMPLETION_PRIORITY.toDouble()
                )
            )
        }
    }
}
