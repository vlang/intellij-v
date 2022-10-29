package org.vlang.ide.templates.postfix

import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.VlangAssignmentStatement
import org.vlang.lang.psi.VlangSimpleStatement
import org.vlang.lang.psi.VlangVarDeclaration

object VlangPostfixUtil {
    fun isExpression(context: PsiElement) = context.parentOfType<VlangSimpleStatement>() != null
    fun notInsideVarDeclaration(context: PsiElement) = context.parentOfType<VlangVarDeclaration>() == null
    fun notInsideAssignment(context: PsiElement) = context.parentOfType<VlangAssignmentStatement>() == null

    fun startTemplate(string: String, project: Project, editor: Editor, vararg variables: Pair<String, ConstantNode>) {
        val template = TemplateManager.getInstance(project)
            .createTemplate("templatePostfix", "vlang", string)
        template.isToReformat = true

        variables.forEach { (name, expression) ->
            template.addVariable(name, expression, true)
        }

        TemplateManager.getInstance(project).startTemplate(editor, template)
    }
}