package org.vlang.ide.documentation

import com.intellij.codeEditor.printing.HTMLTextPainter
import com.intellij.codeInsight.documentation.DocumentationManagerProtocol
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.psi.PsiElement
import com.intellij.ui.ColorHexUtil
import com.intellij.ui.ColorUtil
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.MarkdownFlavourDescriptor
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.html.*
import org.intellij.markdown.html.entities.EntityConverter
import org.intellij.markdown.parser.LinkMap
import org.intellij.markdown.parser.MarkdownParser
import org.vlang.lang.doc.psi.VlangDocComment
import org.vlang.lang.doc.psi.VlangDocKind
import org.vlang.utils.indexesOf
import java.net.URI

enum class VlangDocRenderMode {
    QUICK_DOC_POPUP,
    INLINE_DOC_COMMENT
}

fun documentationAsHtml(text: String, context: PsiElement): String {
    val flavour = VDocMarkdownFlavourDescriptor(context, null, VlangDocRenderMode.QUICK_DOC_POPUP)
    val root = MarkdownParser(flavour).buildMarkdownTreeFromString(text)
    return HtmlGenerator(text, root, flavour).generateHtml()
        .replace("psi://element/", DocumentationManagerProtocol.PSI_ELEMENT_PROTOCOL)
}

fun VlangDocComment.documentationAsHtml(renderMode: VlangDocRenderMode = VlangDocRenderMode.QUICK_DOC_POPUP): String {
    val documentationText = VlangDocKind.of(tokenType)
        .removeDecoration(text)
        .joinToString("\n")

    return documentationAsHtml(documentationText, this, renderMode)
}

private fun documentationAsHtml(
    rawDocumentationText: String,
    context: VlangDocComment,
    renderMode: VlangDocRenderMode,
): String {
    val documentationText = try {
        processDocumentationText(context, rawDocumentationText)
    } catch (e: Exception) {
        rawDocumentationText
    }

    val flavour = VDocMarkdownFlavourDescriptor(context, null, renderMode)
    val root = MarkdownParser(flavour).buildMarkdownTreeFromString(documentationText)
    return HtmlGenerator(documentationText, root, flavour).generateHtml()
        .replace("psi://element/", DocumentationManagerProtocol.PSI_ELEMENT_PROTOCOL)
}

fun substringToNextLine(string: String, start: Int): String {
    val end = string.indexOf('\n', start)
    return if (end == -1) string.substring(start) else string.substring(start, end)
}

fun processDocumentationText(comment: VlangDocComment, text: String): String {
    val owner = comment.owner ?: return text
    val ownerName = owner.name ?: return text

    var newText = text

    val firstWord = newText.substringBefore(' ', "")
    if (firstWord == ownerName) {
        newText = newText.replaceRange(IntRange(0, firstWord.length), "[$ownerName] ")
    }

    var shift = 0
    val exampleIndices = newText.indexesOf("Example:")
    for (rawIndex in exampleIndices) {
        val index = rawIndex + shift
        val line = substringToNextLine(newText, index).removePrefix("Example:")
        newText = if (line.isEmpty() || line.isBlank()) {
            val before = "Example:$line"
            val after = "<br>\n\n# Example:\n"
            shift += after.length - before.length
            newText.replaceRange(IntRange(index, index + before.length - 1), after)
        } else {
            val before = "Example:$line"
            val after = "<p></p>\n\n# Example:\n```\n$line\n```\n"
            shift += after.length - before.length
            newText.replaceRange(IntRange(index, index + before.length - 1), after)
        }
    }

    shift = 0
    val noteIndices = newText.indexesOf("Note:")
    for (rawIndex in noteIndices) {
        val index = rawIndex + shift
        val line = substringToNextLine(newText, index).removePrefix("Note:")
        if (line.endsWith(".") || line.endsWith("!") || line.endsWith("?")) {
            val before = "Note:$line"
            val after = "\n\n**Note**: $line\n\n\n"
            shift += after.length - before.length
            newText = newText.replaceRange(IntRange(index, index + before.length - 1), after)
        }
    }

    // Comments spanning multiple lines are merged together using spaces, unless
    //
    // - the line is empty
    // - the line ends with a . (end of sentence)
    // - the line is purely of at least 3 of -, =, _, *, ~ (horizontal rule)
    // - the line starts with at least one # followed by a space (header)
    // - the line starts and ends with a | (table)
    // - the line starts with - (list)

    val lines = newText.lines()
    val newLines = lines.map { line ->
        if (line.endsWith(".") || line.endsWith("!") || line.endsWith("?") ||
            line.matches(Regex("^[-=_*~]{3,}\$")) ||
            line.endsWith("|") ||
            line.startsWith("|") ||
            line.startsWith("-")
        ) {
            line + "\n\n"
        } else {
            line
        }
    }

    return newLines.joinToString("\n")
}

