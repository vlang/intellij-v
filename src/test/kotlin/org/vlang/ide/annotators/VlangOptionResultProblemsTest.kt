package org.vlang.ide.annotators

class VlangOptionResultProblemsTest : AnnotatorTestBase() {
    fun `test err always none`() = doTest("err_is_always_none.v")
}
