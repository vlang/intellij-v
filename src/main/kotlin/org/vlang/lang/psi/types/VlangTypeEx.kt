package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.UserDataHolder
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangMethodDeclaration

interface VlangTypeEx : UserDataHolder {
    fun name(): String
    fun qualifiedName(): String
    fun readableName(context: PsiElement): String
    fun module(): String
    fun anchor(): PsiElement?
    fun accept(visitor: VlangTypeVisitor)
    fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean
    fun isEqual(rhs: VlangTypeEx): Boolean
    fun isBuiltin(): Boolean
    fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx
    fun ownMethodsList(project: Project): List<VlangMethodDeclaration>
    fun methodsList(project: Project, visited: MutableSet<VlangTypeEx> = LinkedHashSet(5)): List<VlangMethodDeclaration>
    fun findMethod(project: Project, name: String): VlangMethodDeclaration?
}
