package org.vlang.lang.resolve

open class ResolveShellScriptTest : ResolveTestBase() {
    fun `test functions from os without module name`() {
        mainFile("a.vsh", """
            /*caret*/mkdir("")
        """.trimIndent())

        assertReferencedTo("FUNCTION_DECLARATION mkdir")
    }
}
