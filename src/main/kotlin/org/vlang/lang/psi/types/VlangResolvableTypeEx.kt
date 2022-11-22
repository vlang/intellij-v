package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import org.vlang.lang.psi.VlangNamedElement

interface VlangResolvableTypeEx<T : VlangNamedElement> {
    fun resolve(project: Project): T?
}
