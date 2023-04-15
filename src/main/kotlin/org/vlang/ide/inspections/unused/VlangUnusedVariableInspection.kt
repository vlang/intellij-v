package org.vlang.ide.inspections.unused

import com.intellij.codeInsight.intention.LowPriorityAction
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangElementFactory

class VlangUnusedVariableInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitVarDefinition(variable: VlangVarDefinition) {
                if (!needCheck(variable)) return

                if (variable.isBlank()) return
                val identifier = variable.getIdentifier()
                val search = ReferencesSearch.search(variable, variable.useScope)
                if (search.findFirst() != null) return

                val fixes = mutableListOf(RENAME_TO_UNDERSCORE_FIX)
                if (canBeDeleted(variable)) {
                    fixes.add(DELETE_VARIABLE_FIX)
                }

                holder.registerProblem(
                    identifier,
                    "Unused variable <code>#ref</code> #loc",
                    ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                    *fixes.toTypedArray(),
                )
            }

            private fun canBeDeleted(variable: VlangVarDefinition): Boolean {
                val declaration = variable.parent as? VlangVarDeclaration ?: return false
                if (declaration is VlangRangeClause) return false
                return declaration.varDefinitionList.size == 1
            }
        }
    }

    companion object {
        val RENAME_TO_UNDERSCORE_FIX = object : LocalQuickFix {
            override fun getFamilyName() = "Rename to _"

            override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
                val element = descriptor.psiElement
                val variable = element.parent as? VlangNamedElement ?: return
                val identifier = variable.getIdentifier() ?: return
                identifier.replace(VlangElementFactory.createIdentifier(project, "_"))
            }
        }

        private val DELETE_VARIABLE_FIX = object : LocalQuickFix, LowPriorityAction {
            override fun getFamilyName() = "Delete variable"

            override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
                val element = descriptor.psiElement
                val variable = element.parentOfType<VlangVarDeclaration>() ?: return
                val expr = variable.expressionList.firstOrNull() ?: return
                val statement = variable.parent
                val newStatement = VlangElementFactory.createStatement(project, expr.text)
                statement.replace(newStatement)
            }
        }

        fun needCheck(variable: VlangNamedElement): Boolean {
            val containingFunction = variable.parentOfType<VlangFunctionOrMethodDeclaration>()
            if (containingFunction != null) {
                if (functionReturnTemplate(containingFunction)) return false
            }

            return true
        }

        private fun functionReturnTemplate(containingFunction: VlangFunctionOrMethodDeclaration?): Boolean {
            val calls = PsiTreeUtil.findChildrenOfType(containingFunction, VlangCallExpr::class.java)

            // TODO: better solution
            return calls
                .asSequence()
                .mapNotNull { it.expression?.text }
                .any { it == "\$vweb.html" || it.startsWith("\$tmpl") }
        }
    }
}
