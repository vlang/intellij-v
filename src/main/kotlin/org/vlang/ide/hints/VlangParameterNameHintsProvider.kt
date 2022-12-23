package org.vlang.ide.hints

import com.intellij.codeInsight.hints.HintInfo
import com.intellij.codeInsight.hints.InlayInfo
import com.intellij.codeInsight.hints.InlayParameterHintsProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.refactoring.suggested.startOffset
import org.vlang.lang.VlangLanguage
import org.vlang.lang.psi.*
import kotlin.math.min

@Suppress("UnstableApiUsage")
class VlangParameterNameHintsProvider : InlayParameterHintsProvider {
    override fun getHintInfo(element: PsiElement): HintInfo? {
        if (element !is VlangCallExpr) return null
        val resolved = element.resolve() ?: return null
        if (resolved !is VlangFunctionOrMethodDeclaration) return null
        val signature = resolved.getSignature() ?: return null
        val parameters = signature.parameters.paramDefinitionList.map { it.name ?: "_" }
        return createMethodInfo(resolved, parameters)
    }

    private fun createMethodInfo(function: VlangFunctionOrMethodDeclaration, parameters: List<String>): HintInfo.MethodInfo? {
        val qualifiedName = function.getQualifiedName() ?: return null
        return HintInfo.MethodInfo(qualifiedName, parameters, VlangLanguage)
    }

    override fun getParameterHints(element: PsiElement, file: PsiFile): MutableList<InlayInfo> {
        if (element !is VlangCallExpr) return mutableListOf()
        return handleCallExpr(element)
    }

    private fun handleCallExpr(element: VlangCallExpr): MutableList<InlayInfo> {
        val hints = mutableListOf<InlayInfo>()
        val resolved = element.resolve() ?: return hints
        if (resolved !is VlangFunctionOrMethodDeclaration) return hints

        val parameters = resolved.getSignature()?.parameters ?: return hints
        val params = parameters.paramDefinitionList

        val argsList = element.argumentList.elementList
        val args = argsList
            .filter { it.key == null && it.value != null } // don't show any hint for 'key: value' arguments
            .mapNotNull { it.value?.expression }

        for (i in 0 until min(params.size, args.size)) {
            val parameter = params[i] ?: continue
            val name = parameter.name ?: continue
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

            val offset = arg.startOffset
            val inlayInfo = InlayInfo(name, offset)
            hints.add(inlayInfo)
        }

        return hints
    }

    override fun getDefaultBlackList() = mutableSetOf("builtin.array.*(*)")

    override fun getBlacklistExplanationHTML(): String {
        return """
            To disable hints for a function use the appropriate pattern:<br />
            <b>builtin.*</b> - functions from the standard library<br />
            <b>builtin.array.*(*)</b> - methods of builtin array with any arguments<br />
            <b>datatypes.*</b> - functions from the datatypes module<br />
            <b>(*_)</b> - single parameter function where the parameter name ends with <i>_</i><br />
            <b>(key, value)</b> - functions with parameters <i>key</i> and <i>value</i><br />
            <b>*.put(key, value)</b> - <i>put</i> methods with <i>key</i> and <i>value</i> parameters
        """.trimIndent()
    }
}