private class VDocMarkdownFlavourDescriptor(
    private val context: PsiElement,
    private val uri: URI? = null,
    private val renderMode: VlangDocRenderMode,
    private val gfm: MarkdownFlavourDescriptor = GFMFlavourDescriptor(useSafeLinks = false, absolutizeAnchorLinks = true),
) : MarkdownFlavourDescriptor by gfm {

    override fun createHtmlGeneratingProviders(linkMap: LinkMap, baseURI: URI?): Map<IElementType, GeneratingProvider> {
        val generatingProviders = HashMap(gfm.createHtmlGeneratingProviders(linkMap, uri ?: baseURI))
        // Filter out MARKDOWN_FILE to avoid producing unnecessary <body> tags
        generatingProviders.remove(MarkdownElementTypes.MARKDOWN_FILE)
        // h1 and h2 are too large
        generatingProviders[MarkdownElementTypes.ATX_1] = SimpleTagProvider("h2")
        generatingProviders[MarkdownElementTypes.ATX_2] = SimpleTagProvider("h3")
        generatingProviders[MarkdownElementTypes.CODE_FENCE] = VlangCodeFenceProvider(context, renderMode)

        generatingProviders[MarkdownElementTypes.SHORT_REFERENCE_LINK] =
            VlangReferenceLinksGeneratingProvider(linkMap, uri ?: baseURI, resolveAnchors = true)
        generatingProviders[MarkdownElementTypes.FULL_REFERENCE_LINK] =
            VlangReferenceLinksGeneratingProvider(linkMap, uri ?: baseURI, resolveAnchors = true)
        generatingProviders[MarkdownElementTypes.INLINE_LINK] =
            VlangInlineLinkGeneratingProvider(uri ?: baseURI, resolveAnchors = true)

        return generatingProviders
    }
}

