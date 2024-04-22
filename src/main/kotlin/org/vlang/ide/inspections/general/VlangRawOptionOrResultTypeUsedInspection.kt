package org.vlang.ide.inspections.general

import com.intellij.codeInsight.intention.preview.IntentionPreviewInfo
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import com.intellij.refactoring.suggested.endOffset
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangElementFactory
import org.vlang.lang.psi.types.VlangOptionTypeEx
import org.vlang.lang.psi.types.VlangResultTypeEx
import org.vlang.lang.psi.types.VlangTypeEx
import org.vlang.utils.inside

class VlangRawOptionOrResultTypeUsedInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitSqlExpression(sql: VlangSqlExpression) {
                val type = sql.getType(null) ?: return
                checkType(sql, type)
            }

            override fun visitCallExpr(call: VlangCallExpr) {
                super.visitCallExpr(call)

                val resolved = call.resolve() ?: return
                val type = VlangCodeInsightUtil.getReturnType(resolved) ?: return
                checkType(call, type)
            }

            override fun visitReferenceExpression(expr: VlangReferenceExpression) {
                super.visitReferenceExpression(expr)

                val resolved = expr.resolve() as? VlangTypeOwner ?: return
                val type = resolved.getType(null) ?: return
                checkType(expr, type)
            }

            private fun checkType(expr: PsiElement, type: VlangTypeEx) {
                val containingFile = expr.containingFile as? VlangFile ?: return
                if (containingFile is VlangCodeFragment) {
                    return
                }

                val parent = expr.parent
                if (parent is VlangOrBlockExpr ||
                    parent is VlangDotExpression &&
                    (parent.optionPropagationExpression != null || parent.resultPropagationExpression != null)
                ) {
                    return
                }

                val text = expr.parent.text
                // TODO: should be fixed in the parser
                //       example: some()!.foo()
                if (text.contains("!") || text.contains("?")) {
                    return
                }

                val parentRef = expr.parentOfType<VlangReferenceExpression>()
                if (parentRef != null && parentRef.safeAccess()) {
                    return
                }

                if (parent is VlangFieldName) {
                    return
                }

                val insideReturn = expr.inside<VlangReturnStatement>()
                if (insideReturn) {
                    return
                }

                val insideSpawn = expr.inside<VlangSpawnExpression>()
                if (insideSpawn) {
                    return
                }

                val parentAssignment = expr.parentOfType<VlangAssignmentStatement>()
                if (parentAssignment != null && PsiTreeUtil.isAncestor(parentAssignment.leftHandExprList, expr, false)) {
                    return
                }

                val parentIf = expr.parentOfType<VlangIfExpression>()
                if (parentIf != null && parentIf.isGuard) {
                    return
                }

                if (type !is VlangResultTypeEx && type !is VlangOptionTypeEx) return

                if (type is VlangResultTypeEx) {
                    holder.registerProblem(
                        expr, "Result value should have either an `or {}` block, or `!` at the end",
                        AddOrBlockQuickFix.INSTANCE, AddResultPropagationQuickFix.INSTANCE,
                    )
                }
            }
        }
    }

    class AddOrBlockQuickFix : LocalQuickFix {
        override fun getFamilyName() = "Add or {}"

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val element = descriptor.psiElement
            val editor = FileEditorManager.getInstance(project).selectedEditor as? TextEditor ?: return

            val orBlock = VlangElementFactory.createOrBlock(project, element)
            val newOrBlock = element.replace(orBlock)

            editor.editor.caretModel.moveToOffset(newOrBlock.endOffset - 2)
        }

        override fun generatePreview(project: Project, previewDescriptor: ProblemDescriptor): IntentionPreviewInfo {
            return IntentionPreviewInfo.EMPTY
        }

        companion object {
            val INSTANCE = AddOrBlockQuickFix()
        }
    }

    class AddResultPropagationQuickFix : LocalQuickFix {
        override fun getFamilyName() = "Add !"

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val element = descriptor.psiElement
            val editor = FileEditorManager.getInstance(project).selectedEditor as? TextEditor ?: return

            val propagation = VlangElementFactory.createResultPropagation(project, element)
            val newPropagation = element.replace(propagation)

            editor.editor.caretModel.moveToOffset(newPropagation.endOffset)
        }

        override fun generatePreview(project: Project, previewDescriptor: ProblemDescriptor): IntentionPreviewInfo {
            return IntentionPreviewInfo.EMPTY
        }

        companion object {
            val INSTANCE = AddResultPropagationQuickFix()
        }
    }
}
