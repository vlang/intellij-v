package org.vlang.lang.resolve

class ResolveArrayInitFieldsTest : ResolveTestBase() {
    fun `test fields`() {
        mainFile("a.v", """
            module main
            
            fn main() {
                arr := []int{/*caret*/len: 0, /*caret*/cap: 0, /*caret*/init: 0}
            }
        """.trimIndent())

        assertQualifiedReferencedTo("FIELD_DEFINITION stubs.ArrayInit.len")
        assertQualifiedReferencedTo("FIELD_DEFINITION stubs.ArrayInit.cap")
        assertQualifiedReferencedTo("FIELD_DEFINITION stubs.ArrayInit.init")
    }

    fun `test deprecated it variable`() {
        mainFile("a.v", """
            module main
            
            fn main() {
                arr := []int{init: /*caret*/it * 2}
                arr2 := []int{init: arr[/*caret*/it + 1]}
            }
        """.trimIndent())

        assertQualifiedReferencedTo("FIELD_DEFINITION stubs.ArrayInit.index")
        assertQualifiedReferencedTo("FIELD_DEFINITION stubs.ArrayInit.index")
    }

    fun `test index variable`() {
        mainFile("a.v", """
            module main
            
            fn main() {
                arr := []int{init: /*caret*/index * 2}
                arr2 := []int{init: arr[/*caret*/index + 1]}
            }
        """.trimIndent())

        assertQualifiedReferencedTo("FIELD_DEFINITION stubs.ArrayInit.index")
        assertQualifiedReferencedTo("FIELD_DEFINITION stubs.ArrayInit.index")
    }
}
