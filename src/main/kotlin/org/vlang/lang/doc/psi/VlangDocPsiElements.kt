package org.vlang.lang.doc.psi

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.impl.source.tree.injected.InjectionBackgroundSuppressor
import org.vlang.lang.psi.VlangCompositeElement

interface VlangDocElement : VlangCompositeElement {
    val containingDoc: VlangDocComment

    val markdownValue: String
}

/**
 * A skipped `///`, `//!` or `*` (or other kind of documentation comment decorations)
 * is treated as a comment leaf in the markdown tree
 */
interface VlangDocGap : PsiComment

/**
 * [VlangDocAtxHeading] or [VlangDocSetextHeading]
 */
interface VlangDocHeading : VlangDocElement

/**
 * A [markdown ATX headings](https://spec.commonmark.org/0.29/#atx-heading)
 * ```
 * /// # Header 1
 * /// ## Header 2
 * /// ### Header 3
 * /// #### Header 4
 * /// ##### Header 5
 * /// ###### Header 6
 * ```
 */
interface VlangDocAtxHeading : VlangDocHeading

/**
 * A [markdown Setext headings](https://spec.commonmark.org/0.29/#setext-heading)
 * ```
 * /// Header 1
 * /// ========
 * ///
 * /// Header 2
 * /// --------
 * ```
 */
interface VlangDocSetextHeading : VlangDocHeading

/** *an emphasis span* or _an emphasis span_ */
interface VlangDocEmphasis : VlangDocElement

/** **a strong span** or __a strong span__ */
interface VlangDocStrong : VlangDocElement

/** `a code span` */
interface VlangDocCodeSpan : VlangDocElement

/** <http://example.com> */
interface VlangDocAutoLink : VlangDocElement

interface VlangDocLink : VlangDocElement

/**
 * ```
 * /// [link text](link_destination)
 * /// [link text](link_destination "link title")
 * ```
 */
interface VlangDocInlineLink : VlangDocLink {
    val linkText: VlangDocLinkText
    val linkDestination: VlangDocLinkDestination
}

/**
 * ```
 * /// [link label]
 * ```
 *
 * Then, the link should be defined with [VlangDocLinkDefinition]
 */
interface VlangDocLinkReferenceShort : VlangDocLink {
    val linkLabel: VlangDocLinkLabel
}

/**
 * ```
 * /// [link text][link label]
 * ```
 *
 * Then, the link should be defined with [VlangDocLinkDefinition] (identified by [linkLabel])
 */
interface VlangDocLinkReferenceFull : VlangDocLink {
    val linkText: VlangDocLinkText
    val linkLabel: VlangDocLinkLabel
}

/**
 * ```
 * /// [link label]: link_destination
 * ```
 */
interface VlangDocLinkDefinition : VlangDocLink {
    val linkLabel: VlangDocLinkLabel
    val linkDestination: VlangDocLinkDestination
}

/**
 * A `[LINK TEXT]` part of such links:
 * ```
 * /// [LINK TEXT](link_destination)
 * /// [LINK TEXT][link label]
 * ```
 * Includes brackets (`[`, `]`).
 * A child of [VlangDocInlineLink] or [VlangDocLinkReferenceFull]
 */
interface VlangDocLinkText : VlangDocElement

/**
 * A `[LINK LABEL]` part in these contexts:
 * ```
 * /// [LINK LABEL]
 * /// [link text][LINK LABEL]
 * /// [LINK LABEL]: link_destination
 * ```
 *
 * A link label is used to match *a link reference* with *a link definition*.
 *
 * A child of [VlangDocLinkReferenceShort], [VlangDocLinkReferenceFull] or [VlangDocLinkDefinition]
 */
interface VlangDocLinkLabel : VlangDocElement

/**
 * A `LINK TITLE` (with quotes and parentheses) part in these contexts:
 * ```
 * /// [inline link](http://example.com "LINK TITLE")
 * /// [inline link](http://example.com 'LINK TITLE')
 * /// [inline link](http://example.com (LINK TITLE))
 * ```
 *
 * A child of [VlangDocInlineLink]
 */
interface VlangDocLinkTitle : VlangDocElement

/**
 * A `LINK DESTINATION` part in these contexts:
 * ```
 * /// [link text](LINK DESTINATION)
 * /// [link label]: LINK DESTINATION
 * ```
 *
 * A child of [VlangDocInlineLink] or [VlangDocLinkDefinition]
 */
interface VlangDocLinkDestination : VlangDocElement

/**
 * A [markdown code fence](https://spec.commonmark.org/0.29/#fenced-code-blocks).
 *
 * Provides specific behavior for language injections (see [VlangDoctestLanguageInjector]).
 *
 * [InjectionBackgroundSuppressor] is used to disable builtin background highlighting for injection.
 * We create such background manually by [VlangDoctestAnnotator] (see the class docs)
 */
interface VlangDocCodeFence : VlangDocElement, PsiLanguageInjectionHost, InjectionBackgroundSuppressor {
    val start: VlangDocCodeFenceStartEnd
    val end: VlangDocCodeFenceStartEnd?
    val lang: VlangDocCodeFenceLang?
}

// TODO should be `PsiLanguageInjectionHost` too
interface VlangDocCodeBlock : VlangDocElement

/**
 * See [markdown HTML blocks](https://spec.commonmark.org/0.29/#html-blocks)
 */
interface VlangDocHtmlBlock : VlangDocElement

/**
 * Leading and trailing backtick or tilda sequences of [VlangDocCodeFence].
 *
 * `````
 * /// ```
 * ///  ^ this
 * /// ```
 *      ^ and this
 * `````
 */
interface VlangDocCodeFenceStartEnd : VlangDocElement

/**
 * A child of [VlangDocCodeFence].
 *
 * `````
 * /// ```vlang
 *        ^^^^^ this text
 * `````
 */
interface VlangDocCodeFenceLang : VlangDocElement
