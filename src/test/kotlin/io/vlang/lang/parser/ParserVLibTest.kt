package io.vlang.lang.parser

import io.vlang.lang.VlangParserDefinition
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

        val vLibDirectory = getVLibDirectory()
        var failed = 0
        var failed_tests = 0
        for (path in vLibDirectory.walk()) {
            if (path.extension == "v") {
                println("Parsing file: $path")
                val relativePath = vLibDirectory.relativize(path).toString()
                try {
                    parseFile(relativePath, loadFile(relativePath))
                    try {
                        ensureNoErrorElements()
                    } catch (e: AssertionError) {
                        println("Error elements found in file: $relativePath, details: ${e.message}")
                        failed++
                        if (path.toString().endsWith("_test.v")) {
                            failed_tests++
                        }
                    }
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            }
        }
        if (failed > 0) {
            fail("$failed files in vlib contain parsing error\n$failed_tests of tests\n${failed-failed_tests} vlib files\nSee the log above for details.")
        }
    }
}
