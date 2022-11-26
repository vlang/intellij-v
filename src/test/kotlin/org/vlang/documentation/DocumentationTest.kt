package org.vlang.documentation

class DocumentationTest : DocumentationTestBase() {
    fun `test function doc`() = doTest("function", "main.v")
    fun `test alias doc`() = doTest("alias", "main.v")
    fun `test module doc`() = doTest("module", "main.v")
    fun `test struct fields doc`() = doTest("struct_fields", "main.v")
    fun `test interface members doc`() = doTest("interface_members", "main.v")
    fun `test it variable doc`() = doTest("it_variable", "main.v")

    fun `test rendered doc`() = doTest("rendered", "main.v")
}
