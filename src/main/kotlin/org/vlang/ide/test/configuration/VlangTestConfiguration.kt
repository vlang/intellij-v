package org.vlang.ide.test.configuration

import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.LocatableConfigurationBase
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import org.jdom.Element

open class VlangTestConfiguration(project: Project, factory: ConfigurationFactory, name: String) :
    LocatableConfigurationBase<VlangTestConfigurationOptions>(project, factory, name) {

    override fun getOptions() = super.getOptions() as VlangTestConfigurationOptions

    var scope: VlangTestScope
        get() = options.scope
        set(value) {
            options.scope = value
        }

    var directory: String
        get() = options.directory
        set(value) {
            options.directory = value
        }

    var filename: String
        get() = options.filename
        set(value) {
            options.filename = value
        }

    var pattern: String
        get() = options.pattern
        set(value) {
            options.pattern = value
        }

    var additionalParameters: String
        get() = options.additionalParameters
        set(value) {
            options.additionalParameters = value
        }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        element.writeString("scope", scope.name)
        element.writeString("testFile", filename)
        element.writeString("directory", directory)
        element.writeString("pattern", pattern)
        element.writeString("additionalParameters", additionalParameters)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        element.readString("scope")?.let { scope = VlangTestScope.from(it) }
        element.readString("testFile")?.let { filename = it }
        element.readString("directory")?.let { directory = it }
        element.readString("pattern")?.let { pattern = it }
        element.readString("additionalParameters")?.let { additionalParameters = it }
    }

    override fun getConfigurationEditor() = VlangTestConfigurationEditor(project)

    override fun checkConfiguration() {
        // TODO: add check for class or method?
    }

    override fun getState(executor: Executor, executionEnvironment: ExecutionEnvironment): RunProfileState? {
        return VlangTestConfigurationRunState(executionEnvironment, this)
    }

    override fun suggestedName(): String = "V Test"

    private fun Element.writeString(name: String, value: String) {
        val opt = Element("option")
        opt.setAttribute("name", name)
        opt.setAttribute("value", value)
        addContent(opt)
    }

    private fun Element.readString(name: String): String? =
        children
            .find { it.name == "option" && it.getAttributeValue("name") == name }
            ?.getAttributeValue("value")
}
