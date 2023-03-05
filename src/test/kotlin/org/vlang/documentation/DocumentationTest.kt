package org.vlang.documentation

class DocumentationTest : DocumentationTestBase() {
    fun `test function doc`() = doTest("function", "main.v")
    fun `test alias doc`() = doTest("alias", "main.v")
    fun `test module doc`() = doTest("module", "main.v")
    fun `test import module doc`() = doTest("imports", "main.v")
    fun `test struct fields doc`() = doTest("struct_fields", "main.v")
    fun `test interface members doc`() = doTest("interface_members", "main.v")
    fun `test it variable doc`() = doTest("it_variable", "main.v")
    fun `test anon structs doc`() = doTest("anon_structs", "main.v")
    fun `test sum type doc`() = doTest("sum", "main.v")
    fun `test attribute doc`() = doTest("attribute", "main.v")
    fun `test enum fields`() = doTest("enum_fields", "main.v")
    fun `test structs`() = doTest("structs", "main.v")
    fun `test type methods`() = doTest("type_methods", "main.v")
    fun `test embedded definition`() = doTest("embedded_definition", "main.v")
    fun `test interfaces`() = doTest("interfaces", "main.v")

    fun `test rendered doc`() = doTest("rendered", "main.v")
}
