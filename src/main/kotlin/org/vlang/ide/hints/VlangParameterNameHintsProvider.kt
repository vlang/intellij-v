package org.vlang.ide.hints

import com.intellij.codeInsight.hints.InlayInfo
import com.intellij.codeInsight.hints.InlayParameterHintsProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.refactoring.suggested.startOffset
import org.vlang.lang.psi.*
import kotlin.math.min

@Suppress("UnstableApiUsage")
class VlangParameterNameHintsProvider : InlayParameterHintsProvider {
    override fun getParameterHints(element: PsiElement, file: PsiFile): MutableList<InlayInfo> {
        if (element is VlangCallExpr) {
            val expression = element.expression as? VlangReferenceExpression ?: return mutableListOf()
            val reference = expression.reference
            val resolved = reference.resolve() ?: return mutableListOf()
            if (resolved !is VlangFunctionOrMethodDeclaration) return mutableListOf()

            val parameterDeclarationList = resolved.getSignature()?.parameters?.parameterDeclarationList ?: return mutableListOf()
            val params = parameterDeclarationList.flatMap { decl -> decl.paramDefinitionList }

            val argsList = element.argumentList.elementList
            val args = argsList
                .filter { it.key == null && it.value != null } // don't show any hint for 'key: value' arguments
                .mapNotNull { it.value?.expression }

            val hints = mutableListOf<InlayInfo>()

            for (i in 0 until min(params.size, args.size)) {
                val parameter = params[i]
                val arg = args[i]

                val argInner = when (arg) {
                    is VlangMutExpression -> arg.expression
                    is VlangSharedExpression -> arg.expression
                    else -> arg
                }
                val argResolved = argInner?.reference?.resolve()
                if (argResolved is VlangNamedElement) {
                    // don't show hints for obvious cases
                    if (argResolved.name == parameter.name) continue
                }

                val name = parameter.name
                val offset = arg.startOffset
                val inlayInfo = InlayInfo(name, offset)
                hints.add(inlayInfo)
            }

            return hints
        }

        return mutableListOf()
    }

    override fun getDefaultBlackList(): MutableSet<String> {
        return mutableSetOf()
    }
}
