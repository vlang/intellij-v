package org.vlang.ide.templates.postfix

import com.intellij.codeInsight.template.Expression
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.VlangAssignmentStatement
import org.vlang.lang.psi.VlangExpression
import org.vlang.lang.psi.VlangSimpleStatement
import org.vlang.lang.psi.VlangVarDeclaration
import org.vlang.lang.psi.types.VlangMapTypeEx

object VlangPostfixUtil {
    fun isExpression(context: PsiElement) = context.parentOfType<VlangSimpleStatement>() != null
    fun notInsideVarDeclaration(context: PsiElement) = context.parentOfType<VlangVarDeclaration>() == null
    fun notInsideAssignment(context: PsiElement) = context.parentOfType<VlangAssignmentStatement>() == null
    fun isMapType(context: PsiElement) = context.parentOfType<VlangExpression>()?.getType(null) is VlangMapTypeEx

    fun startTemplate(string: String, project: Project, editor: Editor, vararg variables: Pair<String, Expression>) {
        val template = TemplateManager.getInstance(project)
            .createTemplate("templatePostfix", "vlang", string)
        template.isToReformat = true

        variables.forEach { (name, expression) ->
            if (expression is ConstantNode) {
                template.addVariable(name, expression, true)
                return@forEach
            }

            template.addVariable(name, expression, ConstantNode("_"), true)
        }

        TemplateManager.getInstance(project).startTemplate(editor, template)
    }
}