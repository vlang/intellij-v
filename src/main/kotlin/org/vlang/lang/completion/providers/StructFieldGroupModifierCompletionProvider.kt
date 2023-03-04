package org.vlang.lang.completion.providers

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import com.intellij.util.ProcessingContext
import org.vlang.lang.completion.VlangCompletionUtil.CONTEXT_COMPLETION_PRIORITY
import org.vlang.lang.completion.VlangCompletionUtil.withPriority
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangStructType

object StructFieldGroupModifierCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val parentStructType = parameters.position.parentOfType<VlangStructType>() ?: return
        val groups = parentStructType.fieldsGroupList
        val usedModifiers = groups.mapNotNull { it.memberModifiers?.text }.toSet()

        val variants = listOf("pub:", "mut:", "pub mut:").filter { it !in usedModifiers }

        result.addAllElements(
            variants.map {
                LookupElementBuilder.create(it)
                    .withInsertHandler(MyInsertHandler)
                    .bold()
                    .withPriority(CONTEXT_COMPLETION_PRIORITY)
            }
        )
    }

    object MyInsertHandler : InsertHandler<LookupElement> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val file = context.file as VlangFile
            val element = file.findElementAt(context.startOffset) ?: return

            val prev = PsiTreeUtil.prevLeaf(element) ?: return
            val prevPrev = PsiTreeUtil.prevLeaf(prev) ?: return
            if (prevPrev.text.contains("\n")) {
                context.document.deleteString(prev.startOffset, prev.endOffset)
            }
        }
    }
}
