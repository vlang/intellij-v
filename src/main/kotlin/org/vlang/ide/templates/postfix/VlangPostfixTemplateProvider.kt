package org.vlang.ide.templates.postfix

import com.intellij.codeInsight.template.postfix.templates.PostfixTemplate
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateProvider
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

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
    )

    override fun isTerminalSymbol(currentChar: Char) = currentChar == '.'

    override fun preExpand(file: PsiFile, editor: Editor) {}

    override fun afterExpand(file: PsiFile, editor: Editor) {}

    override fun preCheck(copyFile: PsiFile, realEditor: Editor, currentOffset: Int) = copyFile
}
