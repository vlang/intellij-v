package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangType

class VlangUnknownTypeEx(raw: VlangType?): VlangBaseTypeEx<VlangType?>(raw) {
    override fun toString(): String = "any"

    override fun qualifiedName() = "any"

    override fun readableName(context: PsiElement): String = "any"

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean = true

    override fun isEqual(rhs: VlangTypeEx<*>): Boolean = true

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }
}
