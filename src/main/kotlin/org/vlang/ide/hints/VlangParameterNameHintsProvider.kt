package org.vlang.ide.hints

import com.intellij.codeInsight.hints.InlayInfo
import com.intellij.codeInsight.hints.InlayParameterHintsProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangUnknownTypeEx
import org.vlang.utils.line
import kotlin.math.min

@Suppress("UnstableApiUsage")
class VlangParameterNameHintsProvider : InlayParameterHintsProvider {
    override fun getParameterHints(element: PsiElement, file: PsiFile): MutableList<InlayInfo> {
        val hints = mutableListOf<InlayInfo>()

        when (element) {
            is VlangCallExpr      -> handleCallExpr(element, hints)
            is VlangVarDefinition -> handleVarDefinition(element, hints)
            is VlangOrBlockExpr   -> handleOrBlockExpr(element, hints)
            is VlangElseStatement -> handleElseStatement(element, hints)
        }

        return hints
    }

    override fun getInlayPresentation(inlayText: String): String {
        if (inlayText.endsWith(" p")) {
            return inlayText.substring(0, inlayText.length - 2) + ":"
        }
        if (inlayText.endsWith(" →")) {
            return inlayText
        }
        return ": $inlayText"
    }

    private fun handleOrBlockExpr(element: VlangOrBlockExpr, hints: MutableList<InlayInfo>) {
        val right = element.block
        val openBracket = right.lbrace
        val closeBracket = right.rbrace
        if (openBracket.line() == closeBracket?.line()) {
            // don't show hint if 'or { ... }'
            return
        }
        val inlayInfo = InlayInfo("err →", openBracket.endOffset)
        hints.add(inlayInfo)
    }

    private fun handleElseStatement(element: VlangElseStatement, hints: MutableList<InlayInfo>) {
        if (!VlangCodeInsightUtil.insideElseBlockIfGuard(element)) {
            return
        }

        val right = element.block ?: return
        val openBracket = right.lbrace
        val closeBracket = right.rbrace
        if (openBracket.line() == closeBracket?.line()) {
            // don't show hint if 'else { ... }'
            return
        }
        val inlayInfo = InlayInfo("err →", openBracket.endOffset)
        hints.add(inlayInfo)
    }

    private fun handleVarDefinition(element: VlangVarDefinition, hints: MutableList<InlayInfo>) {
        val type = element.getTypeInner(null)
        val exType = type.toEx()
        if (exType is VlangUnknownTypeEx) {
            // no need show hint if type is unknown
            return
        }
        val readableName = exType.readableName(element)
        val inlayInfo = InlayInfo(readableName, element.endOffset)
        hints.add(inlayInfo)
    }

    private fun handleCallExpr(element: VlangCallExpr, hints: MutableList<InlayInfo>) {
        val expression = element.expression as? VlangReferenceExpression ?: return
        val reference = expression.reference
        val resolved = reference.resolve() ?: return
        if (resolved !is VlangFunctionOrMethodDeclaration) return

        val parameters = resolved.getSignature()?.parameters ?: return
        val params = parameters.parametersList

        val argsList = element.argumentList.elementList
        val args = argsList
            .filter { it.key == null && it.value != null } // don't show any hint for 'key: value' arguments
            .mapNotNull { it.value?.expression }

        for (i in 0 until min(params.size, args.size)) {
            val parameter = params[i] ?: continue
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
