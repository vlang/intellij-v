package org.vlang.lang.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentsOfType
import org.vlang.lang.psi.VlangLabeledStatement

object LabelUtil {
    fun collectContextLabelNames(context: PsiElement): List<String> {
        val labeledStatements = collectContextLabels(context)

        val labels = mutableListOf<String>()
        labeledStatements.forEach { statement ->
            labels.add(statement.label.labelRef.text)
        }
        return labels
    }

    fun collectContextLabels(context: PsiElement): List<VlangLabeledStatement> {
        return context.parentsOfType<VlangLabeledStatement>().toList()
    }
}
