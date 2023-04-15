package org.vlang.ide.inspections.unused

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.progress.ProgressManager
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.search.searches.ReferencesSearch
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.ide.inspections.unused.VlangUnusedVariableInspection.Companion.RENAME_TO_UNDERSCORE_FIX
import org.vlang.ide.inspections.unused.VlangUnusedVariableInspection.Companion.needCheck
import org.vlang.lang.psi.*

class VlangUnusedParameterInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitMethodDeclaration(methodDeclaration: VlangMethodDeclaration) {
                super.visitMethodDeclaration(methodDeclaration)
                visitDeclaration(methodDeclaration.getSignature())
            }

            override fun visitFunctionDeclaration(functionDeclaration: VlangFunctionDeclaration) {
                super.visitFunctionDeclaration(functionDeclaration)

                if (functionDeclaration.isDefinition) {
                    return
                }

                visitDeclaration(functionDeclaration.getSignature())
            }

            override fun visitFunctionLit(functionLit: VlangFunctionLit) {
                super.visitFunctionLit(functionLit)

                visitDeclaration(functionLit.getSignature())
            }

            private fun visitDeclaration(signature: VlangSignature?) {
                if (signature == null) return
                val parameters = signature.parameters
                visitParameterList(parameters.paramDefinitionList)
            }

            private fun visitParameterList(parameters: List<VlangParamDefinition>) {
                for (parameter in parameters) {
                    ProgressManager.checkCanceled()
                    if (parameter.isBlank()) continue
                    if (!needCheck(parameter)) return
                    val identifier = parameter.getIdentifier() ?: continue

                    val search = ReferencesSearch.search(parameter, parameter.useScope)
                    if (search.findFirst() != null) continue

                    holder.registerProblem(identifier, "Unused parameter <code>#ref</code> #loc", ProblemHighlightType.LIKE_UNUSED_SYMBOL)
                }
            }

            override fun visitReceiver(receiver: VlangReceiver) {
                super.visitReceiver(receiver)
                if (receiver.isBlank()) return
                if (!needCheck(receiver)) return

                val search = ReferencesSearch.search(receiver, receiver.useScope)
                if (search.findFirst() != null) return

                holder.registerProblem(
                    receiver.getIdentifier(),
                    "Unused receiver <code>#ref</code> #loc", ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                    RENAME_TO_UNDERSCORE_FIX,
                )
            }
        }
    }
}
