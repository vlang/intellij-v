package org.vlang.ide.inspections.validityIssues

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangStructTypeEx
import org.vlang.lang.utils.VlangUnsafeUtil

class VlangCallMutableMethodByImmutableValueInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitCallExpr(call: VlangCallExpr) {
                val identifier = call.identifier ?: return
                val qualifier = call.qualifier ?: return
                val method = call.resolve() as? VlangMethodDeclaration ?: return

                val mutabilityOwner = qualifier.resolve() as? VlangMutabilityOwner ?: return

                if (suppress(call, method)) return

                val mutableMethod = method.isMutable
                val mutableCaller = mutabilityOwner.isMutable()

                if (mutableMethod && !mutableCaller) {
                    // TODO: fix this, after proper field makeMutable() implementation
                    val fixes = if (qualifier.resolve() is VlangFieldDefinition) emptyArray()
                    else arrayOf(MAKE_MUTABLE_QUICK_FIX)

                    holder.registerProblem(
                        identifier,
                        "Cannot call mutable method '${method.name}' on immutable value",
                        *fixes
                    )
                }
            }

            private fun suppress(call: VlangCallExpr, method: VlangMethodDeclaration): Boolean {
                val methodReceiverType = method.receiverType.toEx()
                if (methodReceiverType is VlangStructTypeEx && methodReceiverType.qualifiedName() == "sync.Channel") {
                    return true
                }
                return VlangUnsafeUtil.insideUnsafe(call)
            }
        }
    }

    companion object {
        val MAKE_MUTABLE_QUICK_FIX = object : LocalQuickFix {
            override fun getFamilyName() = "Make caller mutable"

            override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
                val ref = descriptor.psiElement.parent as? VlangReferenceExpression ?: return
                val qualifier = ref.getQualifier() as? VlangReferenceExpression ?: return
                val resolved = qualifier.resolve() as? VlangMutabilityOwner ?: return
                resolved.makeMutable()
            }
        }
    }
}
