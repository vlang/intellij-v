package org.vlang.ide.inspections.unused

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangImportList
import org.vlang.lang.psi.VlangVisitor
import org.vlang.lang.psi.impl.imports.VlangImportOptimizer

class VlangUnusedImportInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        val file = holder.file as? VlangFile ?: return PsiElementVisitor.EMPTY_VISITOR
        val imports = file.getImports()
        val unusedImports = VlangImportOptimizer.collectUnusedImports(file, imports)

        return object : VlangVisitor() {
            override fun visitImportList(o: VlangImportList) {
                super.visitImportList(o)

                for (importDeclaration in o.importDeclarationList) {
                    val importSpec = importDeclaration.importSpec ?: continue

                    if (importSpec.importAlias != null) {
                        if (unusedImports.unusedAliases.contains(importSpec.importAlias)) {
                            holder.registerProblem(
                                importSpec.importAlias!!,
                                "Unused import alias",
                                ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                                OPTIMIZE_QUICK_FIX,
                            )
                        }
                    }

                    if (importSpec.selectiveImportList != null) {
                        for (selectiveImportSymbol in importSpec.selectiveImportList!!.referenceExpressionList) {
                            if (unusedImports.unusedSelectiveImportSymbol.contains(selectiveImportSymbol)) {
                                holder.registerProblem(
                                    selectiveImportSymbol,
                                    "Unused imported symbol '${selectiveImportSymbol.text}'",
                                    ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                                    OPTIMIZE_QUICK_FIX,
                                )
                            }
                        }
                    }

                    if (!unusedImports.unusedImports.contains(importSpec)) {
                        continue
                    }

                    holder.registerProblem(
                        importDeclaration,
                        "Unused import '${importSpec.pathName}'",
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
                val runnable = VlangImportOptimizer().processFile(file)
                runnable.run()
            }
        }
    }
}
