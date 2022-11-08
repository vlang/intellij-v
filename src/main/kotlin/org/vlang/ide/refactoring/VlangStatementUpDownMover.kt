package org.vlang.ide.refactoring

import com.intellij.codeInsight.editorActions.moveUpDown.LineMover
import com.intellij.codeInsight.editorActions.moveUpDown.LineRange
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.parentOfType
import com.intellij.psi.util.parentOfTypes
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import org.vlang.ide.documentation.CommentsConverter
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.*

class VlangStatementUpDownMover : LineMover() {
    override fun checkAvailable(editor: Editor, file: PsiFile, info: MoveInfo, down: Boolean): Boolean {
        val selection = editor.selectionModel
        if (selection.hasSelection()) {
            // TODO: support moving selected lines
            return true
        }

        val offset = editor.caretModel.offset
        val element = file.findElementAt(offset) ?: return false

        val parent = findParent(element) ?: return false
        info.toMove = elementRange(editor, parent)

        val siblingElement = findSibling(parent, down) ?: return false
        info.toMove2 = elementRange(editor, siblingElement)

        return true
    }

    private fun elementRange(editor: Editor, element: PsiElement):LineRange {
        val comments = CommentsConverter.getCommentsForElement(element)
        val start = if (comments.isNotEmpty()) {
            comments.first().startOffset
        } else {
            element.startOffset
        }
        val startLine = editor.document.getLineNumber(start)
        val endLine = editor.document.getLineNumber(element.endOffset) + 1
        return LineRange(startLine, endLine)
    }

    private fun findParent(element: PsiElement): PsiElement? {
        if (element.parentOfType<VlangStatement>() != null) {
            return null
        }

        return element.parentOfTypes(
            VlangFunctionOrMethodDeclaration::class,
            VlangStructDeclaration::class,
            VlangInterfaceDeclaration::class,
            VlangEnumDeclaration::class,
            VlangConstDeclaration::class,
            VlangGlobalVariableDeclaration::class,
            VlangTypeAliasDeclaration::class,
        )
    }

    private fun findSibling(element: PsiElement, down: Boolean): PsiElement? {
        val types = listOf(
            VlangTypes.FUNCTION_DECLARATION,
            VlangTypes.METHOD_DECLARATION,
            VlangTypes.STRUCT_DECLARATION,
            VlangTypes.INTERFACE_DECLARATION,
            VlangTypes.ENUM_DECLARATION,
            VlangTypes.CONST_DECLARATION,
            VlangTypes.GLOBAL_VARIABLE_DECLARATION,
            VlangTypes.TYPE_ALIAS_DECLARATION,
        )
        return if (down)
            findSiblingForward(element, types)
        else
            findSiblingBackward(element, types)
    }

    private fun findSiblingForward(element: PsiElement, elementTypes: List<IElementType>): PsiElement? {
        var e = element.nextSibling
        while (e != null) {
            if (elementTypes.contains(e.node.elementType)) {
                return e
            }
            e = e.nextSibling
        }
        return null
    }

    private fun findSiblingBackward(element: PsiElement, elementTypes: List<IElementType>): PsiElement? {
        var e = element.prevSibling
        while (e != null) {
            if (elementTypes.contains(e.node.elementType)) {
                return e
            }
            e = e.prevSibling
        }
        return null
    }
}
