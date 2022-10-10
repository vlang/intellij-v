package org.vlang.ide.inspections.general

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.ide.inspections.unused.VlangUnusedImportInspection
import org.vlang.lang.psi.VlangImportList
import org.vlang.lang.psi.VlangVisitor

class VlangAmbiguousImportInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitImportList(o: VlangImportList) {
                super.visitImportList(o)

                val imports = o.importDeclarationList.mapNotNull { it.importSpec }
                val duplicates = VlangCodeInsightUtil.findDuplicateImports(imports)

                // TODO: support selective imports ambiguity like
                // import foo { baz }
                // import foo.bar { baz }
                for (duplicate in duplicates) {
                    holder.registerProblem(
                        duplicate,
                        "Ambiguous import '${duplicate.importedName}', is already imported earlier",
                        ProblemHighlightType.GENERIC_ERROR,
                        VlangUnusedImportInspection.OPTIMIZE_QUICK_FIX
                    )
                }
            }
        }
    }
}
