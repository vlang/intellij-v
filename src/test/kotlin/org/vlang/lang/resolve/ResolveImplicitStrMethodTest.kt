package org.vlang.lang.resolve

class ResolveImplicitStrMethodTest : ResolveTestBase() {
    fun `test for struct`() {
        mainFile("a.v", """
            module main
            
            struct Book {}
            
            fn main() {
                book := Book{}
                book./*caret*/str()
            }
        """.trimIndent())

        assertQualifiedReferencedTo("METHOD_DECLARATION stubs.Any.str")
    }

    fun `test for enum`() {
        mainFile("a.v", """
            module main
            
            enum Color {
                red
            }
            
            fn main() {
                color := Color.red
                color./*caret*/str()
            }
        """.trimIndent())

        assertQualifiedReferencedTo("METHOD_DECLARATION stubs.Any.str")
    }

    fun `test for sum type`() {
        mainFile("a.v", """
            module main
            
            type IntOrString = int | string
            
            fn main() {
                val := IntOrString(0)
                val./*caret*/str()
            }
        """.trimIndent())

        assertQualifiedReferencedTo("METHOD_DECLARATION stubs.Any.str")
    }

    fun `test for type alias`() {
        mainFile("a.v", """
            module main
            
            type Int = int
            
            fn main() {
                val := Int(0)
                val./*caret*/str()
            }
        """.trimIndent())

        assertQualifiedReferencedTo("METHOD_DECLARATION stubs.Any.str")
    }

    fun `test for map type`() {
        mainFile("a.v", """
            module main
            
            fn main() {
                val := map[string]int{}
                val./*caret*/str()
            }
        """.trimIndent())

        assertQualifiedReferencedTo("METHOD_DECLARATION stubs.Any.str")
    }

    fun `test for array type`() {
        mainFile("a.v", """
            module main
            
            fn main() {
                val := []int{}
                val./*caret*/str()
            }
        """.trimIndent())

        assertQualifiedReferencedTo("METHOD_DECLARATION stubs.Any.str")
    }

    fun `test for struct type with str`() {
        mainFile("a.v", """
            module main
            
            struct Book {}
            
            fn (b &Book) str()  {
                return "Book"            
            }
            
            fn main() {
                book := Book{}
                book./*caret*/str()
            }
        """.trimIndent())

        assertQualifiedReferencedTo("METHOD_DECLARATION main.Book.str")
    }
}
