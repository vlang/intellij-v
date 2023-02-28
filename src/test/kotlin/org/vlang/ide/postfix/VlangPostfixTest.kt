package org.vlang.ide.postfix

class VlangPostfixTest : PostfixTestBase() {
    fun `test for template for maps`() = doTest("for_maps.v")
    fun `test for template for arrays`() = doTest("for_arrays.v")
}
