package org.vlang.lang.psi.impl

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.ResolveResult
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.parentOfType
import com.intellij.util.ArrayUtil
import org.vlang.lang.psi.*
import org.vlang.lang.stubs.index.VlangFunctionIndex
import org.vlang.lang.stubs.index.VlangNamesIndex
import org.vlang.lang.utils.LabelUtil

class VlangReference(private val el: VlangReferenceExpressionBase) :
    VlangReferenceBase<VlangReferenceExpressionBase>(
        el,
        TextRange.from(el.getIdentifier()?.startOffsetInParent ?: 0, el.getIdentifier()?.textLength ?: el.textLength)
    ) {
    override fun isReferenceTo(element: PsiElement) = true

    private val identifier: PsiElement?
        get() = myElement?.getIdentifier()

    private fun getFqn(): String? {
        val name = identifier?.text ?: return null
        val containingFile = el.containingFile as VlangFile

        val parentTypeDecl = el.parentOfType<VlangTypeDecl>()
        if (parentTypeDecl != null && parentTypeDecl.typeReferenceExpressionList.size > 1) {
            val list = parentTypeDecl.typeReferenceExpressionList
            val fqnWithoutName = parentTypeDecl.typeReferenceExpressionList.subList(0, list.size - 1)
                .joinToString(".") { it.text }
            val importName = containingFile.resolveName(fqnWithoutName)
            if (parentTypeDecl.typeReferenceExpressionList.first() == el && importName != "") {
                return importName
            }

            val typeName = parentTypeDecl.getIdentifier()?.text

            if (importName != null && importName != "" && typeName != null) {
                return "$importName.$typeName"
            }
        }

        val parentDotCall = el.parentOfType<VlangDotExpression>()
        if (parentDotCall != null) {
            val exprText = parentDotCall.expression.text
            val importName = containingFile.resolveName(exprText)
            if (parentDotCall.expression == el) {
                return importName
            }

            val methodName = parentDotCall.methodCall?.referenceExpression?.getIdentifier()?.text

            return if (importName != null && methodName != null) {
                "$importName.$methodName"
            } else {
                null
            }
        }

        return containingFile.resolveName(name)
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val project = el.project
        val name = identifier?.text ?: return emptyArray()

        if (myElement is VlangLabelRef) {
            return resolveLabelRef(name)
        }

        val fqn = getFqn() ?: return emptyArray()

        val res = when {
            el.parent is VlangCallExpr -> {
                VlangFunctionIndex.find(fqn, name, project, GlobalSearchScope.allScope(project), null)
            }
            else -> {
                VlangNamesIndex.find(fqn, name, project, GlobalSearchScope.allScope(project), null)
            }
        }

        return res
            .map { PsiElementResolveResult(it) }
            .toTypedArray()
    }

    private fun resolveLabelRef(name: String): Array<ResolveResult> {
        val labels = LabelUtil.collectContextLabels(myElement)
        val label = labels.find { it.labelRef.name == name } ?: return emptyArray()
        return arrayOf(PsiElementResolveResult(label))
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
