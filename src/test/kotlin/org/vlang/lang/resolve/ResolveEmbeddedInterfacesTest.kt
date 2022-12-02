package org.vlang.lang.resolve

open class ResolveEmbeddedInterfacesTest : ResolveTestBase() {
    fun `test embedded interface field`() {
        mainFile("a.v", """
            module main

            interface ForEmbed {
                foo int
            }
            
            interface Test {
                ForEmbed
            }
            
            fn main() {
                t := Test()
                t.<caret>foo
            }
        """.trimIndent())

        assertReferencedTo("FIELD_DEFINITION foo")
    }

    fun `test several embedded interfaces`() {
        mainFile("a.v", """
            module main

            interface ForEmbed {
                foo int
            }
            
            interface ForEmbed2 {
                bar int
            }
            
            interface Test {
                ForEmbed
                ForEmbed2
            }
            
            fn main() {
                t := Test{}
                t.<caret>bar
            }
        """.trimIndent())

        assertReferencedTo("FIELD_DEFINITION bar")
    }

    fun `test embedded interface method`() {
        mainFile("a.v", """
            module main

            interface ForEmbed {}
            
            fn (e ForEmbed) foo() {}
            
            interface Test {
                ForEmbed
            }
            
            fn main() {
                t := Test{}
                t.<caret>foo()
            }
        """.trimIndent())

        assertReferencedTo("METHOD_DECLARATION foo")
    }
}
