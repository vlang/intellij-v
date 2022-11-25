package org.vlang.lang.psi

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.vlang.lang.doc.psi.VlangDocCommentElementType
import org.vlang.lang.doc.psi.VlangDocCompositeTokenType
import org.vlang.lang.doc.psi.VlangDocTokenType
import org.vlang.lang.doc.psi.impl.*

@Suppress("MemberVisibilityCanBePrivate")
object VlangDocElementTypes {
    @JvmField val DOC_COMMENT_LINE = VlangTokenType("DOC_COMMENT_LINE")
    @JvmField val DOC_COMMENT = VlangDocCommentElementType()

    val DOC_DATA = VlangDocTokenType("<DOC_DATA>")
    val DOC_GAP = VlangDocTokenType("<DOC_GAP>")

    val DOC_ATX_HEADING = VlangDocCompositeTokenType("<DOC_ATX_HEADING>", ::VlangDocAtxHeadingImpl)
    val DOC_SETEXT_HEADING = VlangDocCompositeTokenType("<DOC_SETEXT_HEADING>", ::VlangDocSetextHeadingImpl)

    val DOC_EMPHASIS = VlangDocCompositeTokenType("<DOC_EMPHASIS>", ::VlangDocEmphasisImpl)
    val DOC_STRONG = VlangDocCompositeTokenType("<DOC_STRONG>", ::VlangDocStrongImpl)
    val DOC_CODE_SPAN = VlangDocCompositeTokenType("<DOC_CODE_SPAN>", ::VlangDocCodeSpanImpl)

    val DOC_AUTO_LINK = VlangDocCompositeTokenType("<DOC_AUTO_LINK>", ::VlangDocAutoLinkImpl)
    val DOC_INLINE_LINK = VlangDocCompositeTokenType("<DOC_INLINE_LINK>", ::VlangDocInlineLinkImpl)
    val DOC_SHORT_REFERENCE_LINK = VlangDocCompositeTokenType("<DOC_SHORT_REFERENCE_LINK>", ::VlangDocLinkReferenceShortImpl)
    val DOC_FULL_REFERENCE_LINK = VlangDocCompositeTokenType("<DOC_FULL_REFERENCE_LINK>", ::VlangDocLinkReferenceFullImpl)
    val DOC_LINK_DEFINITION = VlangDocCompositeTokenType("<DOC_LINK_DEFINITION>", ::VlangDocLinkDefinitionImpl)

    val DOC_LINK_TEXT = VlangDocCompositeTokenType("<DOC_LINK_TEXT>", ::VlangDocLinkTextImpl)
    val DOC_LINK_LABEL = VlangDocCompositeTokenType("<DOC_LINK_LABEL>", ::VlangDocLinkLabelImpl)
    val DOC_LINK_TITLE = VlangDocCompositeTokenType("<DOC_LINK_TITLE>", ::VlangDocLinkTitleImpl)
    val DOC_LINK_DESTINATION = VlangDocCompositeTokenType("<DOC_LINK_DESTINATION>", ::VlangDocLinkDestinationImpl)

    val DOC_CODE_FENCE = VlangDocCompositeTokenType("<DOC_CODE_FENCE>", ::VlangDocCodeFenceImpl)
    val DOC_CODE_BLOCK = VlangDocCompositeTokenType("<DOC_CODE_BLOCK>", ::VlangDocCodeBlockImpl)
    val DOC_HTML_BLOCK = VlangDocCompositeTokenType("<DOC_HTML_BLOCK>", ::VlangDocHtmlBlockImpl)

    val DOC_CODE_FENCE_START_END = VlangDocCompositeTokenType("<DOC_CODE_FENCE_START_END>", ::VlangDocCodeFenceStartEndImpl)
    val DOC_CODE_FENCE_LANG = VlangDocCompositeTokenType("<DOC_CODE_FENCE_LANG>", ::VlangDocCodeFenceLangImpl)

    private val MARKDOWN_ATX_HEADINGS = setOf(
        MarkdownElementTypes.ATX_1,
        MarkdownElementTypes.ATX_2,
        MarkdownElementTypes.ATX_3,
        MarkdownElementTypes.ATX_4,
        MarkdownElementTypes.ATX_5,
        MarkdownElementTypes.ATX_6
    )

    /**
     * Some Markdown nodes are skipped (like [MarkdownElementTypes.PARAGRAPH]) because they are mostly useless
     * for V and just increase Markdown tree depth. We're trying to keep the tree as simple as possible.
     */
    fun mapMarkdownToV(type: org.intellij.markdown.IElementType): VlangDocCompositeTokenType? {
        return when (type) {
            in MARKDOWN_ATX_HEADINGS                                               -> DOC_ATX_HEADING
            MarkdownElementTypes.SETEXT_1, MarkdownElementTypes.SETEXT_2           -> DOC_SETEXT_HEADING
            MarkdownElementTypes.EMPH                                              -> DOC_EMPHASIS
            MarkdownElementTypes.STRONG                                            -> DOC_STRONG
            MarkdownElementTypes.CODE_SPAN                                         -> DOC_CODE_SPAN
            MarkdownElementTypes.AUTOLINK                                          -> DOC_AUTO_LINK
            MarkdownElementTypes.INLINE_LINK                                       -> DOC_INLINE_LINK
            MarkdownElementTypes.SHORT_REFERENCE_LINK                              -> DOC_SHORT_REFERENCE_LINK
            MarkdownElementTypes.FULL_REFERENCE_LINK                               -> DOC_FULL_REFERENCE_LINK
            MarkdownElementTypes.LINK_DEFINITION                                   -> DOC_LINK_DEFINITION
            MarkdownElementTypes.LINK_TEXT                                         -> DOC_LINK_TEXT
            MarkdownElementTypes.LINK_LABEL                                        -> DOC_LINK_LABEL
            MarkdownElementTypes.LINK_TITLE                                        -> DOC_LINK_TITLE
            MarkdownElementTypes.LINK_DESTINATION                                  -> DOC_LINK_DESTINATION
            MarkdownElementTypes.CODE_FENCE                                        -> DOC_CODE_FENCE
            MarkdownElementTypes.CODE_BLOCK                                        -> DOC_CODE_BLOCK
            MarkdownElementTypes.HTML_BLOCK                                        -> DOC_HTML_BLOCK
            MarkdownTokenTypes.CODE_FENCE_START, MarkdownTokenTypes.CODE_FENCE_END -> DOC_CODE_FENCE_START_END
            MarkdownTokenTypes.FENCE_LANG                                          -> DOC_CODE_FENCE_LANG
            else                                                                   -> null // `null` means that the node is skipped
        }
    }
}
