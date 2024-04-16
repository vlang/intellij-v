package io.vlang.lang.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import io.vlang.lang.annotator.checkers.VlangCommonProblemsChecker
import io.vlang.lang.annotator.checkers.VlangOptionResultProblemsChecker
import io.vlang.lang.psi.VlangVisitor

class VlangCheckerAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (!element.isValid) return

        val visitors = getCheckerVisitors(holder)

        for (visitor in visitors) {
            element.accept(visitor)
        }
    }

    private fun getCheckerVisitors(holder: AnnotationHolder): List<VlangVisitor> {
        return listOf(
            VlangCommonProblemsChecker(holder),
            VlangOptionResultProblemsChecker(holder),
        )
    }
}
