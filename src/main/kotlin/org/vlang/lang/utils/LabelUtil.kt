package org.vlang.lang.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.VlangFunctionDeclaration
import org.vlang.lang.psi.VlangLabel

object LabelUtil {
    fun collectContextLabelNames(context: PsiElement): List<String> {
        val labeledStatements = collectContextLabels(context)

        val labels = mutableListOf<String>()
        labeledStatements.forEach { statement ->
            labels.add(statement.labelRef.text)
        }
        return labels
    }

    fun collectContextLabels(context: PsiElement): List<VlangLabel> {
        val containingFunction = context.parentOfType<VlangFunctionDeclaration>() ?: return emptyList()
        val labels = mutableListOf<VlangLabel>()

        PsiTreeUtil.processElements(containingFunction) { element ->
            if (element is VlangLabel) {
                labels.add(element)
            }

            true
        }
        return labels
    }
}
