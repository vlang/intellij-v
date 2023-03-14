package org.vlang.ide.highlight

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import com.intellij.util.FunctionUtil
import org.vlang.lang.psi.VlangCallExpr
import org.vlang.lang.psi.VlangFunctionOrMethodDeclaration
import org.vlang.lang.psi.VlangReferenceExpression

class VlangRecursiveCallMarkerProvider : LineMarkerProvider {
    override fun getLineMarkerInfo(element: PsiElement) = null

    override fun collectSlowLineMarkers(elements: List<PsiElement>, result: MutableCollection<in LineMarkerInfo<*>?>) {
        val lines = mutableSetOf<Int>()
        for (element in elements) {
            if (element !is VlangCallExpr) continue

            val resolve = (element.expression as? VlangReferenceExpression)?.resolve() as? VlangFunctionOrMethodDeclaration ?: continue

            if (isRecursiveCall(element, resolve)) {
                val document = PsiDocumentManager.getInstance(element.project).getDocument(element.containingFile) ?: continue
                val lineNumber = document.getLineNumber(element.textOffset)
                if (!lines.contains(lineNumber)) {
                    result.add(RecursiveMethodCallMarkerInfo(element.identifier ?: element))
                }

                lines.add(lineNumber)
            }
        }
    }

    private class RecursiveMethodCallMarkerInfo(methodCall: PsiElement) : LineMarkerInfo<PsiElement?>(
        methodCall,
        methodCall.textRange,
        AllIcons.Gutter.RecursiveMethod,
        FunctionUtil.constant("Recursive call"),
        null,
        GutterIconRenderer.Alignment.RIGHT,
        { "Recursive call" }
    )

    companion object {
        private fun isRecursiveCall(element: PsiElement, function: VlangFunctionOrMethodDeclaration): Boolean {
            return element.parentOfType<VlangFunctionOrMethodDeclaration>() == function
        }
    }
}
