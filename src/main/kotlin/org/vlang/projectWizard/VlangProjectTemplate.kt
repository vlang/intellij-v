package org.vlang.projectWizard

import com.intellij.openapi.module.Module
import com.intellij.openapi.vfs.VirtualFile
import java.nio.charset.StandardCharsets

class VlangProjectTemplate {
    fun generateProject(
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

        val mainFile = baseDir.createChildData(this, "main.v")
        mainFile.setBinaryContent(
            """
                module main

                fn main() {
                	println('Hello, World!')
                }
                
             """.trimIndent().toByteArray(StandardCharsets.UTF_8)
        )

        val editorConfig = baseDir.createChildData(this, ".editorconfig")
        editorConfig.setBinaryContent(
            """
                [*]
                charset = utf-8
                end_of_line = lf
                insert_final_newline = true
                trim_trailing_whitespace = true
                
                [*.v]
                indent_style = tab
                indent_size = 4
             """.trimIndent().toByteArray(StandardCharsets.UTF_8)
        )

        val gitAttributes = baseDir.createChildData(this, ".gitattributes")
        gitAttributes.setBinaryContent(
            """
                * text=auto eol=lf
                *.bat eol=crlf
                
                **/*.v linguist-language=V
                **/*.vv linguist-language=V
                **/*.vsh linguist-language=V
                **/v.mod linguist-language=V
             """.trimIndent().toByteArray(StandardCharsets.UTF_8)
        )

        val gitIgnore = baseDir.createChildData(this, ".gitignore")
        gitIgnore.setBinaryContent(
            """
                # Binaries for programs and plugins
                main
                untitled19
                *.exe
                *.exe~
                *.so
                *.dylib
                *.dll
                
                # Ignore binary output folders
                bin/
                
                # Ignore common editor/system specific metadata
                .DS_Store
                .idea/
                .vscode/
                *.iml
             """.trimIndent().toByteArray(StandardCharsets.UTF_8)
        )

        return listOf(modFile, mainFile)
    }
}
