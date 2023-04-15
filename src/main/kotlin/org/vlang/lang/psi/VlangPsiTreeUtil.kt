package org.vlang.lang.psi

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.Couple
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.StubBasedPsiElement
import com.intellij.psi.impl.source.tree.TreeUtil
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.SmartList

object VlangPsiTreeUtil {
    @JvmStatic
    fun <T : PsiElement?> getStubChildOfType(element: PsiElement?, aClass: Class<T>): T? {
        if (element == null) return null
        val stub = (if (element is StubBasedPsiElement<*>) element.stub else null)
            ?: return PsiTreeUtil.getChildOfType(element, aClass)
        for (childStub in stub.childrenStubs) {
            val child = childStub.psi
            if (aClass.isInstance(child)) {
                return child as T
            }
        }
        return null
    }

    @JvmStatic
    inline fun <reified T: StubElement<*>> StubElement<*>.parentStubOfType(): T? {
        var stub: StubElement<*>? = this
        while (stub != null) {
            if (stub is T) {
                return stub
            }
            stub = stub.parentStub
        }
        return null
    }

    @JvmStatic
    fun <T : PsiElement?> getStubChildrenOfTypeAsList(element: PsiElement?, aClass: Class<T>): List<T> {
        if (element == null) return emptyList()
        val stub = (if (element is StubBasedPsiElement<*>) element.stub else null)
            ?: return PsiTreeUtil.getChildrenOfTypeAsList(element, aClass)
        val result: MutableList<T> = SmartList()
        for (childStub in stub.childrenStubs) {
            val child = childStub.psi
            if (aClass.isInstance(child)) {
                result.add(child as T)
            }
        }
        return result
    }

    private fun getElementRange(file: VlangFile, startOffset: Int, endOffset: Int): Couple<PsiElement?>? {
        var startElement = findNotWhiteSpaceElementAtOffset(file, startOffset, true)
        var endElement = findNotWhiteSpaceElementAtOffset(file, endOffset - 1, false)
        if (startElement == null || endElement == null) return null
        val startNode: ASTNode? = TreeUtil.findFirstLeaf(startElement.node)
        val endNode = TreeUtil.findLastLeaf(endElement.node)
        if (startNode == null || endNode == null) return null
        startElement = startNode.psi
        endElement = endNode.psi
        return if (startElement == null || endElement == null) null else Couple.of(
            startElement,
            endElement
        )
    }

    /**
     * Return element range which contains TextRange(start, end) of top level elements
     * common parent of elements is straight parent for each element
     */
    private fun getTopmostElementRange(elementRange: Couple<PsiElement?>): Couple<PsiElement?>? {
        if (elementRange.first == null || elementRange.second == null) return null
        var commonParent = PsiTreeUtil.findCommonParent(elementRange.first, elementRange.second)
            ?: return null
        if (commonParent.isEquivalentTo(elementRange.first) || commonParent.isEquivalentTo(elementRange.second)) {
            commonParent = commonParent.parent
        }
        var startElement: PsiElement? = PsiTreeUtil.findPrevParent(
            commonParent, elementRange.first!!
        )
        var endElement = PsiTreeUtil.findPrevParent(commonParent, elementRange.second!!)
        if (!startElement!!.parent.isEquivalentTo(endElement.parent)) return null
        val start = elementRange.first!!.textRange.startOffset
        val end = elementRange.second!!.textRange.endOffset
        val range = commonParent.textRange
        val children = commonParent.children
        if (range.equalsToRange(
                start,
                end
            ) || range.startOffset == start && (children.size == 0 || children[0].textRange.startOffset > start) || range.endOffset == end && (children.size == 0 || children[children.size - 1].textRange.endOffset < end)
        ) {
            startElement = commonParent
            endElement = commonParent
        }
        if (startElement.isEquivalentTo(endElement)) {
            while (startElement!!.textRange == startElement.parent.textRange) {
                startElement = startElement.parent
            }
            return Couple.of(startElement, startElement)
        }
        return Couple.of(startElement, endElement)
    }

    @JvmStatic
    fun <T : PsiElement?> getChildrenOfTypeAsList(element: PsiElement?, aClass: Class<out T>): List<T> {
        return PsiTreeUtil.getChildrenOfTypeAsList(element, aClass)
    }

    @JvmStatic
    fun <T : PsiElement?> getChildOfType(element: PsiElement?, aClass: Class<T>): T? {
        return PsiTreeUtil.getChildOfType(element, aClass)
    }

    private fun findNotWhiteSpaceElementAtOffset(file: VlangFile, offset: Int, forward: Boolean): PsiElement? {
        var element = file.findElementAt(offset)
        while (element is PsiWhiteSpace) {
            element = file.findElementAt(if (forward) element.getTextRange().endOffset else element.getTextRange().startOffset - 1)
        }
        return element
    }
}