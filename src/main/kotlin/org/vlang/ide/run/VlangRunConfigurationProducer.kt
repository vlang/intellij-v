package org.vlang.ide.run

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.openapi.components.PathMacroManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.roots.TestSourcesFilter
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.vlang.ide.run.VlangRunConfigurationEditor.RunKind
import org.vlang.lang.psi.VlangFile
import org.vlang.utils.pathAsPath
import kotlin.io.path.relativeTo

class VlangRunConfigurationProducer : LazyRunConfigurationProducer<VlangRunConfiguration>() {
    override fun getConfigurationFactory(): ConfigurationFactory =
        ConfigurationTypeUtil.findConfigurationType(VlangRunConfigurationType.ID)!!
            .configurationFactories[0]

    override fun isConfigurationFromContext(
        configuration: VlangRunConfiguration,
        context: ConfigurationContext,
    ): Boolean {
        val element = context.location?.psiElement ?: return false
        val containingFile = element.containingFile ?: return false
        if (TestSourcesFilter.isTestSources(containingFile.virtualFile, element.project)) {
            return false
        }

        if (containingFile !is VlangFile) {
            return false
        }

        val moduleName = containingFile.getModuleName()
        val withModule = moduleName != null
        val runAsDirectory = withModule && element !is VlangFile

        val expectedKind = if (runAsDirectory) RunKind.Directory else RunKind.File

        return configuration.runKind == expectedKind &&
                configuration.fileName == containingFile.virtualFile.path
    }

    override fun setupConfigurationFromContext(
        configuration: VlangRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>,
    ): Boolean {
        val project = context.project
        val element = sourceElement.get()
        val containingFile = element.containingFile ?: return false
        if (TestSourcesFilter.isTestSources(containingFile.virtualFile, element.project)) {
            return false
        }

        if (containingFile !is VlangFile) {
            return false
        }

        val moduleName = containingFile.getModuleName()
        val withModule = moduleName != null
        val runAsDirectory = withModule && element !is VlangFile

        if (withModule) {
            val configurationName = getModuleConfigurationName(project, moduleName, containingFile) ?: moduleName
            configuration.name = "V Build $configurationName"
        } else {
            configuration.name = "V Run ${containingFile.name}"
        }

        configuration.runKind = if (runAsDirectory) RunKind.Directory else RunKind.File
        configuration.fileName = containingFile.virtualFile.path
        configuration.directory = containingFile.virtualFile.parent.path

        if (configuration.runKind == RunKind.File) {
            configuration.outputDir = configuration.directory
            configuration.workingDir = configuration.directory
        } else {
            configuration.outputDir = path(project, "\$PROJECT_DIR$/bin")
            configuration.workingDir = path(project, "\$PROJECT_DIR$")
        }

        return true
    }

    private fun getModuleConfigurationName(project: Project, moduleName: String?, containingFile: PsiFile): String? {
        if (moduleName != "main") {
            return moduleName
        }

        val containingDir = containingFile.containingDirectory.virtualFile ?: return null
        val projectDir = project.guessProjectDir() ?: return null
        val projectSrcDir = projectDir.findChild("src")

        // under source root
        if (projectDir == containingDir || projectSrcDir == containingDir) {
            return moduleName
        }

        val relative = containingDir.pathAsPath.relativeTo(projectDir.pathAsPath)
        val relativeString = relative.toString()
            .replace('\\', '/')
            .trim('/')

        if (relativeString.isEmpty()) {
            return moduleName
        }

        return "$relativeString/$moduleName"
    }

    private fun path(project: Project, path: String): String =
        PathMacroManager.getInstance(project).expandPath(path)
}
