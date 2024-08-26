package io.vlang.ide.test.configuration

import com.intellij.execution.TestStateStorage
import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.execution.testframework.TestIconMapper
import com.intellij.execution.testframework.sm.runner.states.TestStateInfo
import com.intellij.icons.AllIcons
import com.intellij.openapi.roots.TestSourcesFilter
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import io.vlang.ide.test.VlangTestUtil
import io.vlang.ide.ui.VIcons
import io.vlang.lang.VlangTypes
import io.vlang.lang.psi.VlangFunctionDeclaration
import io.vlang.lang.psi.VlangModuleClause
import io.vlang.lang.psi.VlangNamedElement

class VlangTestLineMarkerProvider : RunLineMarkerContributor() {
    private val contextActions = ExecutorAction.getActions(0)

    override fun getInfo(element: PsiElement): Info? {
        if (element.elementType != VlangTypes.IDENTIFIER) return null

        val parent = element.parent
        if (parent is VlangFunctionDeclaration) {
            if (!VlangTestUtil.isTestFunction(parent)) {
                return null
            }

            val icon = getTestStateIcon(getTestState(parent), false)

            return Info(icon, contextActions) { "Run" }
        }

        if (parent is VlangModuleClause) {
            val containingFile = parent.containingFile
            if (!TestSourcesFilter.isTestSources(containingFile.virtualFile, element.project)) {
                return null
            }

            return Info(AllIcons.RunConfigurations.TestState.Run_run, contextActions) { "Run module tests" }
        }

        return null
    }

    companion object {
        fun getTestState(element: VlangNamedElement): TestStateStorage.Record? {
            val fullUrl = VlangTestLocator.getTestUrl(element)
            val urlWithoutRootModuleName = VlangTestLocator.getTestUrlWithoutRootModuleName(element)
            val urlWithoutModuleName = VlangTestLocator.getTestUrlWithoutModuleName(element)
            val storage = TestStateStorage.getInstance(element.project)
            return storage.getState(fullUrl) ?: storage.getState(urlWithoutModuleName) ?: storage.getState(urlWithoutRootModuleName)
        }
    }
}
