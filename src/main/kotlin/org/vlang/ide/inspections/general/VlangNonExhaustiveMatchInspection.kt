package org.vlang.ide.inspections.general

import com.intellij.codeInsight.intention.FileModifier.SafeFieldForPreview
import com.intellij.codeInsight.intention.LowPriorityAction
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangElementFactory
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangEnumTypeEx
import org.vlang.lang.psi.types.VlangSumTypeEx

class VlangNonExhaustiveMatchInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitMatchExpression(match: VlangMatchExpression) {
                val expression = match.expression ?: return
                val expressionType = expression.getType(null) ?: return

                val variants = when (expressionType) {
                    is VlangEnumTypeEx -> {
                        val enum = expressionType.resolve(match.project) ?: return
                        enum.enumType.fieldList.map { ".${it.name}" }
                    }

                    is VlangSumTypeEx  -> {
                        expressionType.types.map { it.readableName(match) }
                    }

                    else               -> return
                }

                if (variants.isEmpty()) return
                if (match.withElse()) return

                val armEnums = match.expressionArms
                    .filterIsInstance<VlangEnumFetch>()
                    .map { "." + it.getIdentifier().text }

                val armTypes = match.typeArms
                    .map { it.toEx().readableName(match) }

                val usedVariants = if (expressionType is VlangEnumTypeEx) armEnums else armTypes

                val unusedVariants = variants.filter { it !in usedVariants }
                if (unusedVariants.isEmpty()) return

                holder.registerProblem(
                    match.match,
                    "Match is not exhaustive, add missing branches for: ${unusedVariants.joinToString { "'$it'" }} or 'else {}' branch",
                    AddMissingBranchesQuickFix(unusedVariants),
                    ADD_ELSE_QUICK_FIX,
                )
            }
        }
    }

    companion object {
        class AddMissingBranchesQuickFix(@SafeFieldForPreview private val cases: List<String>) : LocalQuickFix {
            override fun getFamilyName(): String = "Add missing branches"

            override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
                val match = descriptor.psiElement.parent as VlangMatchExpression
                val expression = match.expression ?: return
                val matchArms = match.matchArms ?: return

                val newMatchExpression = VlangElementFactory.createMatch(project, expression.text, cases)

                if (matchArms.matchArmList.isEmpty()) {
                    match.replace(newMatchExpression)
                    return
                }

                val armsToAdd = newMatchExpression.arms
                matchArms.add(VlangElementFactory.createNewLine(project))
                matchArms.addRange(armsToAdd.first(), armsToAdd.last())
            }
        }

        private val ADD_ELSE_QUICK_FIX = object : LocalQuickFix, LowPriorityAction {
            override fun getFamilyName(): String = "Add else branch"

            override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
                val match = descriptor.psiElement.parent as VlangMatchExpression
                val expression = match.expression ?: return
                val matchArms = match.matchArms ?: return

                val newMatchExpression = VlangElementFactory.createMatch(project, expression.text, listOf("else"))

                if (matchArms.matchArmList.isEmpty()) {
                    match.replace(newMatchExpression)
                    return
                }

                val elseArm = newMatchExpression.elseArm ?: return
                matchArms.add(VlangElementFactory.createNewLine(project))
                matchArms.add(elseArm)
            }
        }
    }
}
