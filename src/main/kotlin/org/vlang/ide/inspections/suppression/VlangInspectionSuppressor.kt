package org.vlang.ide.inspections.suppression

import com.intellij.codeInsight.daemon.impl.actions.SuppressByCommentFix
import com.intellij.codeInspection.InspectionSuppressor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.codeInspection.SuppressionUtil
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import org.vlang.lang.psi.VlangStatement

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

    // TODO:
    override fun getSuppressActions(element: PsiElement?, toolId: String): Array<SuppressQuickFix> {
        return arrayOf(
            SuppressByCommentFix(toolId, VlangStatement::class.java)
        )
    }
}
