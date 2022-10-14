package org.vlang.ide.refactoring

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.vlang.lang.psi.VlangExpression
import org.vlang.lang.psi.VlangVarDefinition

data class VlangIntroduceOperation(
    var project: Project,
    var editor: Editor,
    var file: PsiFile,
    var expression: VlangExpression? = null,
    var occurrences: List<PsiElement?> = emptyList(),
    var name: String = "",
    var variable: VlangVarDefinition? = null,
    var replaceAll: Boolean = false,
)
