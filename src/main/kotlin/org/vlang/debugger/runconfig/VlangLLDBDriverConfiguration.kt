package org.vlang.debugger.runconfig

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.lang.Language
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.util.Expirable
import com.intellij.openapi.util.UserDataHolderEx
import com.intellij.openapi.vfs.VfsUtil
import com.jetbrains.cidr.ArchitectureType
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver
import com.jetbrains.cidr.execution.debugger.backend.EvaluationContext
import com.jetbrains.cidr.execution.debugger.backend.LLFrame
import com.jetbrains.cidr.execution.debugger.backend.LLThread
import com.jetbrains.cidr.execution.debugger.backend.lldb.LLDBDriver
import com.jetbrains.cidr.execution.debugger.backend.lldb.LLDBDriverConfiguration
import org.vlang.debugger.VlangLldbDriver
import org.vlang.debugger.lang.VlangDownloadedLldbToolEnvironment
import org.vlang.debugger.lang.VlangLldbEvaluationContext
import java.io.File

open class VlangLLDBDriverConfiguration(
    private val project: Project,
    private val cppEnvironment: CidrToolEnvironment,
    private val isElevated: Boolean,
) : LLDBDriverConfiguration() {

    private val lldbWorkingDirectory = project.guessProjectDir()?.let { VfsUtil.virtualToIoFile(it) }?.absolutePath

    override fun isElevated(): Boolean = isElevated

    override fun getConsoleLanguage(): Language {
        return super.getConsoleLanguage()
    }

    override fun isStaticVarsLoadingEnabled(): Boolean {
        return super.isStaticVarsLoadingEnabled()
    }

    private fun configureDriver(driver: LLDBDriver) {
        try {
            if (cppEnvironment::class.java.getMethod("isMSVC")
                    .invoke(cppEnvironment) as Boolean
            ) {
                configureMSVCStepping(driver)
            }
        } catch (ignore: ReflectiveOperationException) {
        }
    }

    private fun configureMSVCStepping(driver: LLDBDriver) {
        driver.setAutorunScriptName("jb_lldb_init")
        // configure custom stepping
        driver.setStepIntoClassName("jb_lldb_stepping.StepIn")
        driver.setStepOverClassName("jb_lldb_stepping.StepOver")
        driver.setStepOutClassName(null)
    }

    override fun createDriverCommandLine(
        driver: DebuggerDriver,
        architectureType: ArchitectureType,
    ): GeneralCommandLine =
        super.createDriverCommandLine(driver, architectureType).also {
//            cppEnvironment.prepare(it, CidrToolEnvironment.PrepareFor.RUN)
            it.setWorkDirectory(lldbWorkingDirectory)
        }

    override fun createEvaluationContext(
        driver: DebuggerDriver,
        expirable: Expirable?,
        thread: LLThread,
        frame: LLFrame,
        cacheHolder: UserDataHolderEx,
    ): EvaluationContext {
        return VlangLldbEvaluationContext(project, driver, expirable, thread, frame, cacheHolder)
    }

    override fun createDriver(handler: DebuggerDriver.Handler, architectureType: ArchitectureType): LLDBDriver {
        return VlangLldbDriver(handler, this, architectureType)
    }

    override fun useSTLRenderers(): Boolean {
        return false
    }

    override fun getLLDBFrameworkFile(architectureType: ArchitectureType): File =
        if (cppEnvironment is VlangDownloadedLldbToolEnvironment) {
            cppEnvironment.lldbFrameworkFile
        } else {
            super.getLLDBFrameworkFile(architectureType)
        }

    override fun getLLDBFrontendFile(architectureType: ArchitectureType): File =
        if (cppEnvironment is VlangDownloadedLldbToolEnvironment) {
            cppEnvironment.lldbFrontendFile
        } else {
            super.getLLDBFrontendFile(architectureType)
        }

    override fun convertToLocalPath(absolutePath: String?): String? = cppEnvironment.toLocalPath(absolutePath)
    override fun convertToEnvPath(localPath: String?): String? = cppEnvironment.toEnvPath(localPath)
//    override fun getHostMachine(): HostMachine = cppEnvironment.hostMachine
}