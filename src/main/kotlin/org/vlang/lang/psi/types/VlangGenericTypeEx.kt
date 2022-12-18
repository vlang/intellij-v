package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class VlangGenericTypeEx(private val name: String, anchor: PsiElement) : VlangBaseTypeEx(anchor) {
    override fun toString() = name

    override fun qualifiedName(): String = name

    override fun readableName(context: PsiElement) = name

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        return true // until generic constraints
    }

    override fun isEqual(rhs: VlangTypeEx): Boolean {
        return rhs is VlangGenericTypeEx && rhs.qualifiedName() == qualifiedName()
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx {
        return nameMap[name] ?: this
    }
}
