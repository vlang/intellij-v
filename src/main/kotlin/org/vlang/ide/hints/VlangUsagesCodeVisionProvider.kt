package org.vlang.ide.hints

import com.intellij.codeInsight.codeVision.CodeVisionAnchorKind
import com.intellij.codeInsight.codeVision.CodeVisionRelativeOrdering
import com.intellij.codeInsight.hints.codeVision.ReferencesCodeVisionProvider
import com.intellij.codeInsight.navigation.actions.GotoDeclarationAction
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.ui.awt.RelativePoint
import com.intellij.util.Processor
import org.vlang.ide.inspections.unused.VlangUnusedFunctionInspection.Companion.canBeExplicitlyUsed
import org.vlang.lang.psi.*
import java.awt.event.MouseEvent
import java.util.concurrent.atomic.AtomicInteger

class VlangUsagesCodeVisionProvider : ReferencesCodeVisionProvider() {
    companion object {
        private const val ID = "VlangUsagesCodeVisionProvider"
    }

    override val defaultAnchor: CodeVisionAnchorKind = CodeVisionAnchorKind.Right
    override val id: String = ID
    override val name: String = "VlangUsagesCodeVisionProvider"
    override val relativeOrderings = listOf(CodeVisionRelativeOrdering.CodeVisionRelativeOrderingFirst)

    override fun acceptsFile(file: PsiFile): Boolean = file is VlangFile

    override fun acceptsElement(element: PsiElement): Boolean {
        return element is VlangFunctionDeclaration && canBeExplicitlyUsed(element) ||
                element is VlangMethodDeclaration && canBeExplicitlyUsed(element) ||
                element is VlangStructDeclaration ||
                element is VlangEnumDeclaration ||
                element is VlangInterfaceDeclaration ||
                (element is VlangConstDeclaration && !element.isMultiline) ||
                element is VlangTypeAliasDeclaration
    }

    override fun getHint(element: PsiElement, file: PsiFile): String {
        val usagesCount = AtomicInteger()
        ReferencesSearch.search(adjustElement(element)).allowParallelProcessing()
            .forEach(Processor { usagesCount.incrementAndGet() <= 500 })

        return when (val result = usagesCount.get()) {
            0    -> " no usages"
            1    -> " 1 usage"
            else -> " $result usages"
        }
    }

    override fun handleClick(editor: Editor, element: PsiElement, event: MouseEvent?) {
        GotoDeclarationAction.startFindUsages(
            editor, element.project,
            adjustElement(element), if (event == null) null else RelativePoint(event)
        )
    }

    private fun adjustElement(element: PsiElement): PsiElement {
        if (element is VlangConstDeclaration && !element.isMultiline) {
            return element.constDefinitionList.firstOrNull() ?: element
        }
        return element
    }
}
