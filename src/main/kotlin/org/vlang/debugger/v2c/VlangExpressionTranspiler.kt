package org.vlang.debugger.v2c

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.parentOfType
import com.jetbrains.cidr.execution.debugger.backend.DebuggerCommandException
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangStatement
import org.vlang.lang.psi.impl.VlangElementFactory

class VlangExpressionTranspiler {
    fun transpile(project: Project, file: VirtualFile, offset: Int?, expr: String): String {
        val offsetInFile = offset ?: 0
        val originalFile = PsiManager.getInstance(project).findFile(file) as VlangFile
        val copyFile = originalFile.copy() as VlangFile

        val element = copyFile.findElementAt(offsetInFile + 1)
        val parentStatement = element?.parentOfType<VlangStatement>() ?: return expr

        val newFile = VlangElementFactory.createFileFromText(project, expr)
        val expression = newFile.firstChild.children[0].children[0]

        val parentForStatement = parentStatement.parent
        val addedExpression = parentForStatement.addBefore(expression, parentStatement)
        parentForStatement.addBefore(VlangElementFactory.createNewLine(project), parentStatement)

        val evaluator = VlangExpressionEvaluator()
        try {
            addedExpression.accept(evaluator)
        } catch (e: Exception) {
            throw DebuggerCommandException(e.message ?: "Error while transpiling expression")
        }
        return evaluator.text()
    }
}
