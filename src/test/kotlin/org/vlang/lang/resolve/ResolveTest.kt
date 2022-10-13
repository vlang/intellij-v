package org.vlang.lang.resolve

open class ResolveTest : ResolveTestBase() {
    fun `test struct with same field and method`() {
        file("struct.v", """
            struct Test {
            	name string
            }
            
            fn (t Test) name() {}
        """.trimIndent())
        mainFile("a.v", """
            fn main() {
                test := Test{}
	            test.n<caret>ame()
	            test.n<caret>ame
            }
        """.trimIndent())

        assertReferencedTo("METHOD_DECLARATION name")
        assertReferencedTo("FIELD_DEFINITION name")
    }

    fun `test union with same field and method`() {
        file("union.v", """
            union Test {
            	name string
            }
            
            fn (t Test) name() {}
        """.trimIndent())
        mainFile("a.v", """
            fn main() {
                test := Test{}
	            test.n<caret>ame()
	            test.n<caret>ame
            }
        """.trimIndent())

        assertReferencedTo("METHOD_DECLARATION name")
        assertReferencedTo("FIELD_DEFINITION name")
    }

    fun `test interface with same field and method`() {
        file("interface.v", """
            interface ITest {
            	name string
                name()
            }
        """.trimIndent())
        mainFile("a.v", """
            fn foo(test ITest) {
	            test.n<caret>ame()
	            test.n<caret>ame
            }
        """.trimIndent())

        assertReferencedTo("INTERFACE_METHOD_DEFINITION name")
        assertReferencedTo("FIELD_DEFINITION name")
    }
}
