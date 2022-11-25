package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class VlangMapTypeEx(val key: VlangTypeEx, val value: VlangTypeEx, anchor: PsiElement) : VlangBaseTypeEx(anchor) {
    override fun toString() = buildString {
        append("map[")
        append(key)
        append("]")
        append(value)
    }

    override fun qualifiedName(): String = buildString {
        append("map[")
        append(key.qualifiedName())
        append("]")
        append(value.qualifiedName())
    }

    override fun readableName(context: PsiElement) = buildString {
        append("map[")
        append(key.readableName(context))
        append("]")
        append(value.readableName(context))
    }

    override fun module() = value.module()

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

        key.accept(visitor)

        value.accept(visitor)
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx {
        return VlangMapTypeEx(key.substituteGenerics(nameMap), value.substituteGenerics(nameMap), anchor!!)
    }
}
