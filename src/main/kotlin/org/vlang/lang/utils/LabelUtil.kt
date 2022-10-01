package org.vlang.lang.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.VlangFunctionOrMethodDeclaration
import org.vlang.lang.psi.VlangLabelDefinition

object LabelUtil {
    fun collectContextLabelNames(context: PsiElement): List<String> {
        val labeledStatements = collectContextLabels(context)

        val labels = mutableListOf<String>()
        labeledStatements.forEach { statement ->
            labels.add(statement.getIdentifier().text)
        }
        return labels
    }

    fun collectContextLabels(context: PsiElement): List<VlangLabelDefinition> {
        val containingFunction = context.parentOfType<VlangFunctionOrMethodDeclaration>() ?: return emptyList()
        val labels = mutableListOf<VlangLabelDefinition>()

        PsiTreeUtil.processElements(containingFunction) { element ->
            if (element is VlangLabelDefinition) {
                labels.add(element)
            }

            true
        }
        return labels
    }
}
