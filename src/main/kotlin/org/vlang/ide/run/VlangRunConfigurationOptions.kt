package org.vlang.ide.run

import com.intellij.execution.configurations.RunConfigurationOptions

class VlangRunConfigurationOptions : RunConfigurationOptions() {
    private var _runKind = string(VlangRunConfigurationEditor.RunKind.File.name).provideDelegate(this, "runKindRunConfiguration")
    private var _fileName = string("").provideDelegate(this, "fileNameRunConfiguration")
    private var _directory = string("").provideDelegate(this, "directoryRunConfiguration")
    private var _outputDir = string("").provideDelegate(this, "outputDirRunConfiguration")
    private var _runAfterBuild = property(true).provideDelegate(this, "runAfterBuildRunConfiguration")
    private var _workingDir = string("").provideDelegate(this, "workingDirRunConfiguration")
    private var _envs = string("").provideDelegate(this, "envsRunConfiguration")
    private var _buildArguments = string("").provideDelegate(this, "buildArgumentsRunConfiguration")
    private var _programArguments = string("").provideDelegate(this, "programArgumentsRunConfiguration")
    private var _production = property(false).provideDelegate(this, "productionRunConfiguration")
    private var _emulateTerminal = property(true).provideDelegate(this, "emulateTerminalRunConfiguration")

    var runKind: VlangRunConfigurationEditor.RunKind
        get() = VlangRunConfigurationEditor.RunKind.fromString(_runKind.getValue(this))
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

    var outputDir: String
        get() = _outputDir.getValue(this) ?: ""
        set(value) {
            _outputDir.setValue(this, value)
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
}
