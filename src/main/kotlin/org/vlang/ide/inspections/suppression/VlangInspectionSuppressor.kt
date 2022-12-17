package org.vlang.ide.inspections.suppression

import com.intellij.codeInsight.daemon.impl.actions.AbstractBatchSuppressByNoInspectionCommentFix
import com.intellij.codeInspection.InspectionSuppressor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.codeInspection.SuppressionUtil
import com.intellij.codeInspection.SuppressionUtilCore
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import org.vlang.lang.psi.VlangStatement
import org.vlang.lang.psi.impl.VlangElementFactory
import org.vlang.utils.ancestorOrSelf

class VlangInspectionSuppressor : InspectionSuppressor {
    override fun isSuppressedFor(element: PsiElement, toolId: String): Boolean {
        return isSuppressedInStatement(toolId, element)
    }

    private fun isSuppressedInStatement(toolId: String, element: PsiElement): Boolean {
        val statement = PsiTreeUtil.getNonStrictParentOfType(element, VlangStatement::class.java)
        if (statement != null) {
            val prev = PsiTreeUtil.skipSiblingsBackward(statement, PsiWhiteSpace::class.java)
            if (prev is PsiComment) {
                val text = prev.getText()
                val matcher = SuppressionUtil.SUPPRESS_IN_LINE_COMMENT_PATTERN.matcher(text)
                return matcher.matches() && SuppressionUtil.isInspectionToolIdMentioned(matcher.group(1), toolId)
            }
        }
        return false
    }

    override fun getSuppressActions(element: PsiElement?, toolId: String): Array<SuppressQuickFix> {
        return arrayOf(
            SuppressInspectionFix(toolId),
            SuppressInspectionFix(SuppressionUtil.ALL)
        )
    }

    private class SuppressInspectionFix(id: String) :
        AbstractBatchSuppressByNoInspectionCommentFix(id, id == SuppressionUtil.ALL) {

        init {
            text = if (id == SuppressionUtil.ALL) "Suppress all inspections for statement" else "Suppress for statement"
        }

        override fun createSuppression(project: Project, element: PsiElement, container: PsiElement) {
            val text = " ${SuppressionUtilCore.SUPPRESS_INSPECTIONS_TAG_NAME} $myID"
            val comment = SuppressionUtil.createComment(project, text, getCommentLanguage(element))
            val nl = VlangElementFactory.createNewLine(project)
            container.parent.addBefore(comment, container)
            container.parent.addBefore(nl, container)
        }

        override fun getContainer(context: PsiElement?): PsiElement? {
            if (context == null) return null
            return context.ancestorOrSelf<VlangStatement>()
        }
    }
}
