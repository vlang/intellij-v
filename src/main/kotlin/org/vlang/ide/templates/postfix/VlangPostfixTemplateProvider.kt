package org.vlang.ide.templates.postfix

import com.intellij.codeInsight.template.postfix.templates.PostfixTemplate
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateExpressionSelector
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateExpressionSelectorBase
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateProvider
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.Condition
import com.intellij.openapi.util.Conditions
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.SyntaxTraverser
import com.intellij.psi.util.PsiUtilCore
import org.vlang.lang.psi.VlangExpression
import org.vlang.lang.psi.VlangStatement

class VlangPostfixTemplateProvider : PostfixTemplateProvider {
    override fun getTemplates(): MutableSet<PostfixTemplate> = hashSetOf(
        VlangVarPostfixTemplate(),
        VlangIfPostfixTemplate(),
        VlangArgPostfixTemplate(),
        VlangPrintPostfixTemplate(),
        VlangPrintlnPostfixTemplate(),
        VlangEprintPostfixTemplate(),
        VlangEprintlnPostfixTemplate(),
        VlangDumpPostfixTemplate(),
        VlangParPostfixTemplate(),
        VlangReturnPostfixTemplate(),
        VlangMatchPostfixTemplate(),
        VlangForPostfixTemplate(),
        VlangForIndexedPostfixTemplate(),
        VlangNotPostfixTemplate(),
        VlangUnsafePostfixTemplate(),
    )

    override fun isTerminalSymbol(currentChar: Char) = currentChar == '.'

    override fun preExpand(file: PsiFile, editor: Editor) {}

    override fun afterExpand(file: PsiFile, editor: Editor) {}

    override fun preCheck(copyFile: PsiFile, realEditor: Editor, currentOffset: Int) = copyFile

    companion object {
        fun findAllExpressions(condition: Condition<in PsiElement?>): PostfixTemplateExpressionSelector {
            return object : PostfixTemplateExpressionSelectorBase(condition) {
                override fun getNonFilteredExpressions(context: PsiElement, document: Document, offset: Int): List<PsiElement> {
                    val at = PsiUtilCore.getElementAtOffset(context.containingFile, offset - 1)
                    return SyntaxTraverser.psiApi().parents(at)
                        .takeWhile(Conditions.notInstanceOf(VlangStatement::class.java))
                        .filter(VlangExpression::class.java)
                        .filter(PsiElement::class.java)
                        .toList()
                }
            }
        }
    }
}
