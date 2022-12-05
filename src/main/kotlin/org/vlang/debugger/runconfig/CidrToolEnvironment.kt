package org.vlang.debugger.runconfig

open class CidrToolEnvironment {
    fun toLocalPath(absolutePath: String?): String? {
        return absolutePath
    }

    fun toEnvPath(localPath: String?): String? {
        return localPath
    }
}
