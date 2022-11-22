package org.vlang.ide.inspections.typeMismatch

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.vlang.lang.psi.VlangAssignmentStatement
import org.vlang.lang.psi.VlangLiteral
import org.vlang.lang.psi.VlangVisitor
import org.vlang.lang.psi.types.VlangPrimitiveTypeEx

class VlangAssignTypeMismatchInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitAssignmentStatement(assign: VlangAssignmentStatement) {
                val vars = assign.leftHandExprList.expressionList
                val exprs = assign.expressionList

                if (vars.size == 1 && exprs.size == 1) {
                    val left = vars[0]
                    val expr = exprs[0]

                    val leftType = left.getType(null) ?: return
                    val exprType = expr.getType(null) ?: return

                    if (leftType is VlangPrimitiveTypeEx && leftType.isNumeric() && expr is VlangLiteral && expr.isNumeric) {
                        // if assign literal to numeric type, no need to check
                        // because it will be converted to the type of the variable
                        return
                    }

                    if (!leftType.isAssignableFrom(exprType, assign.project)) {
                        holder.registerProblem(
                            assign,
                            "Type mismatch: ${leftType.readableName(assign)} is not assignable from ${exprType.readableName(assign)}"
                        )
                    }
                }
            }
        }
    }
}
