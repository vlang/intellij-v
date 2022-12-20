package org.vlang.ide.inspections.unused

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import org.vlang.lang.psi.VlangConstDeclaration
import org.vlang.lang.psi.VlangConstDefinition
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.VlangVisitor

class VlangUnusedConstantInspection : VlangUnusedBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitConstDefinition(const: VlangConstDefinition) = check(holder, const)
        }
    }

    override fun shouldBeCheck(element: VlangNamedElement): Boolean = true

    override fun registerProblem(holder: ProblemsHolder, element: VlangNamedElement) {
        val identifier = element.getIdentifier() ?: return
        holder.registerProblem(identifier,
            "Unused constant '${element.name}'",
            ProblemHighlightType.LIKE_UNUSED_SYMBOL,
            DELETE_CONSTANT_FIX)
    }

    companion object {
        val DELETE_CONSTANT_FIX = object : LocalQuickFix {
            override fun getFamilyName() = "Delete constant"

            override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
                val definition = descriptor.psiElement.parent as VlangConstDefinition
                val declaration = definition.parent as VlangConstDeclaration
                if (!declaration.isMultiline) {
                    declaration.delete()
                } else {
                    val countDefinition = declaration.constDefinitionList.size
                    if (countDefinition == 1) {
                        declaration.delete()
                    } else {
                        definition.prevSibling?.delete()
                        definition.delete()
                    }
                }
            }
        }
    }
}
