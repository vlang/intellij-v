package org.vlang.lang.resolve

open class ResolveEmbeddedStructsTest : ResolveTestBase() {
    fun `test embedded struct field`() {
        mainFile("a.v", """
            module main

            struct ForEmbed {
                foo int
            }
            
            struct Test {
                ForEmbed
            }
            
            fn main() {
                t := Test{}
                t./*caret*/foo
            }
        """.trimIndent())

        assertReferencedTo("FIELD_DEFINITION foo")
    }

    fun `test embedded struct by name`() {
        mainFile("a.v", """
            module main

            struct ForEmbed {
                foo int
            }
            
            struct Test {
                ForEmbed
            }
            
            fn main() {
                t := Test{}
                t./*caret*/ForEmbed
            }
        """.trimIndent())

        assertReferencedTo("EMBEDDED_DEFINITION ForEmbed")
    }

    fun `test embedded struct field with embedded struct name`() {
        mainFile("a.v", """
            module main

            struct ForEmbed {
                foo int
            }
            
            struct Test {
                ForEmbed
            }
            
            fn main() {
                t := Test{}
                t.ForEmbed./*caret*/foo
            }
        """.trimIndent())

        assertReferencedTo("FIELD_DEFINITION foo")
    }

    fun `test several embedded structs`() {
        mainFile("a.v", """
            module main

            struct ForEmbed {
                foo int
            }
            
            struct ForEmbed2 {
                bar int
            }
            
            struct Test {
                ForEmbed
                ForEmbed2
            }
            
            fn main() {
                t := Test{}
                t./*caret*/bar
            }
        """.trimIndent())

        assertReferencedTo("FIELD_DEFINITION bar")
    }

    fun `test several embedded structs with same field`() {
        mainFile("a.v", """
            module main

            struct ForEmbed {
                foo int
            }
            
            struct ForEmbed2 {
                foo int
            }
            
            struct Test {
                ForEmbed
                ForEmbed2
            }
            
            fn main() {
                t := Test{}
                t./*caret*/ForEmbed./*caret*/foo
                t./*caret*/ForEmbed2./*caret*/foo
            }
        """.trimIndent())

        assertQualifiedReferencedTo("EMBEDDED_DEFINITION main.ForEmbed")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.ForEmbed.foo")
        assertQualifiedReferencedTo("EMBEDDED_DEFINITION main.ForEmbed2")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.ForEmbed2.foo")
    }

    fun `test embedded struct method`() {
        mainFile("a.v", """
            module main

            struct ForEmbed {}
            
            fn (e ForEmbed) foo() {}
            
            struct Test {
                ForEmbed
            }
            
            fn main() {
                t := Test{}
                t./*caret*/foo()
            }
        """.trimIndent())

        assertReferencedTo("METHOD_DECLARATION foo")
    }

    fun `test embedded struct method via type alias`() {
        mainFile("a.v", """
            module main

            struct ForEmbed {}
            
            fn (e ForEmbed) foo() {}
            
            struct Test {
                ForEmbed
            }
            
            type TestAlias = Test
            
            fn main() {
                t := TestAlias{}
                t./*caret*/foo()
            }
        """.trimIndent())

        assertReferencedTo("METHOD_DECLARATION foo")
    }
}
