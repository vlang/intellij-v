package org.vlang.lang.doc.psi.impl

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import com.intellij.psi.impl.source.tree.LazyParseablePsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.childrenOfType
import org.vlang.lang.doc.psi.VlangDocCodeFence
import org.vlang.lang.doc.psi.VlangDocComment
import org.vlang.lang.doc.psi.VlangDocLinkDefinition
import org.vlang.lang.psi.VlangConstDeclaration
import org.vlang.lang.psi.VlangGlobalVariableDeclaration
import org.vlang.lang.psi.VlangModuleClause
import org.vlang.lang.psi.VlangNamedElement

/**
 * @param text a text for lazy parsing. `null` value means that the element is parsed ([isParsed] is `true`)
 */
class VlangDocCommentImpl(type: IElementType, text: CharSequence?) : LazyParseablePsiElement(type, text), VlangDocComment {
    override fun getTokenType(): IElementType = elementType

    /** Needed for URL references ([com.intellij.openapi.paths.WebReference]) */
    override fun getReferences(): Array<PsiReference> {
        return ReferenceProvidersRegistry.getReferencesFromProviders(this)
    }

    // Needed for VlangFoldingBuilder
    override fun accept(visitor: PsiElementVisitor) {
        visitor.visitComment(this)
    }

    override fun toString(): String {
        return "PsiComment($elementType)"
    }

    override fun getOwner(): VlangNamedElement? {
        val element = skipSiblingsForward(this, PsiComment::class.java, PsiWhiteSpace::class.java) ?: return getCommentModuleOwner()
        if (element is VlangModuleClause) {
            return getCommentModuleOwner()
        }

        // treat as documentation if there is a global variable declaration with one definition
        // __global goo = 1
        if (element is VlangGlobalVariableDeclaration && !element.isMultiline) {
            val defs = element.globalVariableDefinitionList
            if (defs.size == 1) {
                return defs[0]
            }
        }

        // treat as documentation if there is a constant declaration with one definition
        //  const name = 0
        if (element is VlangConstDeclaration && !element.isMultiline) {
            val defs = element.constDefinitionList
            if (defs.size == 1) {
                return defs[0]
            }
        }

        return element as? VlangNamedElement ?: getCommentModuleOwner()
    }

    override fun getOwnerNameRangeInElement(): TextRange? {
        if (!isValidDoc()) return null
        val commentText = text
        val firstWordStart = commentText.indexOfFirst { !it.isWhitespace() && it != '/' }
        if (firstWordStart == -1) return null
        val firstWordEnd = commentText.substring(firstWordStart).indexOfFirst { it.isWhitespace() || it == '\n' }
        if (firstWordEnd == -1) return null

        return TextRange(firstWordStart, firstWordStart + firstWordEnd)
    }

    override fun isValidDoc(): Boolean {
        val owner = owner ?: return true
        val commentText = text
        val preparedCommentText = commentText.removePrefix("//").trim()
        val endOfFirstWord = preparedCommentText.indexOfFirst { it.isWhitespace() || it == '\n' }
        val firstWord = if (endOfFirstWord != -1) {
            preparedCommentText.substring(0, endOfFirstWord).trim()
        } else {
            preparedCommentText
        }

        val ownerName = owner.name ?: return true
        return ownerName == firstWord
    }

    private fun getCommentModuleOwner(): VlangModuleClause? {
        val element = skipSiblingsBackward(this, PsiComment::class.java, PsiWhiteSpace::class.java) ?: return null
        return element as? VlangModuleClause
    }

    private fun skipSiblingsForward(element: PsiElement?, vararg elementClasses: Class<out PsiElement?>): PsiElement? {
        if (element != null) {
            var e = element.nextSibling
            while (e != null) {
                if (e is PsiWhiteSpace && e.text.startsWith("\n\n")) {
                    return null
                }

                if (!PsiTreeUtil.instanceOf(e, *elementClasses)) {
                    return e
                }
                e = e.nextSibling
            }
        }
        return null
    }

    private fun skipSiblingsBackward(element: PsiElement?, vararg elementClasses: Class<out PsiElement?>): PsiElement? {
        if (element != null) {
            var e = element.prevSibling
            while (e != null) {
                if (e is PsiComment) {
                    return null
                }

                if (!PsiTreeUtil.instanceOf(e, *elementClasses)) {
                    return e
                }
                e = e.prevSibling
            }
        }
        return null
    }

    override val codeFences: List<VlangDocCodeFence>
        get() = childrenOfType()

    override val linkDefinitions: List<VlangDocLinkDefinition>
        get() = childrenOfType()

    override val linkReferenceMap: Map<String, VlangDocLinkDefinition>
        get() = CachedValuesManager.getCachedValue(this) {
            val result = linkDefinitions.associateBy { it.linkLabel.markdownValue }
            CachedValueProvider.Result(result, containingFile)
        }
}
