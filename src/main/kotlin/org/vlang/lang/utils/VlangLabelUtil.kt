package org.vlang.lang.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.VlangFunctionOrMethodDeclaration
import org.vlang.lang.psi.VlangLabelDefinition

object VlangLabelUtil {
    fun collectContextLabelNames(context: PsiElement): List<String> {
        val labeledStatements = collectContextLabels(context)

        val labels = mutableListOf<String>()
        labeledStatements.forEach { statement ->
            labels.add(statement.getIdentifier().text)
        }
        return labels
    }

    fun collectContextLabels(context: PsiElement): List<VlangLabelDefinition> {
        val containingElement = context.parentOfType<VlangFunctionOrMethodDeclaration>() ?: context.containingFile ?: return emptyList()
        val labels = mutableListOf<VlangLabelDefinition>()

        PsiTreeUtil.processElements(containingElement) { element ->
            if (element is VlangLabelDefinition) {
                labels.add(element)
            }

            true
        }
        return labels
    }
}
