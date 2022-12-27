package org.vlang.lang.search

import com.intellij.lang.LanguageCodeInsightActionHandler
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.VlangFieldDefinition
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangMethodDeclaration
import org.vlang.lang.psi.VlangNamedElement
import java.awt.event.MouseEvent

class VlangGotoSuperHandler : LanguageCodeInsightActionHandler {
    override fun startInWriteAction() = false

    override fun isValidFor(editor: Editor, file: PsiFile?) = file is VlangFile

    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        val element = file.findElementAt(editor.caretModel.offset) ?: return
        showPopup(element)
    }

    companion object {
        private fun showPopup(element: PsiElement) {
            val namedElement = element.parentOfType<VlangNamedElement>() ?: return
            showPopup(null, namedElement)
        }

        fun showPopup(event: MouseEvent?, typeSpec: VlangNamedElement) {
            val name = typeSpec.name ?: ""
            VlangGotoUtil.showPopup(
                "Implemented Interface of $name",
                { count ->
                    "Type '$name' Implements $count interfaces"
                },
                event,
                VlangGotoUtil.param(typeSpec),
                VlangGotoUtil.getDefaultRenderer(typeSpec),
                VlangSuperSearch,
            )
        }

        fun showPopup(event: MouseEvent?, method: VlangMethodDeclaration) {
            val name = method.name ?: ""
            VlangGotoUtil.showPopup(
                "Implemented methods of $name",
                { count ->
                    "Method '$name' Implements $count interfaces"
                },
                event,
                VlangGotoUtil.param(method),
                VlangGotoUtil.getMethodRenderer(method),
                VlangSuperMethodSearch,
            )
        }

        fun showPopup(event: MouseEvent?, field: VlangFieldDefinition) {
            val name = field.name ?: ""
            VlangGotoUtil.showPopup(
                "Implemented fields of $name",
                { count ->
                    "Field '$name' Implements $count interfaces"
                },
                event,
                VlangGotoUtil.param(field),
                VlangGotoUtil.getFieldRenderer(field),
                VlangSuperFieldSearch,
            )
        }
    }
}
