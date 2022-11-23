package org.vlang.documentation

class DocumentationTest : DocumentationTestBase() {
    fun `test function doc`() = doTest("function", "main.v")
    fun `test alias doc`() = doTest("alias", "main.v")
    fun `test module doc`() = doTest("module", "main.v")
}
