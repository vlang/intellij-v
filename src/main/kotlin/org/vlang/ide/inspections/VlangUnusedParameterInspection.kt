package org.vlang.ide.inspections

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.progress.ProgressManager
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.search.searches.ReferencesSearch
import org.vlang.lang.psi.*

class VlangUnusedParameterInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitMethodDeclaration(o: VlangMethodDeclaration) {
                super.visitMethodDeclaration(o)
                visitDeclaration(o, false)
            }

            override fun visitFunctionDeclaration(o: VlangFunctionDeclaration) {
                if (o.isDefinition) {
                    return
                }

                super.visitFunctionDeclaration(o)
                visitDeclaration(o, true)
            }

            private fun visitDeclaration(o: VlangFunctionOrMethodDeclaration, checkParameters: Boolean) {
                val signature = o.getSignature() ?: return
                if (checkParameters) {
                    val parameters = signature.parameters
                    visitParameterList(parameters.parameterDeclarationList, "parameter")
                }
                val returnParameters = signature.result?.parameters
                if (returnParameters != null) {
                    visitParameterList(returnParameters.parameterDeclarationList, "named return parameter")
                }
            }

            private fun visitParameterList(parameters: List<VlangParameterDeclaration>, what: String) {
                for (parameterDeclaration in parameters) {
                    for (parameter in parameterDeclaration.paramDefinitionList) {
                        ProgressManager.checkCanceled()
                        if (parameter.isBlank())
                            continue

                        val search = ReferencesSearch.search(parameter, parameter.useScope)
                        if (search.findFirst() != null)
                            continue

                        holder.registerProblem(parameter, "Unused $what <code>#ref</code> #loc", ProblemHighlightType.LIKE_UNUSED_SYMBOL)
                    }
                }
            }
        }
    }
}
