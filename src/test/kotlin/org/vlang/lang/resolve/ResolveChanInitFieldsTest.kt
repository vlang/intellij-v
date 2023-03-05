package org.vlang.lang.resolve

class ResolveChanInitFieldsTest : ResolveTestBase() {
    fun `test fields`() {
        mainFile("a.v", """
            module main
            
            fn main() {
                ch := chan int{/*caret*/cap: 0}
            }
        """.trimIndent())

        assertQualifiedReferencedTo("FIELD_DEFINITION stubs.ChanInit.cap")
    }
}
