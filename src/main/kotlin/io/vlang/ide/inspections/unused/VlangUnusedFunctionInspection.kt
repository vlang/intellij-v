package io.vlang.ide.inspections.unused

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import io.vlang.ide.inspections.unused.VlangDeleteQuickFix.Companion.DELETE_FUNCTION_FIX
import io.vlang.ide.inspections.unused.VlangDeleteQuickFix.Companion.DELETE_METHOD_FIX
import io.vlang.ide.inspections.unused.VlangDeleteQuickFix.Companion.DELETE_STATIC_METHOD_FIX
import io.vlang.ide.test.VlangTestUtil
import io.vlang.lang.psi.*
import io.vlang.lang.search.VlangSuperMarkerProvider

class VlangUnusedFunctionInspection : VlangUnusedBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitFunctionDeclaration(func: VlangFunctionDeclaration) = check(holder, func)
            override fun visitMethodDeclaration(method: VlangMethodDeclaration) = check(holder, method)
            override fun visitStaticMethodDeclaration(staticMethod: VlangStaticMethodDeclaration) =
                check(holder, staticMethod)
        }
    }

    override fun shouldBeCheck(element: VlangNamedElement): Boolean {
        if (!canBeExplicitlyUsed(element)) return false
        if (hasSuperMethod(element)) return false
        return true
    }

    override fun registerProblem(holder: ProblemsHolder, element: VlangNamedElement) {
        when (element) {
            is VlangFunctionDeclaration     -> {
                holder.registerProblem(
                    element,
                    "Unused function '${element.name}'",
                    ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                    element.getIdentifier().textRangeInParent,
                    DELETE_FUNCTION_FIX,
                )
            }

            is VlangMethodDeclaration       -> {
                holder.registerProblem(
                    element,
                    "Unused method '${element.name}'",
                    ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                    element.methodName.textRangeInParent,
                    DELETE_METHOD_FIX,
                )
            }

            is VlangStaticMethodDeclaration -> {
                holder.registerProblem(
                    element,
                    "Unused static method '${element.name}'",
                    ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                    element.getIdentifier().textRangeInParent,
                    DELETE_STATIC_METHOD_FIX,
                )
            }
        }
    }

    companion object {
        fun canBeExplicitlyUsed(func: VlangNamedElement): Boolean {
            val file = func.containingFile as VlangFile
            if (func.isBlank()) return false
            if (func.name == "main" || func.name == "init") return false
            if (func is VlangMethodDeclaration && func.name == "str") return false
            if (isIteratorMethod(func)) return false
            if (isVwebMethod(func)) return false
            return !(file.isTestFile() &&
                    func is VlangFunctionDeclaration &&
                    (VlangTestUtil.isTestFunction(func) || VlangTestUtil.isMetaTestFunction(func)))
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
