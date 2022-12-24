package org.vlang.configurations

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessAdapter
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.util.SystemInfo
import org.vlang.utils.toPath
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.io.path.exists

object VlangConfigurationUtil {
    private val LOG = logger<VlangConfigurationUtil>()

    const val TOOLCHAIN_NOT_SETUP = "V executable not found, toolchain not setup correctly?"
    const val UNDEFINED_VERSION = "N/A"
    const val STANDARD_LIB_PATH = "vlib"
    val STANDARD_V_COMPILER = if (SystemInfo.isWindows) "v.exe" else "v"

    fun getStdlibLocation(path: String): String? {
        if (path.isBlank()) {
            return null
        }
        return Path.of(path, STANDARD_LIB_PATH).toString()
    }

    fun guessToolchainVersion(path: String): String {
        if (path.isBlank()) {
            return UNDEFINED_VERSION
        }

        val exePath = "$path/$STANDARD_V_COMPILER"
        if (!exePath.toPath().exists()) {
            return UNDEFINED_VERSION
        }

        val cmd = GeneralCommandLine()
            .withExePath(exePath)
            .withParameters("-version")
            .withCharset(StandardCharsets.UTF_8)

        val processOutput = StringBuilder()
        try {
            val handler = OSProcessHandler(cmd)
            handler.addProcessListener(object : CapturingProcessAdapter() {
                override fun processTerminated(event: ProcessEvent) {
                    if (event.exitCode != 0) {
                        LOG.warn("Couldn't get V toolchain version: " + output.stderr)
                    } else {
                        processOutput.append(output.stdout)
                    }
                }
            })
            handler.startNotify()
            val future = ApplicationManager.getApplication().executeOnPooledThread {
                handler.waitFor()
            }
            future.get(300, TimeUnit.MILLISECONDS)
        } catch (e: ExecutionException) {
            LOG.warn("Can't execute command for getting V toolchain version", e)
        } catch (e: TimeoutException) {
            LOG.warn("Can't execute command for getting V toolchain version", e)
        }
        val result = processOutput.toString()

        return result.trim().ifEmpty { UNDEFINED_VERSION }
    }
}
