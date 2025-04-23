package io.vlang.ide.run

import com.intellij.execution.configurations.LocatableRunConfigurationOptions
import com.intellij.openapi.components.PathMacroManager
import com.intellij.openapi.project.Project
import com.intellij.util.EnvironmentUtil
import io.vlang.ide.run.VlangRunConfigurationEditor.RunKind

class VlangRunConfigurationOptions : LocatableRunConfigurationOptions() {

    private var _runKind = string(RunKind.Directory.name).provideDelegate(this, "runKind")
    private var _fileName = string("").provideDelegate(this, "fileName")
    private var _directory = string(".").provideDelegate(this, "directory")
    private var _outputFileName = string("").provideDelegate(this, "outputFileName")
    private var _runAfterBuild = property(true).provideDelegate(this, "runAfterBuild")
    private var _workingDir = string("\$PROJECT_DIR$").provideDelegate(this, "workingDir")
    private var _envs = string("").provideDelegate(this, "envs")
    private var _isPassParentEnvs = property(true).provideDelegate(this, "isPassParentEnvs")
    private var _buildArguments = string("").provideDelegate(this, "buildArguments")
    private var _programArguments = string("").provideDelegate(this, "programArguments")
    private var _production = property(false).provideDelegate(this, "production")
    private var _emulateTerminal = property(true).provideDelegate(this, "emulateTerminal")

    var runKind: RunKind
        get() = RunKind.fromString(_runKind.getValue(this))
        set(value) {
            _runKind.setValue(this, value.name)
        }

    var fileName: String
        get() = _fileName.getValue(this) ?: ""
        set(value) {
            _fileName.setValue(this, value)
        }

    var directory: String
        get() = _directory.getValue(this) ?: ""
        set(value) {
            _directory.setValue(this, value)
        }

    var outputFileName: String
        get() = _outputFileName.getValue(this) ?: ""
        set(value) {
            _outputFileName.setValue(this, value)
        }

    var runAfterBuild: Boolean
        get() = _runAfterBuild.getValue(this)
        set(value) {
            _runAfterBuild.setValue(this, value)
        }

    var workingDir: String
        get() = _workingDir.getValue(this) ?: ""
        set(value) {
            _workingDir.setValue(this, value)
        }

    var envs: String
        get() = _envs.getValue(this) ?: ""
        set(value) {
            _envs.setValue(this, value)
        }

    var buildArguments: String
        get() = _buildArguments.getValue(this) ?: ""
        set(value) {
            _buildArguments.setValue(this, value)
        }

    var programArguments: String
        get() = _programArguments.getValue(this) ?: ""
        set(value) {
            _programArguments.setValue(this, value)
        }

    var production: Boolean
        get() = _production.getValue(this)
        set(value) {
            _production.setValue(this, value)
        }

    var emulateTerminal: Boolean
        get() = _emulateTerminal.getValue(this)
        set(value) {
            _emulateTerminal.setValue(this, value)
        }

    var isPassParentEnvs: Boolean
        get() = _isPassParentEnvs.getValue(this)
        set(value) {
            _isPassParentEnvs.setValue(this, value)
        }

    fun expandOptions(project: Project): ExpandedOptions {
        return ExpandedOptions(this, project)
    }

    class ExpandedOptions(val options: VlangRunConfigurationOptions, project: Project) {

        val macroManager = PathMacroManager.getInstance(project)

        val runKind: RunKind
            get() = options.runKind

        val fileName: String
            get() {
                return macroManager.expandPath(options.fileName)
            }

        val directory: String
            get() {
                return macroManager.expandPath(options.directory)
            }

        val outputFileName: String
            get() {
                return macroManager.expandPath(options.outputFileName)
            }

        val runAfterBuild: Boolean
            get() = options.runAfterBuild

        val workingDir: String
            get() {
                return macroManager.expandPath(options.workingDir)
            }

        val envs: String
            get() {
                return macroManager.expandPath(options.envs)
            }

        val envsMap: Map<String, String>
            get() = EnvironmentUtil.parseEnv(envs.split("\n", ";").toTypedArray())

        val buildArguments: String
            get() {
                return macroManager.expandPath(options.buildArguments)
            }

        val programArguments: String
            get() {
                return macroManager.expandPath(options.programArguments)
            }

        val production: Boolean
            get() = options.production

        val emulateTerminal: Boolean
            get() = options.emulateTerminal

        val isPassParentEnvs: Boolean
            get() = options.isPassParentEnvs
    }
}
