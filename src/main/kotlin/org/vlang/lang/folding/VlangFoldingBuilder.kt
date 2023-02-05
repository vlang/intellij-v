package org.vlang.lang.folding

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.FoldingGroup
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.*

class VlangFoldingBuilder : FoldingBuilderEx(), DumbAware {
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()

        root.accept(object : VlangRecursiveElementVisitor() {
            override fun visitBlock(el: VlangBlock) {
                val range = el.textRange
                val group = FoldingGroup.newGroup("VlangFoldingGroup")
                descriptors.add(FoldingDescriptor(el.node, TextRange(range.startOffset, range.endOffset), group))
                super.visitElement(el)
            }

            override fun visitImportList(el: VlangImportList) {
                if (el.importDeclarationList.size == 1) {
                    return
                }

                val first = el.importDeclarationList.first()
                val startIndex = first.importSpec?.startOffset ?: return

                val range = el.textRange
                val group = FoldingGroup.newGroup("VlangFoldingGroup")
                descriptors.add(FoldingDescriptor(el.node, TextRange(startIndex, range.endOffset), group))
                super.visitElement(el)
            }

            override fun visitConstDeclaration(el: VlangConstDeclaration) {
                genericFolding(el, VlangTypes.LPAREN)
                super.visitElement(el)
            }

            override fun visitArrayCreation(el: VlangArrayCreation) {
                genericFolding(el, VlangTypes.LBRACK)
                super.visitElement(el)
            }

            override fun visitUnsafeExpression(el: VlangUnsafeExpression) {
                genericFolding(el)
                super.visitElement(el)
            }

            override fun visitDeferStatement(el: VlangDeferStatement) {
                genericFolding(el)
                super.visitElement(el)
            }

            override fun visitStructDeclaration(el: VlangStructDeclaration) {
                genericFolding(el)
                super.visitElement(el)
            }

            override fun visitInterfaceDeclaration(el: VlangInterfaceDeclaration) {
                genericFolding(el)
                super.visitElement(el)
            }

            override fun visitEnumDeclaration(el: VlangEnumDeclaration) {
                genericFolding(el)
                super.visitElement(el)
            }

            override fun visitMatchExpression(el: VlangMatchExpression) {
                genericFolding(el)
                super.visitElement(el)
            }

            override fun visitSelectExpression(el: VlangSelectExpression) {
                genericFolding(el)
                super.visitElement(el)
            }

            override fun visitIfExpression(el: VlangIfExpression) {
                genericFolding(el)
                super.visitElement(el)
            }

            override fun visitElseBranch(el: VlangElseBranch) {
                genericFolding(el)
                super.visitElement(el)
            }

            override fun visitForStatement(el: VlangForStatement) {
                genericFolding(el)
                super.visitElement(el)
            }

            override fun visitLiteralValueExpression(el: VlangLiteralValueExpression) {
                genericFolding(el)
                super.visitElement(el)
            }

            override fun visitMapInitExpr(o: VlangMapInitExpr) {
                genericFolding(o)
                super.visitElement(o)
            }

            private fun genericFolding(el: PsiElement, start: IElementType = VlangTypes.LBRACE) {
                var lbrack: PsiElement? = null
                PsiTreeUtil.processElements(el) {
                    if (it.elementType == start) {
                        lbrack = it
                        return@processElements false
                    }

                    true
                }
                if (lbrack == null) {
                    return
                }

                val endOffset = when (el) {
                    is VlangIfExpression -> el.block?.endOffset ?: el.textRange.endOffset
                    else -> el.textRange.endOffset
                }

                val range = TextRange(lbrack!!.startOffset, endOffset)
                val group = FoldingGroup.newGroup("VlangFoldingGroup")
                descriptors.add(FoldingDescriptor(el.node, range, group))
            }
        })

        return descriptors.toTypedArray()
    }

    override fun getPlaceholderText(node: ASTNode) = when (node.elementType) {
        VlangTypes.ARRAY_CREATION -> "[...]"
        VlangTypes.CONST_DECLARATION -> "(...)"
        VlangTypes.IMPORT_LIST -> "..."
        else -> "{...}"
    }

    override fun isCollapsedByDefault(node: ASTNode) = node.elementType == VlangTypes.IMPORT_LIST
}
