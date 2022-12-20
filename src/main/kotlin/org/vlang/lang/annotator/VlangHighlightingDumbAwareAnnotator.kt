package org.vlang.lang.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import org.vlang.ide.colors.VlangColor
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.VlangImportName
import org.vlang.lang.psi.VlangImportPath
import org.vlang.lang.psi.VlangModuleClause

class VlangHighlightingDumbAwareAnnotator : Annotator, DumbAware {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (holder.isBatchMode) return

        val color = when (element) {
            is LeafPsiElement  -> highlightLeaf(element)
            is VlangImportName -> {
                val parent = element.parent as? VlangImportPath ?: return
                val lastPart = parent.lastPartPsi
                if (element != lastPart) return

                VlangColor.MODULE
            }
            else                        -> null
        } ?: return

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION).textAttributes(color.textAttributesKey).create()
    }

    private fun highlightLeaf(element: LeafPsiElement): VlangColor? {
        if (element.elementType == VlangTypes.IDENTIFIER && element.parent is VlangModuleClause) {
            return VlangColor.MODULE
        }

        return null
    }
}
