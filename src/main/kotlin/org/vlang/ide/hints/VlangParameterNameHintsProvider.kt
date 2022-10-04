package org.vlang.ide.hints

import com.intellij.codeInsight.hints.InlayInfo
import com.intellij.codeInsight.hints.InlayParameterHintsProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import org.vlang.lang.psi.*
import kotlin.math.min

@Suppress("UnstableApiUsage")
class VlangParameterNameHintsProvider : InlayParameterHintsProvider {
    override fun getParameterHints(element: PsiElement, file: PsiFile): MutableList<InlayInfo> {
        val hints = mutableListOf<InlayInfo>()

        when (element) {
            is VlangCallExpr      -> handleCallExpr(element, hints)
            is VlangVarDefinition -> handleVarDefinition(element, hints)
        }

        return hints
    }

    override fun getInlayPresentation(inlayText: String): String {
        if (inlayText.endsWith(" p")) {
            return inlayText.substring(0, inlayText.length - 2) + ":"
        }
        return ": $inlayText"
    }

    private fun handleVarDefinition(element: VlangVarDefinition, hints: MutableList<InlayInfo>) {
        val type = element.getTypeInner(null)?.resolveType() ?: return
        val readableName = type.readableName
        val inlayInfo = InlayInfo(readableName, element.endOffset)
        hints.add(inlayInfo)
    }

    private fun handleCallExpr(element: VlangCallExpr, hints: MutableList<InlayInfo>) {
        val expression = element.expression as? VlangReferenceExpression ?: return
        val reference = expression.reference
        val resolved = reference.resolve() ?: return
        if (resolved !is VlangFunctionOrMethodDeclaration) return

        val parameterDeclarationList = resolved.getSignature()?.parameters?.parameterDeclarationList ?: return
        val params = parameterDeclarationList.flatMap { decl -> decl.paramDefinitionList }

        val argsList = element.argumentList.elementList
        val args = argsList
            .filter { it.key == null && it.value != null } // don't show any hint for 'key: value' arguments
            .mapNotNull { it.value?.expression }

        for (i in 0 until min(params.size, args.size)) {
            val parameter = params[i]
            val arg = args[i]

            val argInner = when (arg) {
                is VlangMutExpression    -> arg.expression
                is VlangSharedExpression -> arg.expression
                else                     -> arg
            }
            val argResolved = argInner?.reference?.resolve()
            if (argResolved is VlangNamedElement) {
                // don't show hints for obvious cases
                if (argResolved.name == parameter.name) continue
            }

            val name = parameter.name
            val offset = arg.startOffset
            val inlayInfo = InlayInfo("$name p", offset)
            hints.add(inlayInfo)
        }
    }

    override fun getDefaultBlackList(): MutableSet<String> {
        return mutableSetOf()
    }
}
