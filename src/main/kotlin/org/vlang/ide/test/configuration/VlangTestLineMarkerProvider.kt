package org.vlang.ide.test.configuration

import com.intellij.execution.TestStateStorage
import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.execution.testframework.TestIconMapper
import com.intellij.execution.testframework.sm.runner.states.TestStateInfo
import com.intellij.icons.AllIcons
import com.intellij.openapi.roots.TestSourcesFilter
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import org.vlang.ide.test.VlangTestUtil
import org.vlang.ide.ui.VIcons
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.VlangFunctionDeclaration
import org.vlang.lang.psi.VlangModuleClause
import org.vlang.lang.psi.VlangNamedElement

class VlangTestLineMarkerProvider : RunLineMarkerContributor() {
    private val contextActions = ExecutorAction.getActions(0)

    override fun getInfo(element: PsiElement): Info? {
        if (element.elementType != VlangTypes.IDENTIFIER) return null

        val parent = element.parent
        if (parent is VlangFunctionDeclaration) {
            if (!VlangTestUtil.isTestFunction(parent)) {
                return null
            }

            val magnitude = getTestState(parent)
                ?.let { TestIconMapper.getMagnitude(it.magnitude) }

            val icon = when (magnitude) {
                TestStateInfo.Magnitude.PASSED_INDEX,
                TestStateInfo.Magnitude.COMPLETE_INDEX,
                     -> VIcons.TestGreen

                TestStateInfo.Magnitude.ERROR_INDEX,
                TestStateInfo.Magnitude.FAILED_INDEX,
                     -> VIcons.TestRed

                else -> VIcons.Test
            }

            return Info(icon, { "Run" }, *contextActions)
        }

        if (parent is VlangModuleClause) {
            val containingFile = parent.containingFile
            if (!TestSourcesFilter.isTestSources(containingFile.virtualFile, element.project)) {
                return null
            }

            return Info(AllIcons.RunConfigurations.TestState.Run_run, { "Run module tests" }, *contextActions)
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
