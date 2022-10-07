package org.vlang.ide.inspections.unused

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
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
                    if (importSpec.selectiveImportList != null) {
                        // TODO: support selective imports
                        continue
                    }

                    val search = ReferencesSearch.search(importSpec.importPath.lastPartPsi, GlobalSearchScope.allScope(holder.project))
                    if (search.findFirst() != null)
                        continue

                    if (importSpec.importAlias != null) {
                        val searchAlias = ReferencesSearch.search(importSpec.importAlias!!, importSpec.useScope)
                        if (searchAlias.findFirst() != null)
                            continue
                    }

                    holder.registerProblem(importDeclaration, "Unused import <code>'${importSpec.name}'</code> #loc", ProblemHighlightType.LIKE_UNUSED_SYMBOL)
                }
            }
        }
    }
}
