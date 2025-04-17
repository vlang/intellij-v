package io.vlang.ide.run

import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.options.ConfigurationQuickFix
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.util.NlsActions
import java.io.File

open class VlangRunConfiguration(project: Project, factory: ConfigurationFactory?, name: String?) :
    RunConfigurationBase<VlangRunConfigurationOptions>(project, factory, name),
    LocatableConfiguration {

    public override fun getOptions() = super.getOptions() as VlangRunConfigurationOptions

    override fun isGeneratedName(): Boolean {
        return false
    }

    override fun suggestedName(): @NlsActions.ActionText String? {
        val opt = expandedOptions()
        val name = if (opt.runKind == VlangRunConfigurationEditor.RunKind.File) {
            File(opt.fileName).nameWithoutExtension
        } else {
            val dir = File(opt.directory).name
            if (dir == ".") {
                File(opt.workingDir).name
            } else {
                dir
            }
        }
        return "V Build $name"
    }

    override fun getConfigurationEditor() = VlangRunConfigurationEditor(project)

    @Suppress("UnstableApiUsage")
    override fun checkConfiguration() {
        // RuntimeConfigurationWarning – if the configuration settings contain a problem which the user should be warned about.
        // RuntimeConfigurationException – if the configuration settings contain a non-fatal error which the user should be warned about but the execution should still be allowed.
        // RuntimeConfigurationError – if the configuration settings contain a fatal error which makes it impossible to execute the run configuration.

        val opt = this.expandedOptions()

        if (opt.workingDir.isBlank()) {
            throw RuntimeConfigurationError("Working directory is empty", object : ConfigurationQuickFix {
                override fun applyFix(dataContext: DataContext) {
                    options.workingDir = project.guessProjectDir()?.path ?: ""
                }
            })
        }

        if (opt.runKind == VlangRunConfigurationEditor.RunKind.File) {
            if (opt.fileName.isBlank()) {
                throw RuntimeConfigurationError("Source file path is empty")
            }
            if (!opt.fileName.endsWith(".v") && !opt.fileName.endsWith(".vv") && !opt.fileName.endsWith(".vsh") ) {
                throw RuntimeConfigurationError("Source file must be a V file (\".v\", \".vv\", \".vsh\")")
            }
            if (!File(opt.fileName).exists() && !File(opt.workingDir, opt.fileName).exists()) {
                throw RuntimeConfigurationError("Source file '${opt.fileName}' does not exist")
            }
        } else {
            if (opt.directory.isBlank()) {
                throw RuntimeConfigurationError("Source directory path is empty")
            }
            if (!File(opt.directory).exists() && !File(opt.workingDir, opt.directory).exists()) {
                throw RuntimeConfigurationError("Source directory '${opt.directory}' does not exist")
            }
        }

        if (opt.buildArguments.contains("-o") || opt.buildArguments.contains("-output")) {
            throw RuntimeConfigurationError("Output file is set in build arguments - please use 'Output file' field instead!", object : ConfigurationQuickFix {
                override fun applyFix(dataContext: DataContext) {
                    val args = opt.buildArguments.split(" ")
                    args.forEachIndexed { index, arg ->
                        if (arg == "-o" || arg == "-output") {
                            options.outputFileName = args[index + 1]
                        }
                    }
                    options.buildArguments = opt.buildArguments.replace("(-o|-output)\\s+\\S+\\s?".toRegex(), "")
                }
            })
        }
    }

    override fun getState(executor: Executor, executionEnvironment: ExecutionEnvironment): RunProfileState? {
        return VlangRunConfigurationRunState(executionEnvironment, this.expandedOptions())
    }

    fun expandedOptions(): VlangRunConfigurationOptions.ExpandedOptions {
        return options.expandOptions(project)
    }
}
