package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.ide.codeInsight.VlangCodeInsightUtil

class VlangSumTypeEx(val name: String, val right: List<VlangTypeEx>, anchor: PsiElement) : VlangBaseTypeEx(anchor), VlangImportableTypeEx {
    override fun toString() = buildString {
        append(name)
        append(" = ")
        append(right.joinToString(" | ") { it.toString() })
    }

    override fun qualifiedName() = name

    override fun readableName(context: PsiElement) = VlangCodeInsightUtil.getQualifiedName(context, anchor!!, name)

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        return true // TODO: implement this
    }

    override fun isEqual(rhs: VlangTypeEx): Boolean {
        return true // TODO: implement this
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        for (right in right) {
            right.accept(visitor)
        }
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx {
        return VlangSumTypeEx(name, right.map { it.substituteGenerics(nameMap) }, anchor!!)
    }
}
