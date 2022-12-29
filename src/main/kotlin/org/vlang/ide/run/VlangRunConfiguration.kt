package org.vlang.ide.run

import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.LocatableConfiguration
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import org.jdom.Element

open class VlangRunConfiguration(project: Project, factory: ConfigurationFactory?, name: String?) :
    RunConfigurationBase<VlangRunConfigurationOptions>(project, factory, name),
    LocatableConfiguration {

    override fun getOptions() = super.getOptions() as VlangRunConfigurationOptions

    var runKind: VlangRunConfigurationEditor.RunKind
        get() = options.runKind
        set(value) {
            options.runKind = value
        }

    var fileName: String
        get() = options.fileName
        set(value) {
            options.fileName = value
        }

    var directory: String
        get() = options.directory
        set(value) {
            options.directory = value
        }

    var outputDir: String
        get() = options.outputDir
        set(value) {
            options.outputDir = value
        }

    var runAfterBuild: Boolean
        get() = options.runAfterBuild
        set(value) {
            options.runAfterBuild = value
        }

    var workingDir: String
        get() = options.workingDir
        set(value) {
            options.workingDir = value
        }

    var envs: MutableMap<String, String>
        get() = options.envs
            .ifEmpty { null }
            ?.split(";")
            ?.map { it.split("=") }
            ?.associate { it[0] to it[1] }
            ?.toMutableMap() ?: mutableMapOf()
        set(value) {
            options.envs = value
                .map { "${it.key}=${it.value}" }
                .joinToString(";")
        }

    var buildArguments: String
        get() = options.buildArguments
        set(value) {
            options.buildArguments = value
        }

    var programArguments: String
        get() = options.programArguments
        set(value) {
            options.programArguments = value
        }

    var production: Boolean
        get() = options.production
        set(value) {
            options.production = value
        }

    var emulateTerminal: Boolean
        get() = options.emulateTerminal
        set(value) {
            options.emulateTerminal = value
        }

    override fun writeExternal(element: Element) {
        super<RunConfigurationBase>.writeExternal(element)

        with(element) {
            writeString("runKind", runKind.name)
            writeString("fileName", fileName)
            writeString("directory", directory)
            writeString("outputDir", outputDir)
            writeBool("runAfterBuild", runAfterBuild)
            writeString("workingDir", workingDir)
            writeString("envs", options.envs)
            writeString("buildArguments", buildArguments)
            writeString("programArguments", programArguments)
            writeBool("production", production)
            writeBool("emulateTerminal", emulateTerminal)
        }
    }

    override fun isGeneratedName(): Boolean {
        return false
    }

    override fun readExternal(element: Element) {
        super<RunConfigurationBase>.readExternal(element)

        with(element) {
            readString("runKind")?.let { runKind = VlangRunConfigurationEditor.RunKind.fromString(it) }
            readString("fileName")?.let { fileName = it }
            readString("directory")?.let { directory = it }
            readString("outputDir")?.let { outputDir = it }
            readBool("runAfterBuild")?.let { runAfterBuild = it }
            readString("workingDir")?.let { workingDir = it }
            readString("envs")?.let { options.envs = it }
            readString("buildArguments")?.let { buildArguments = it }
            readString("programArguments")?.let { programArguments = it }
            readBool("production")?.let { production = it }
            readBool("emulateTerminal")?.let { emulateTerminal = it }
        }
    }

    override fun getConfigurationEditor() = VlangRunConfigurationEditor(project)

    override fun checkConfiguration() {
        // TODO: add check for class or method?
    }

    override fun getState(executor: Executor, executionEnvironment: ExecutionEnvironment): RunProfileState? {
        return VlangRunConfigurationRunState(executionEnvironment, this)
    }

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

    private fun Element.writeBool(name: String, value: Boolean) {
        writeString(name, value.toString())
    }

    private fun Element.readBool(name: String): Boolean? =
        readString(name)?.toBoolean()
}
