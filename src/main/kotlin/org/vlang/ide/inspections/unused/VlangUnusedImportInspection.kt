package org.vlang.ide.inspections.unused

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.ide.codeInsight.imports.VlangImportOptimizer
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.VlangImportList
import org.vlang.lang.psi.VlangVisitor

class VlangUnusedImportInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitImportList(o: VlangImportList) {
                super.visitImportList(o)

                for (importDeclaration in o.importDeclarationList) {
                    val importSpec = importDeclaration.importSpec ?: continue
                    if (VlangCodeInsightUtil.isImportUsed(importSpec)) {
                        continue
                    }

                    holder.registerProblem(
                        importDeclaration,
                        "Unused import '${importSpec.importedName}'",
                        ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                        OPTIMIZE_QUICK_FIX
                    )
                }
            }
        }
    }

    companion object {
        val OPTIMIZE_QUICK_FIX = object : LocalQuickFix {
            override fun getFamilyName() = "Optimize imports"

            override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
                val element = descriptor.psiElement ?: return
                val file = element.containingFile
                WriteCommandAction.runWriteCommandAction(project, VlangImportOptimizer().processFile(file))
            }
        }
    }
}
