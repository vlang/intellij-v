package org.vlang.projectWizard

import com.intellij.openapi.module.Module
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import java.nio.charset.StandardCharsets

class VlangProjectTemplate {
    fun generateProject(
        sdkRoot: String,
        module: Module,
        baseDir: VirtualFile
    ): Collection<VirtualFile> {
        val modFile = baseDir.createChildData(this, "v.mod")
        modFile.setBinaryContent(
            """
                Module {
                	name: '${module.name}'
                	description: ''
                	version: '0.0.1'
                	license: 'MIT'
                	dependencies: []
                }
            """.trimIndent().toByteArray(StandardCharsets.UTF_8)
        )

        val binDir = VfsUtil.createDirectoryIfMissing(baseDir, "src")
        val mainFile = binDir.createChildData(this, "main.v")
        mainFile.setBinaryContent(
            """
                module main

                fn main() {
                	println('Hello, World!')
                }
                
             """.trimIndent().toByteArray(StandardCharsets.UTF_8)
        )

        return listOf(modFile, mainFile)
    }
}
