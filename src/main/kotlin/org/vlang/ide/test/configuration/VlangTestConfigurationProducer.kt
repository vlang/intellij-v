package org.vlang.ide.test.configuration

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.ConfigurationFromContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.openapi.roots.TestSourcesFilter
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangFunctionDeclaration
import org.vlang.lang.psi.VlangModuleClause
import org.vlang.utils.isTestFile

class VlangTestConfigurationProducer : LazyRunConfigurationProducer<VlangTestConfiguration>() {
    override fun getConfigurationFactory(): ConfigurationFactory =
        ConfigurationTypeUtil.findConfigurationType(VlangTestConfigurationType.ID)!!
            .configurationFactories[0]

    override fun isConfigurationFromContext(
        configuration: VlangTestConfiguration,
        context: ConfigurationContext,
    ): Boolean {
        val element = getSourceElement(context.location?.psiElement) ?: return false
        if (element is PsiDirectory) {
            return configuration.scope == VlangTestScope.Directory &&
                    configuration.directory == element.virtualFile.path
        }

        val parent = element.parent ?: element.containingFile
        val containingFile = element.containingFile ?: return false
        if (containingFile !is VlangFile) {
            return false
        }

        if (!TestSourcesFilter.isTestSources(containingFile.virtualFile, element.project)) {
            return false
        }

        if (parent is VlangModuleClause) {
            val containingDir = containingFile.parent ?: return false
            return configuration.scope == VlangTestScope.Directory &&
                    configuration.directory == containingDir.virtualFile.path
        }

        if (parent is VlangFunctionDeclaration) {
            val functionName = parent.name
            return configuration.scope == VlangTestScope.Function &&
                    configuration.filename == containingFile.virtualFile.path &&
                    configuration.pattern == "*.$functionName"
        }

        return configuration.scope == VlangTestScope.File &&
                configuration.directory == containingFile.virtualFile.parent.path &&
                configuration.filename == containingFile.virtualFile.path
    }

    override fun setupConfigurationFromContext(
        configuration: VlangTestConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>,
    ): Boolean {
        val element = getSourceElement(sourceElement.get()) ?: return false

        if (element is PsiDirectory) {
            configuration.scope = VlangTestScope.Directory
            configuration.name = "V Test ${element.name}"
            configuration.directory = element.virtualFile.path
            configuration.filename = element.virtualFile.children.find { it.isTestFile }?.path ?: ""

            return true
        }

        val parent = element.parent ?: return false
        val containingFile = element.containingFile ?: return false
        if (containingFile !is VlangFile) {
            return false
        }

        if (!TestSourcesFilter.isTestSources(containingFile.virtualFile, element.project)) {
            return false
        }

        if (parent is VlangModuleClause) {
            val directory = containingFile.virtualFile.parent
            configuration.scope = VlangTestScope.Directory
            configuration.name = "V Test ${directory.name}"
            configuration.directory = directory.path

            return true
        }

        if (parent is VlangFunctionDeclaration) {
            val functionName = parent.name
            configuration.scope = VlangTestScope.Function
            configuration.name = "V Test $functionName"
            configuration.directory = containingFile.virtualFile.parent.path
            configuration.filename = containingFile.virtualFile.path
            configuration.pattern = "*.$functionName"

            return true
        }

        val file = containingFile.virtualFile.name
        configuration.scope = VlangTestScope.File
        configuration.name = "V Test $file"
        configuration.directory = containingFile.virtualFile.parent.path
        configuration.filename = containingFile.virtualFile.path

        return true
    }

    private fun getSourceElement(sourceElement: PsiElement?): PsiElement? {
        if (sourceElement is VlangFunctionDeclaration) {
            return sourceElement.nameIdentifier
        }
        return sourceElement
    }

    override fun shouldReplace(self: ConfigurationFromContext, other: ConfigurationFromContext) =
        other.configuration !is VlangTestConfiguration
}
