package org.vlang.lang

import com.intellij.lang.SmartEnterProcessorWithFixers
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangElementFactory

// TODO: refactor this and enable
class VlangSmartEnterProcessor : SmartEnterProcessorWithFixers() {
    init {
        addFixers(
            IfFixer(),
            ForFixer(),
            FuncFixer(),
        )
        addEnterProcessors(PlainEnterProcessor())
    }

    private fun addBlockIfNeeded(element: VlangStatement) {
        if (element.block == null) element.add(VlangElementFactory.createBlock(element.project))
    }

    override fun doNotStepInto(element: PsiElement?): Boolean {
        return element is VlangBlock
    }

    override fun collectAdditionalElements(element: PsiElement, result: MutableList<PsiElement?>) {
        val elemParent = PsiTreeUtil.getParentOfType(
            element,
            VlangStatement::class.java,
            VlangFunctionOrMethodDeclaration::class.java,
            VlangFunctionLit::class.java
        )
        if (elemParent != null) {
            result.add(elemParent)
            val parent = elemParent.parent
            if (parent is VlangStatement) {
                result.add(parent)
            }
        }
    }

    private class FuncFixer : Fixer<SmartEnterProcessorWithFixers>() {
        override fun apply(editor: Editor, processor: SmartEnterProcessorWithFixers, element: PsiElement) {
            if (element is VlangFunctionOrMethodDeclaration && element.getBlock() == null) {
                element.add(VlangElementFactory.createBlock(element.project))
            }
            if (element is VlangFunctionLit && element.block == null) {
                element.add(VlangElementFactory.createBlock(element.project))
            }
        }
    }

    private inner class IfFixer : Fixer<SmartEnterProcessorWithFixers>() {
        override fun apply(editor: Editor, processor: SmartEnterProcessorWithFixers, element: PsiElement) {
            if (element is VlangIfStatement)
                addBlockIfNeeded(element)
        }
    }

    private inner class ForFixer : Fixer<SmartEnterProcessorWithFixers>() {
        override fun apply(editor: Editor, processor: SmartEnterProcessorWithFixers, element: PsiElement) {
            if (element is VlangForStatement)
                addBlockIfNeeded(element as VlangStatement)
        }
    }

    class PlainEnterProcessor : FixEnterProcessor() {
        override fun doEnter(psiElement: PsiElement, file: PsiFile, editor: Editor, modified: Boolean): Boolean {
            val block = findBlock(psiElement)
            if (block != null) {
                editor.caretModel.moveToOffset(block.lbrace.textRange.endOffset)
            }
            plainEnter(editor)
            return true
        }

        companion object {
            private fun findBlock(element: PsiElement?): VlangBlock? {
                var el: PsiElement? = PsiTreeUtil.getNonStrictParentOfType(
                    element,
                    VlangStatement::class.java,
                    VlangBlock::class.java,
                    VlangFunctionOrMethodDeclaration::class.java,
                    VlangFunctionLit::class.java
                )
                if (el is VlangSimpleStatement && el.parent is VlangStatement) {
                    el = el.parent
                }
                if (el is VlangIfStatement) return el.block
                if (el is VlangForStatement) return el.block
                if (el is VlangBlock) return el
                if (el is VlangFunctionOrMethodDeclaration) return el.getBlock()
                return if (el is VlangFunctionLit) el.block else null
            }
        }
    }
}