// Inspired by org.intellij.markdown.html.CodeFenceGeneratingProvider
private class VlangCodeFenceProvider(
    private val context: PsiElement,
    private val renderMode: VlangDocRenderMode,
) : GeneratingProvider {

    override fun processNode(visitor: HtmlGenerator.HtmlGeneratingVisitor, text: String, node: ASTNode) {
        val indentBefore = node.getTextInNode(text).commonPrefixWith(" ".repeat(10)).length

        val codeText = StringBuilder()

        var childrenToConsider = node.children
        if (childrenToConsider.last().type == MarkdownTokenTypes.CODE_FENCE_END) {
            childrenToConsider = childrenToConsider.subList(0, childrenToConsider.size - 1)
        }

        var isContentStarted = false
        var lastChildWasContent = false

        loop@ for (child in childrenToConsider) {
            if (isContentStarted && child.type in listOf(MarkdownTokenTypes.CODE_FENCE_CONTENT, MarkdownTokenTypes.EOL)) {
                val rawLine = HtmlGenerator.trimIndents(child.getTextInNode(text), indentBefore)
                codeText.append(rawLine)
                lastChildWasContent = child.type == MarkdownTokenTypes.CODE_FENCE_CONTENT
            }

            if (!isContentStarted && child.type == MarkdownTokenTypes.EOL) {
                isContentStarted = true
            }
        }
        if (lastChildWasContent) {
//            codeText.appendLine()
        }

        visitor.consumeHtml(convertToHtmlWithHighlighting(codeText.toString()))
    }

    private fun convertToHtmlWithHighlighting(codeText: String): String {
        var htmlCodeText = HTMLTextPainter.convertCodeFragmentToHTMLFragmentWithInlineStyles(context, codeText)

        // TODO: use scheme of concrete editor instead of global one because they may differ
        val scheme = EditorColorsManager.getInstance().globalScheme
        htmlCodeText = htmlCodeText.replaceFirst(
            "<pre>",
            "<pre style=\"text-indent: ${CODE_SNIPPET_INDENT}px;\">"
        )

        return when (renderMode) {
            VlangDocRenderMode.INLINE_DOC_COMMENT -> htmlCodeText.dimColors(scheme)
            else                                  -> htmlCodeText
        }
    }

    private fun String.dimColors(scheme: EditorColorsScheme): String {
        val alpha = if (isColorSchemeDark(scheme)) DARK_THEME_ALPHA else LIGHT_THEME_ALPHA

        return replace(COLOR_PATTERN) { result ->
            val colorHexValue = result.groupValues[1]
            val fgColor = ColorHexUtil.fromHexOrNull(colorHexValue) ?: return@replace result.value
            val bgColor = scheme.defaultBackground
            val finalColor = ColorUtil.mix(bgColor, fgColor, alpha)

            "color: #${ColorUtil.toHex(finalColor)}"
        }
    }

    private fun isColorSchemeDark(scheme: EditorColorsScheme): Boolean {
        return ColorUtil.isDark(scheme.defaultBackground)
    }

    companion object {
        private val COLOR_PATTERN = """color:\s*#(\p{XDigit}{3,})""".toRegex()

        private const val CODE_SNIPPET_INDENT = 20
        private const val LIGHT_THEME_ALPHA = 0.6
        private const val DARK_THEME_ALPHA = 0.78
    }
}


open class VlangReferenceLinksGeneratingProvider(private val linkMap: LinkMap, baseURI: URI?, resolveAnchors: Boolean) :
    ReferenceLinksGeneratingProvider(linkMap, baseURI, resolveAnchors) {
    override fun renderLink(visitor: HtmlGenerator.HtmlGeneratingVisitor, text: String, node: ASTNode, info: RenderInfo) {
        super.renderLink(visitor, text, node, info.copy(destination = markLinkAsLanguageItemIfItIsVPath(info.destination)))
    }

    override fun getRenderInfo(text: String, node: ASTNode): RenderInfo? {
        val label = node.children.firstOrNull { it.type == MarkdownElementTypes.LINK_LABEL } ?: return null
        val labelText = label.getTextInNode(text)

        val linkInfo = linkMap.getLinkInfo(labelText)
        val (linkDestination, linkTitle) = if (linkInfo != null) {
            linkInfo.destination to linkInfo.title
        } else {
            val linkText = labelText.removeSurrounding("[", "]").removeSurrounding("`")
            if (!linkIsProbablyValidVPath(linkText)) return null
            linkText to null
        }

        val linkTextNode = node.children.firstOrNull { it.type == MarkdownElementTypes.LINK_TEXT }
        return RenderInfo(
            linkTextNode ?: label,
            EntityConverter.replaceEntities(linkDestination, processEntities = true, processEscapes = true),
            linkTitle?.let { EntityConverter.replaceEntities(it, processEntities = true, processEscapes = true) }
        )
    }
}

open class VlangInlineLinkGeneratingProvider(baseURI: URI?, resolveAnchors: Boolean) :
    InlineLinkGeneratingProvider(baseURI, resolveAnchors) {
    override fun renderLink(visitor: HtmlGenerator.HtmlGeneratingVisitor, text: String, node: ASTNode, info: RenderInfo) {
        super.renderLink(visitor, text, node, info.copy(destination = markLinkAsLanguageItemIfItIsVPath(info.destination)))
    }
}

private fun linkIsProbablyValidVPath(link: CharSequence): Boolean {
    return link.none { it in "/.#" || it.isWhitespace() }
}

private fun markLinkAsLanguageItemIfItIsVPath(link: CharSequence): CharSequence {
    return if (linkIsProbablyValidVPath(link)) "${DocumentationManagerProtocol.PSI_ELEMENT_PROTOCOL}$link" else link
}
