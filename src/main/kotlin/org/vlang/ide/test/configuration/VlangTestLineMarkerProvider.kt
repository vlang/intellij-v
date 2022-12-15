package org.vlang.ide.test.configuration

import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.openapi.roots.TestSourcesFilter
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import org.vlang.ide.test.VlangTestUtil
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.VlangFunctionDeclaration
import org.vlang.lang.psi.VlangModuleClause

class VlangTestLineMarkerProvider : RunLineMarkerContributor() {
    private val contextAction =
        ExecutorAction.getActions(0).firstOrNull { it.toString().startsWith("Run context configuration") }

    override fun getInfo(element: PsiElement): Info? {
        if (element.elementType == VlangTypes.IDENTIFIER) {
            val parent = element.parent
            if (parent is VlangFunctionDeclaration) {
                if (!VlangTestUtil.isTestFunction(parent)) {
                    return null
                }

                return Info(AllIcons.RunConfigurations.TestState.Run, { "Run" }, contextAction)
            }

            if (parent is VlangModuleClause) {
                val containingFile = parent.containingFile
                if (!TestSourcesFilter.isTestSources(containingFile.virtualFile, element.project)) {
                    return null
                }

                return Info(AllIcons.RunConfigurations.TestState.Run_run, { "Run module tests" }, contextAction)
            }

            return null
        }

        return null
    }
}
