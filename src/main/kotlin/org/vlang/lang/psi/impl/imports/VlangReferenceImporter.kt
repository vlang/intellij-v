package org.vlang.lang.psi.impl.imports

import com.intellij.codeInsight.daemon.ReferenceImporter
import com.intellij.codeInsight.daemon.impl.CollectHighlightsUtil
import com.intellij.codeInsight.daemon.impl.DaemonListeners
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import org.vlang.ide.codeInsight.imports.VlangImportModuleQuickFix
import org.vlang.lang.VlangLanguage
import org.vlang.lang.psi.VlangCompositeElement

class VlangReferenceImporter : ReferenceImporter {
    override fun autoImportReferenceAtCursor(editor: Editor, file: PsiFile): Boolean {
        if (!file.viewProvider.languages.contains(VlangLanguage) || !DaemonListeners.canChangeFileSilently(file, true)) {
            return false
        }

        val caretOffset = editor.caretModel.offset
        val document = editor.document
        val lineNumber = document.getLineNumber(caretOffset)
        val startOffset = document.getLineStartOffset(lineNumber)
        val endOffset = document.getLineEndOffset(lineNumber)

        val elements = CollectHighlightsUtil.getElementsInRange(file, startOffset, endOffset)
        for (element in elements) {
            if (element is VlangCompositeElement) {
                for (reference in element.references) {
                    val fix = VlangImportModuleQuickFix(reference)
                    if (fix.doAutoImportOrShowHint(editor, false)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun isAddUnambiguousImportsOnTheFlyEnabled(file: PsiFile): Boolean = true
}
