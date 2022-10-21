package org.vlang.projectWizard

import com.intellij.openapi.util.SystemInfo
import java.nio.file.Path
import kotlin.io.path.isExecutable

object ToolchainUtil {
    fun Path.hasExecutable(toolName: String): Boolean = pathToExecutable(toolName).isExecutable()

    fun Path.pathToExecutable(toolName: String): Path {
        val exeName = if (SystemInfo.isWindows) "$toolName.exe" else toolName
        return resolve(exeName).toAbsolutePath()
    }
}
