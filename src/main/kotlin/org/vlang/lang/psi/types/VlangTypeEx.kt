package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangType

interface VlangTypeEx<T: VlangType?> {
    fun name(): String
    fun qualifiedName(): String
    fun readableName(context: PsiElement): String
    fun module(): String
    fun raw(): T
    fun accept(visitor: VlangTypeVisitor)
    fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean
    fun isEqual(rhs: VlangTypeEx<*>): Boolean
    fun isBuiltin(): Boolean
}
