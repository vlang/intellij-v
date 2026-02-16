package io.vlang.lang.parser

import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiErrorElement
import io.vlang.lang.VlangParserDefinition
import io.vlang.lang.psi.VlangRecursiveVisitor
import java.io.File
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.extension
import kotlin.io.path.isExecutable
import kotlin.io.path.isSymbolicLink
import kotlin.io.path.walk

class ParserVLibTest : ParserTestBase("", "v", VlangParserDefinition()) {

    fun getVLibDirectory(): Path {
        System.getenv("PATH").split(File.pathSeparator).forEach {
            val path = Path(it)
            val vPath = path.resolve("v").let { p -> if(p.isSymbolicLink()) p.toRealPath() else p }
            val vlibPath = vPath.parent.resolve("vlib")
            if (vPath.exists() && vPath.isExecutable() && vlibPath.exists()) {
                println(vlibPath)
                return vlibPath
            }
        }
        error("V executable and vlib directory not found in PATH")
    }

    override fun getTestDataPath(): String {
        return getVLibDirectory().toString()
    }

    fun `test vlib`() {
        if (System.getenv("VLIB_TEST") != "1") {
            println("Skipping vlib tests because VLIB_TEST is not set to 1")
            return
        }

        var errorsCount = 0
        var errorsInTestsCount = 0
        var failedFilesCount = 0
        var failedTestFilesCount = 0
        for (path in vLibDirectory.walk()) {
            if (path.extension == "v") {
//                println("Parsing file: $path")
                val relativePath = vLibDirectory.relativize(path).toString()
                try {
                    val text = loadFile(relativePath)
                    val psiFile = parseFile(relativePath, text)
                    val errors = mutableListOf<PsiErrorElement>()
                    psiFile.accept(object : VlangRecursiveVisitor() {
                        override fun visitErrorElement(element: PsiErrorElement) {
                            super.visitErrorElement(element)
                            errors.add(element)
                        }
                    })
                    if (errors.isNotEmpty()) {
                        println("=".repeat(40))
                        println("Found ${errors.size} error(s) in file: $path")
                        println()
                        val lines = text.split('\n')
                        errors.forEach { element ->
                            val start = StringUtil.offsetToLineColumn(text, element.textRange.startOffset)
                            val end = StringUtil.offsetToLineColumn(text, element.textRange.endOffset)
                            val lineNumber = start.line + 1
                            val startColumn = start.column
                            val endColumn = if (start.line == end.line) end.column else lines[start.line].length

                            println("Error: ${element.errorDescription}")
                            println("${String.format("%6d", lineNumber)} | ${lines[start.line]}")
                            val underline = " ".repeat(startColumn + 9) + "^".repeat(maxOf(1, endColumn - startColumn))
                            println(underline)
                            println()
                        }
                        failedFilesCount++
                        errorsCount += errors.size
                        if (path.toString().endsWith("_test.v")) {
                            failedTestFilesCount++
                            errorsInTestsCount += errors.size
                        }
                    }
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            }
        }
        if (failedFilesCount > 0) {
            fail("$failedFilesCount files in vlib contain $errorsCount parsing errors\ntests: $failedTestFilesCount files with $errorsInTestsCount errors\n vlib: ${failedFilesCount-failedTestFilesCount} files with ${errorsCount-errorsInTestsCount} errors\nSee the log above for details.")
        }
    }
}
