package org.vlang.lang

import com.intellij.codeInsight.generation.CommenterDataHolder
import com.intellij.codeInsight.generation.SelfManagingCommenter
import com.intellij.codeInsight.generation.SelfManagingCommenterUtil
import com.intellij.lang.CodeDocumentationAwareCommenterEx
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.util.text.CharArrayUtil
import org.vlang.lang.doc.psi.VlangDocComment
import org.vlang.lang.psi.VlangDocElementTypes
import org.vlang.lang.psi.VlangTokenTypes

class VlangCommenter : CodeDocumentationAwareCommenterEx, SelfManagingCommenter<VlangCommenter.VlangCommenterDataHolder> {
    class VlangCommenterDataHolder : CommenterDataHolder()

    override fun getLineCommentPrefix() = "//"

    override fun getBlockCommentPrefix() = "/*"

    override fun getBlockCommentSuffix() = "*/"

    override fun getCommentedBlockCommentPrefix() = null

    override fun getCommentedBlockCommentSuffix() = null

    override fun getLineCommentTokenType() = VlangTokenTypes.LINE_COMMENT

    override fun getBlockCommentTokenType() = VlangTokenTypes.MULTI_LINE_COMMENT

    override fun getDocumentationCommentTokenType() = VlangDocElementTypes.DOC_COMMENT

    override fun getDocumentationCommentPrefix() = "/**"

    override fun getDocumentationCommentLinePrefix() = "*"

    override fun getDocumentationCommentSuffix() = "*/"

    override fun isDocumentationComment(element: PsiComment) =
        element is VlangDocComment

    override fun isDocumentationCommentText(element: PsiElement): Boolean {
        val node = element.node ?: return false
        return node.elementType == VlangDocElementTypes.DOC_COMMENT
    }

    override fun getBlockCommentPrefix(selectionStart: Int, document: Document, data: VlangCommenterDataHolder): String {
        return blockCommentPrefix
    }

    override fun getBlockCommentSuffix(selectionEnd: Int, document: Document, data: VlangCommenterDataHolder): String {
        return blockCommentSuffix
    }

    override fun insertBlockComment(startOffset: Int, endOffset: Int, document: Document, data: VlangCommenterDataHolder): TextRange {
        return SelfManagingCommenterUtil.insertBlockComment(
            startOffset, endOffset,
            document, blockCommentPrefix, blockCommentSuffix,
        )
    }

    override fun uncommentBlockComment(startOffset: Int, endOffset: Int, document: Document?, data: VlangCommenterDataHolder) {
        SelfManagingCommenterUtil.uncommentBlockComment(
            startOffset,
            endOffset,
            document!!,
            blockCommentPrefix,
            blockCommentSuffix,
        )
    }

    override fun getBlockCommentRange(selectionStart: Int, selectionEnd: Int, document: Document, data: VlangCommenterDataHolder): TextRange? {
        return SelfManagingCommenterUtil
            .getBlockCommentRange(
                selectionStart,
                selectionEnd,
                document,
                blockCommentPrefix,
                blockCommentSuffix,
            )
    }

    override fun getCommentPrefix(line: Int, document: Document, data: VlangCommenterDataHolder): String {
        return lineCommentPrefix
    }

    override fun isLineCommented(line: Int, offset: Int, document: Document, data: VlangCommenterDataHolder): Boolean {
        return CharArrayUtil.regionMatches(document.charsSequence, offset, lineCommentPrefix)
    }

    override fun uncommentLine(line: Int, offset: Int, document: Document, data: VlangCommenterDataHolder) {
        if (CharArrayUtil.regionMatches(document.charsSequence, offset, lineCommentPrefix)) {
            val hasSpaceAfterPrefix = document.charsSequence[offset + lineCommentPrefix.length] == ' '
            val endOffset = offset + lineCommentPrefix.length + if (hasSpaceAfterPrefix) 1 else 0
            document.deleteString(offset, endOffset)
        }
    }

    override fun commentLine(line: Int, offset: Int, document: Document, data: VlangCommenterDataHolder) {
        document.insertString(offset, "$lineCommentPrefix ")
    }

    override fun createLineCommentingState(startLine: Int, endLine: Int, document: Document, file: PsiFile): VlangCommenterDataHolder {
        return VlangCommenterDataHolder()
    }

    override fun createBlockCommentingState(start: Int, end: Int, document: Document, file: PsiFile): VlangCommenterDataHolder {
        return VlangCommenterDataHolder()
    }
}
