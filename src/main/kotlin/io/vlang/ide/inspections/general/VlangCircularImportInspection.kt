package io.vlang.ide.inspections.general

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import io.vlang.ide.inspections.VlangBaseInspection
import io.vlang.lang.psi.VlangFile
import io.vlang.lang.psi.VlangImportName
import io.vlang.lang.psi.VlangVisitor
import io.vlang.lang.psi.impl.VlangModule

class VlangCircularImportInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {

        fun hasCircularImport(module: VlangModule, visited: MutableSet<VlangModule>): Boolean {
            if (module in visited) return true // circular import detected, stop here

            visited.add(module)
            module.directory.files.filterIsInstance<VlangFile>().forEach { file ->
                val imports = file.getImports()
                for (import in imports) {
                    import.resolve().firstOrNull()?.let{
                        return hasCircularImport(it, visited)
                    }
                }
            }
            visited.remove(module)

            return false // exhausted dfs search, no circular import detected
        }

        return object : VlangVisitor() {

            override fun visitImportName(importName: VlangImportName) {
                super.visitImportName(importName)
                val importList = importName.resolve().toList()
                // check for circular import
                importList.forEach { module ->
                    if(hasCircularImport(module, mutableSetOf())){
                        holder.registerProblem(
                            importName,
                            "Circular import detected",
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                        )
                    }
                } // end check for circular import
            }

        }

    }
}
