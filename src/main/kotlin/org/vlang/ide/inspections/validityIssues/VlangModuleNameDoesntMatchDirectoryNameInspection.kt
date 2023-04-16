package org.vlang.ide.inspections.validityIssues

import com.intellij.codeInsight.intention.LowPriorityAction
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.psi.PsiElementVisitor
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.VlangModuleClause
import org.vlang.lang.psi.VlangVisitor
import org.vlang.lang.psi.impl.VlangElementFactory

class VlangModuleNameDoesntMatchDirectoryNameInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitModuleClause(module: VlangModuleClause) {
                val project = module.project
                val projectRoot = project.guessProjectDir() ?: return
                val containingDirectory = module.containingFile?.containingDirectory ?: return
                // skip modules in root dir
                if (containingDirectory.virtualFile == projectRoot) return

                val moduleName = module.name.removePrefix("@")
                if (moduleName == "main" || moduleName == "builtin") return

                val directoryName = containingDirectory.name
                if (directoryName == "src") return

                if (moduleName != directoryName) {
                    holder.registerProblem(
                        module,
                        "Module name '$moduleName' doesn't match directory name '$directoryName'",
                        RENAME_CLAUSE_NAME_QUICK_FIX,
                        RENAME_DIRECTORY_NAME_QUICK_FIX,
                    )
                }
            }
        }
    }

    companion object {
        private val RENAME_CLAUSE_NAME_QUICK_FIX = object : LocalQuickFix {
            override fun getFamilyName() = "Change module name to match directory name"

            override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
                val module = descriptor.psiElement as? VlangModuleClause ?: return
                val directoryName = module.containingFile?.containingDirectory?.name ?: return
                module.nameIdentifier?.replace(VlangElementFactory.createIdentifier(project, directoryName))
            }
        }

        private val RENAME_DIRECTORY_NAME_QUICK_FIX = object : LocalQuickFix, LowPriorityAction {
            override fun getFamilyName() = "Change directory name to match module name"

            override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
                val module = descriptor.psiElement as? VlangModuleClause ?: return
                val moduleName = module.name
                module.containingFile?.containingDirectory?.name = moduleName
            }
        }
    }
}
