package org.vlang.lang.completion.providers

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.icons.AllIcons
import com.intellij.util.ProcessingContext
import org.vlang.lang.completion.VlangCompletionUtil
import org.vlang.lang.completion.VlangCompletionUtil.CONTEXT_COMPLETION_PRIORITY
import org.vlang.lang.completion.VlangCompletionUtil.withPriority
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.impl.VlangLangUtil
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx

object MethodNameCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val pos = parameters.position

        val containingFile = pos.containingFile as VlangFile
        val structs = containingFile.getStructs()
        if (structs.count() != 1) {
            return
        }

        val struct = structs.first()
        val isGeneric = struct.structType.genericParameters != null
        val structName = struct.name
        val methods = struct.structType.toEx().methodsList(pos.project)
        val receiverName = VlangLangUtil.getUsedReceiverNameOrDefault(methods, structName[0].lowercaseChar().toString())
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

        val element = LookupElementBuilder.create("fn")
            .withPresentableText("fn ($receiverName $receiverReference$receiverType) method()")
            .withIcon(AllIcons.Actions.RealIntentionBulb)
            .withInsertHandler(insertHandler)
            .withPriority(CONTEXT_COMPLETION_PRIORITY)

        result.addElement(element)
    }
}
