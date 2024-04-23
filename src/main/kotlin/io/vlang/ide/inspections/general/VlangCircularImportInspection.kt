package io.vlang.ide.inspections.general

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import io.vlang.ide.inspections.VlangBaseInspection
import io.vlang.lang.psi.VlangFile
import io.vlang.lang.psi.VlangImportName
import io.vlang.lang.psi.VlangVisitor

class VlangCircularImportInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {

        return object : VlangVisitor() {

            override fun visitImportName(importName: VlangImportName) {

                super.visitImportName(importName)
                val currentModule = importName.containingFile.containingDirectory
                val importList = importName.resolve().toList()

                // check for circular import
                importList.forEach { module ->
                    val files = module.directory.files.filterIsInstance<VlangFile>()
                    for (file in files) {
                        val imports = file.getImports()
                        if (imports.any { it.name == currentModule.name }) {
                            holder.registerProblem(
                                importName,
                                "Circular import detected",
                                ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                            )
                        }
                    }
                } // end check for circular import
            }

        }
    }
}
