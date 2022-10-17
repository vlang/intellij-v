package org.vlang.lang.types

class TypeTest : TypeTestBase() {
    fun `test primitives`() = doTest("primitives.v")
    fun `test tuples`() = doTest("tuples.v")
    fun `test loops`() = doTest("loops.v")
    fun `test conditional`() = doTest("conditional.v")
    fun `test unary`() = doTest("unary.v")
    fun `test if expression`() = doTest("if_expression.v")
    fun `test func type`() = doTest("func_type.v")
    fun `test type cast`() = doTest("type_cast.v")
    fun `test or block`() = doTest("or_block.v")
    fun `test arrays`() = doTest("arrays.v")
    fun `test constants`() = doTest("constants.v")
    fun `test if guard`() = doTest("if_guard.v")
    fun `test select`() = doTest("select.v")
    fun `test pointer`() = doTest("pointer.v")
    fun `test json decode`() = doTestWithBuiltin("json_decode.v")
    fun `test array methods`() = doTestWithBuiltin("array_methods.v")
}
