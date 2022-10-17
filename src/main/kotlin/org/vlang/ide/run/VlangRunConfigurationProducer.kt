package org.vlang.ide.run

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.ConfigurationFromContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.openapi.components.PathMacroManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.TestSourcesFilter
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangFile

class VlangRunConfigurationProducer : LazyRunConfigurationProducer<VlangRunConfiguration>() {
    override fun getConfigurationFactory(): ConfigurationFactory =
        ConfigurationTypeUtil.findConfigurationType(VlangRunConfigurationType.ID)!!
            .configurationFactories[0]

    override fun isConfigurationFromContext(
        configuration: VlangRunConfiguration,
        context: ConfigurationContext
    ): Boolean {
        val element = context.location?.psiElement ?: return false
        val containingFile = element.containingFile ?: return false
        if (containingFile !is VlangFile) {
            return false
        }

        if (TestSourcesFilter.isTestSources(containingFile.virtualFile, element.project)) {
            return false
        }

        // TODO
        return configuration.fileName == containingFile.virtualFile.path
    }

    override fun setupConfigurationFromContext(
        configuration: VlangRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>
    ): Boolean {
        val project = context.project
        val element = sourceElement.get()
        val containingFile = element.containingFile ?: return false
        if (containingFile !is VlangFile) {
            return false
        }

        if (TestSourcesFilter.isTestSources(containingFile.virtualFile, element.project)) {
            return false
        }

        configuration.name = "V Build ${containingFile.name}"
        configuration.runKind = VlangRunConfigurationEditor.RunKind.Directory
        configuration.fileName = containingFile.virtualFile.path
        configuration.directory = containingFile.virtualFile.parent.path
        configuration.outputDir = path(project, "\$PROJECT_DIR$/bin")
        configuration.workingDir = path(project, "\$PROJECT_DIR$")

        return true
    }

    private fun path(project: Project, path: String): String =
        PathMacroManager.getInstance(project).expandPath(path)

    override fun shouldReplace(self: ConfigurationFromContext, other: ConfigurationFromContext) =
        other.configuration !is VlangRunConfiguration
}
