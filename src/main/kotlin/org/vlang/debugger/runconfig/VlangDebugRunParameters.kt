package org.vlang.debugger.runconfig

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.jetbrains.cidr.execution.Installer
import com.jetbrains.cidr.execution.RunParameters
import com.jetbrains.cidr.execution.TrivialInstaller
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriverConfiguration
import com.jetbrains.cidr.lang.toolchains.CidrToolEnvironment
import org.vlang.debugger.lang.VlangDownloadedLldbToolEnvironment

class VlangDebugRunParameters(
    val project: Project,
    private val cmd: GeneralCommandLine,
    private val isElevated: Boolean
) : RunParameters() {

    override fun getInstaller(): Installer = TrivialInstaller(cmd)
    override fun getArchitectureId(): String? = null

    override fun getDebuggerDriverConfiguration(): DebuggerDriverConfiguration {
        val cppEnvironment = getLLDBToolchain()
        return VlangLLDBDriverConfiguration(project, cppEnvironment, isElevated)
    }

    private fun getLLDBToolchain(): CidrToolEnvironment =
        try {
            getCppToolchain()
        } catch (e: ReflectiveOperationException) {
            VlangDownloadedLldbToolEnvironment(project)
        }

    private fun getCppToolchain(): CidrToolEnvironment {
        val cppToolchainsCls =
            this::class.java.classLoader.loadClass("com.jetbrains.cidr.cpp.toolchains.CPPToolchains")
        val cppToolChains = cppToolchainsCls.getMethod("getInstance").invoke(null)
        val defaultToolchainMethod = cppToolchainsCls.getMethod("getDefaultToolchain")
        val defaultToolchain = defaultToolchainMethod.invoke(cppToolChains)
            ?: throw ExecutionException("Specify default CPP toolchain")

        val cppEnvironmentCls = this::class.java.classLoader.loadClass(
            "com.jetbrains.cidr.cpp.toolchains.CPPEnvironment"
        )
        val cppToolChainsToolchain = this::class.java.classLoader.loadClass(
            "com.jetbrains.cidr.cpp.toolchains.CPPToolchains\$Toolchain"
        )
        val cppEnvironment = cppEnvironmentCls.getConstructor(cppToolChainsToolchain).newInstance(defaultToolchain)

        return cppEnvironment as CidrToolEnvironment
    }
}
