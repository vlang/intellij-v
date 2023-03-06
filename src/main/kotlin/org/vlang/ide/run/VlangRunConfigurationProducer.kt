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
import org.vlang.lang.psi.VlangSimpleStatement
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

        val runAsModule = needRunFileAsModule(containingFile)
        val expectedKind = if (runAsModule) RunKind.Directory else RunKind.File

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

        val runAsModule = needRunFileAsModule(containingFile)
        if (runAsModule) {
            val configurationName = getModuleConfigurationName(project, moduleName, containingFile) ?: moduleName ?: "main"
            configuration.name = "V Build $configurationName"
        } else {
            configuration.name = "V Run ${containingFile.name}"
        }

        configuration.runKind = if (runAsModule) RunKind.Directory else RunKind.File
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

        val containingDir = containingFile.containingDirectory.virtualFile
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

    /**
     * When the user tries to run a file through the Run icon, we need to
     * figure out how to run that file, either as a single file or as a module.
     * If the file has a `module` section, then we check if that file has
     * top level expressions:
     *
     * For example:
     * ```v
     * module main
     *
     * import os
     *
     * os.read_file('file.txt') or { panic(err) } // top level expression
     * ```
     * Should be run as a single file, because when run as a module,
     * an error will be given for an invalid top level statement.
     *
     * If the file does not have a `module` section and does not have top level
     * expressions, then we need to check if there is a file next to this file
     * for another platform.
     * For example:
     * ```
     * main.v
     * main_windows.v
     * ```
     * Should be run as a module, because in this case files for different
     * platforms will be assembled in one module.
     */
    private fun needRunFileAsModule(file: VlangFile): Boolean {
        val moduleName = file.getModuleName()
        if (moduleName != null) {
            return !hasTopLevelExpressions(file)
        }

        return hasFilesForOtherPlatforms(file)
    }

    private fun hasTopLevelExpressions(file: VlangFile): Boolean {
        return file.children.any { it is VlangSimpleStatement }
    }

    private fun hasFilesForOtherPlatforms(file: VlangFile): Boolean {
        val fileName = file.virtualFile.name.split(".").firstOrNull() ?: file.virtualFile.nameWithoutExtension
        val directory = file.containingDirectory?.virtualFile ?: return false
        return directory.children.any {
            val childrenName = it.name.split(".").firstOrNull() ?: it.nameWithoutExtension
            val platformName = childrenName.substringAfterLast("_", "unknown")
            if (platformName == "unknown") {
                return@any false
            }

            val nameWithoutPlatform = childrenName.substringBeforeLast("_", childrenName)
            nameWithoutPlatform == fileName && platformName in KNOWN_PLATFORMS
        }
    }

    private fun path(project: Project, path: String): String =
        PathMacroManager.getInstance(project).expandPath(path)

    companion object {
        val KNOWN_PLATFORMS = listOf(
            "default", "windows", "linux", "macos", "mac", "darwin", "ios", "android", "mach", "dragonfly", "gnu", "hpux", "haiku", "qnx", "solaris"
        )
    }
}
