package org.vlang.ide.hints

import com.intellij.codeInsight.codeVision.CodeVisionAnchorKind
import com.intellij.codeInsight.codeVision.CodeVisionRelativeOrdering
import com.intellij.codeInsight.hints.codeVision.InheritorsCodeVisionProvider
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangInterfaceDeclaration
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.VlangStructDeclaration
import org.vlang.lang.search.*
import java.awt.event.MouseEvent
import java.util.concurrent.atomic.AtomicInteger

class VlangInheritorsCodeVisionProvider : InheritorsCodeVisionProvider() {
    companion object {
        private const val ID = "VlangInheritorsCodeVisionProvider"
    }

    override val defaultAnchor: CodeVisionAnchorKind = CodeVisionAnchorKind.Right
    override val id: String = ID
    override val name: String = "VlangInheritorsCodeVisionProvider"
    override val relativeOrderings = listOf(CodeVisionRelativeOrdering.CodeVisionRelativeOrderingAfter("VlangUsagesCodeVisionProvider"))

    override fun acceptsFile(file: PsiFile) = file is VlangFile

    override fun acceptsElement(element: PsiElement): Boolean {
        return element is VlangInterfaceDeclaration || element is VlangStructDeclaration
    }

    override fun getHint(element: PsiElement, file: PsiFile): String? {
        val usagesCount = AtomicInteger()

        val param = VlangGotoUtil.param(element)
        if (element is VlangInterfaceDeclaration) {
            VlangInheritorsSearch.execute(param) {
                usagesCount.incrementAndGet() <= 500
            }

            return when (val result = usagesCount.get()) {
                0    -> "no inheritors"
                1    -> "1 inheritor"
                else -> "$result inheritors"
            }
        }

        VlangSuperSearch.execute(param) {
            usagesCount.incrementAndGet() <= 500
        }

        return when (val result = usagesCount.get()) {
            0    -> null
            1    -> "implement 1 interface"
            else -> "implement $result interfaces"
        }
    }

    override fun handleClick(editor: Editor, element: PsiElement, event: MouseEvent?) {
        val name = (element as VlangNamedElement).name ?: return
        if (element is VlangInterfaceDeclaration) {
            VlangInheritorsLineMarkerProvider.showImplementationPopup(name, event, element)
        } else {
            VlangGotoSuperHandler.showPopup(event, element)
        }
    }
}
