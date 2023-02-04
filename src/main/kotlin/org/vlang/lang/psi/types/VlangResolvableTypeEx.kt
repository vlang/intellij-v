package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootModificationTracker
import com.intellij.psi.PsiElement
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import org.vlang.lang.psi.VlangNamedElement

abstract class VlangResolvableTypeEx<T : VlangNamedElement>(anchor: PsiElement? = null) : VlangBaseTypeEx(anchor) {
    fun resolve(project: Project): T? = CachedValuesManager.getManager(project).getCachedValue(this) {
        CachedValueProvider.Result(
            resolveImpl(project),
            PsiModificationTracker.MODIFICATION_COUNT,
            ProjectRootModificationTracker.getInstance(project),
        )
    }

    abstract fun resolveImpl(project: Project): T?
}
