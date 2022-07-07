package org.vlang.lang.psi.impl

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.ResolveResult
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.ArrayUtil
import org.vlang.lang.psi.VlangCallExpr
import org.vlang.lang.psi.VlangReferenceExpressionBase
import org.vlang.lang.stubs.index.VlangFunctionIndex
import org.vlang.lang.stubs.index.VlangStructIndex
import org.vlang.lang.stubs.index.VlangTypeAliasIndex
import org.vlang.lang.stubs.index.VlangUnionIndex

class VlangReference(private val el: VlangReferenceExpressionBase) :
    VlangReferenceBase<VlangReferenceExpressionBase>(
        el,
        TextRange.from(el.getIdentifier()?.startOffsetInParent ?: 0, el.getIdentifier()?.textLength ?: el.textLength)
    ) {
    override fun isReferenceTo(element: PsiElement): Boolean {
        return true
    }

    private val identifier: PsiElement?
        get() = myElement?.getIdentifier()

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val project = el.project

        val name = identifier?.text ?: return emptyArray()

        val res = when {
            el.parent is VlangCallExpr -> {
                VlangFunctionIndex.find(name, project, GlobalSearchScope.allScope(project), null)
            }
            else -> {
                val structs = VlangStructIndex.find(name, project, GlobalSearchScope.allScope(project), null)
                structs.ifEmpty {
                    VlangTypeAliasIndex.find(name, project, GlobalSearchScope.allScope(project), null).ifEmpty {
                        VlangUnionIndex.find(name, project, GlobalSearchScope.allScope(project), null)
                    }
                }
            }
        }

        return res
            .map { PsiElementResolveResult(it) }
            .toTypedArray()
    }

    override fun getVariants() = ArrayUtil.EMPTY_OBJECT_ARRAY

    override fun handleElementRename(newElementName: String): PsiElement? {
        return identifier
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VlangReference

        if (element != other.element) return false

        return true
    }

    override fun hashCode(): Int {
        return element.hashCode()
    }

}