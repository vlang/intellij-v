package org.vlang.lang.types

class TypeTest : TypeTestBase() {
    fun `test primitives`() = doTest("primitives.v")
    fun `test tuples`() = doTest("tuples.v")
    fun `test loops`() = doTest("loops.v")
    fun `test conditional`() = doTest("conditional.v")
    fun `test if expression`() = doTest("if_expression.v")
}
