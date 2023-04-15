package org.vlang.lang.doc.psi.impl

import com.intellij.psi.LiteralTextEscaper
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.impl.source.tree.AstBufferUtil
import com.intellij.psi.impl.source.tree.CompositePsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.childrenOfType
import com.intellij.util.text.CharArrayUtil
import org.vlang.lang.doc.psi.*
import org.vlang.lang.psi.StringLiteralEscaper
import org.vlang.lang.psi.impl.VlangElementFactory
import org.vlang.utils.ancestorStrict
import org.vlang.utils.childOfType
import org.vlang.utils.getPrevNonWhitespaceSibling

abstract class VlangDocElementImpl(type: IElementType) : CompositePsiElement(type), VlangDocElement {
    protected open fun <T: Any> notNullChild(child: T?): T =
        child ?: error("$text parent=${parent.text}")

    override val containingDoc: VlangDocComment
        get() = ancestorStrict()
            ?: error("VlangDocElement cannot leave outside of the doc comment! `${text}`")

    override val markdownValue: String
        get() = AstBufferUtil.getTextSkippingWhitespaceComments(this)

    override fun toString(): String = "${javaClass.simpleName}($elementType)"
}

class VlangDocGapImpl(type: IElementType, val text: CharSequence) : LeafPsiElement(type, text), VlangDocGap {
    override fun getTokenType(): IElementType = elementType
}

class VlangDocAtxHeadingImpl(type: IElementType) : VlangDocElementImpl(type), VlangDocAtxHeading
class VlangDocSetextHeadingImpl(type: IElementType) : VlangDocElementImpl(type), VlangDocSetextHeading

class VlangDocEmphasisImpl(type: IElementType) : VlangDocElementImpl(type), VlangDocEmphasis
class VlangDocStrongImpl(type: IElementType) : VlangDocElementImpl(type), VlangDocStrong
class VlangDocCodeSpanImpl(type: IElementType) : VlangDocElementImpl(type), VlangDocCodeSpan
class VlangDocAutoLinkImpl(type: IElementType) : VlangDocElementImpl(type), VlangDocAutoLink

class VlangDocInlineLinkImpl(type: IElementType) : VlangDocElementImpl(type), VlangDocInlineLink {
    override val linkText: VlangDocLinkText
        get() = notNullChild(childOfType())

    override val linkDestination: VlangDocLinkDestination
        get() = notNullChild(childOfType())
}

class VlangDocLinkReferenceShortImpl(type: IElementType) : VlangDocElementImpl(type), VlangDocLinkReferenceShort {
    override val linkLabel: VlangDocLinkLabel
        get() = notNullChild(childOfType())
}

class VlangDocLinkReferenceFullImpl(type: IElementType) : VlangDocElementImpl(type), VlangDocLinkReferenceFull {
    override val linkText: VlangDocLinkText
        get() = notNullChild(childOfType())

    override val linkLabel: VlangDocLinkLabel
        get() = notNullChild(childOfType())
}

class VlangDocLinkDefinitionImpl(type: IElementType) : VlangDocElementImpl(type), VlangDocLinkDefinition {
    override val linkLabel: VlangDocLinkLabel
        get() = notNullChild(childOfType())

    override val linkDestination: VlangDocLinkDestination
        get() = notNullChild(childOfType())
}

class VlangDocLinkTextImpl(type: IElementType) : VlangDocElementImpl(type), VlangDocLinkText
class VlangDocLinkLabelImpl(type: IElementType) : VlangDocElementImpl(type), VlangDocLinkLabel
class VlangDocLinkTitleImpl(type: IElementType) : VlangDocElementImpl(type), VlangDocLinkTitle
class VlangDocLinkDestinationImpl(type: IElementType) : VlangDocElementImpl(type), VlangDocLinkDestination

class VlangDocCodeFenceImpl(type: IElementType) : VlangDocElementImpl(type), VlangDocCodeFence {
    override fun isValidHost(): Boolean = true

    override val start: VlangDocCodeFenceStartEnd
        get() = notNullChild(childOfType())

    override val end: VlangDocCodeFenceStartEnd?
        get() = childrenOfType<VlangDocCodeFenceStartEnd>().getOrNull(1)

    override val lang: VlangDocCodeFenceLang?
        get() = childOfType()

    /**
     * Handles changes in PSI injected to the comment (see [VlangDoctestLanguageInjector]).
     * It is not used on typing. Instead, it's used on direct PSI changes (performed by
     * intentions/quick fixes).
     *
     * Each line of doc comment should start with some prefix (see [VlangDocKind.infix]). For example, with `///`.
     * But if some intention inserts newline to PSI, there will not be such prefix after that newline.
     * Here we insure that every comment line is started from appropriate prefix
     */
    override fun updateText(text: String): PsiLanguageInjectionHost {
        val prefix = "//"
        val infix = ""

        val prevSibling = getPrevNonWhitespaceSibling() // Should be an `infix` (e.g. `///`)

        val newText = StringBuilder()

        if (prevSibling != null && prevSibling.text != prefix) {
            // `newText` must be parsed in an empty file, so append a prefix if it differs from `infix` (e.g. `/**`)
            newText.append(prefix)

            // Then add a proper whitespace between the prefix (`/**`) and the first (`*`)
            val prevPrevSibling = prevSibling.prevSibling
            if (prevPrevSibling is PsiWhiteSpace) {
                newText.append(prevPrevSibling.text)
            } else {
                newText.append("\n")
            }
        }

        newText.append(infix)

        // Add a whitespace between `infix` and a code fence start (e.g. between "///" and "```").
        // The whitespace affects markdown escaping, hence markdown parsing
        if (prevSibling != null && prevSibling.nextSibling != this) {
            newText.append(prevSibling.nextSibling.text)
        }

        var prevIndent = ""
        var index = 0
        while (index < text.length) {
            val linebreakIndex = text.indexOf("\n", index)
            if (linebreakIndex == -1) {
                newText.append(text, index, text.length)
                break
            } else {
                val nextLineStart = linebreakIndex + 1
                newText.append(text, index, nextLineStart)
                index = nextLineStart

                val firstNonWhitespace = CharArrayUtil.shiftForward(text, nextLineStart, " \t")
                if (firstNonWhitespace == text.length) continue
                val isStartCorrect = text.startsWith(infix, firstNonWhitespace)
                if (!isStartCorrect) {
                    newText.append(prevIndent)
                    newText.append(infix)
                    newText.append(" ")
                } else {
                    prevIndent = text.substring(nextLineStart, firstNonWhitespace)
                }
            }
        }

        // There are some problems with indentation if we just use replaceWithText(text).
        // copied from PsiCommentManipulator
        val fromText = VlangElementFactory.createFileFromText(project, newText.toString())
        val newElement = PsiTreeUtil.findChildOfType(fromText, javaClass, false)
            ?: error(newText)
        return replace(newElement) as VlangDocCodeFenceImpl
    }

    override fun createLiteralTextEscaper(): LiteralTextEscaper<VlangDocCodeFenceImpl> =
        StringLiteralEscaper(this)
}

class VlangDocCodeBlockImpl(type: IElementType) : VlangDocElementImpl(type), VlangDocCodeBlock
class VlangDocHtmlBlockImpl(type: IElementType) : VlangDocElementImpl(type), VlangDocHtmlBlock

class VlangDocCodeFenceStartEndImpl(type: IElementType) : VlangDocElementImpl(type), VlangDocCodeFenceStartEnd
class VlangDocCodeFenceLangImpl(type: IElementType) : VlangDocElementImpl(type), VlangDocCodeFenceLang


