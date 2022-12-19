package org.vlang.ide.inspections.unused

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import org.vlang.ide.test.VlangTestUtil
import org.vlang.lang.psi.*
import org.vlang.lang.search.VlangSuperMarkerProvider

class VlangUnusedFunctionInspection : VlangUnusedBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitFunctionDeclaration(func: VlangFunctionDeclaration) = check(holder, func)
            override fun visitMethodDeclaration(method: VlangMethodDeclaration) = check(holder, method)
        }
    }

    override fun shouldBeCheck(element: VlangNamedElement): Boolean {
        if (!canBeExplicitlyUsed(element)) return false
        if (hasSuperMethod(element)) return false
        return true
    }

    override fun registerProblem(holder: ProblemsHolder, element: VlangNamedElement) {
        val identifier = element.getIdentifier() ?: return
        val kind = if (element is VlangFunctionDeclaration) "function" else "method"
        holder.registerProblem(
            identifier, "Unused $kind '${element.name}'",
            ProblemHighlightType.LIKE_UNUSED_SYMBOL
        )
    }

    companion object {
        fun canBeExplicitlyUsed(func: VlangNamedElement): Boolean {
            val file = func.containingFile as VlangFile
            if (func.isBlank()) return false
            if (func.name == "main" || func.name == "init") return false
            if (func is VlangMethodDeclaration && func.name == "str") return false
            if (isIteratorMethod(func)) return false
            if (isVwebMethod(func)) return false
            if (file.isTestFile() && func is VlangFunctionDeclaration && VlangTestUtil.isTestFunction(func)) return false
            return true
        }

        private fun isIteratorMethod(method: VlangNamedElement): Boolean {
            return method is VlangMethodDeclaration &&
                    method.name == "next" &&
                    method.getSignature()?.parameters?.paramDefinitionList?.isEmpty() ?: false
        }

        private fun isVwebMethod(method: VlangNamedElement): Boolean {
            return method is VlangMethodDeclaration &&
                    method.attributes != null &&
                    method.attributes!!.attributeList.any {
                        val plainAttribute = PsiTreeUtil.findChildOfType(it, VlangPlainAttribute::class.java)
                        val stringLiteral = PsiTreeUtil.findChildOfType(plainAttribute, VlangStringLiteral::class.java)
                        val contents = stringLiteral?.contents ?: ""
                        contents.contains("/") && !contents.contains(" ") // TODO: proper check when we add vvweb support
                    }
        }

        private fun hasSuperMethod(method: VlangNamedElement): Boolean {
            return method is VlangMethodDeclaration && VlangSuperMarkerProvider.hasSuperMethod(method)
        }
    }
}
