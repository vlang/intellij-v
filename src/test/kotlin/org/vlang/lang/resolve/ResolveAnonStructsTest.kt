package org.vlang.lang.resolve

class ResolveAnonStructsTest : ResolveTestBase() {
    fun `test anon structs`() {
        mainFile("a.v", """
            module main
            
            struct Book {
                author struct {
                    name string
                    age  int
                }
            
                title string
            }
            
            fn main() {
                book := Book{}
                book./*caret*/author./*caret*/age
                book./*caret*/author./*caret*/name
                book./*caret*/title
            }

        """.trimIndent())

        assertQualifiedReferencedTo("FIELD_DEFINITION main.Book.author")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.Book.<anonymous struct>.age")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.Book.author")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.Book.<anonymous struct>.name")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.Book.title")
    }

    fun `test anon structs as param`() {
        mainFile("a.v", """
            module main
            
            
            fn foo(params struct { name string age int }) {
                params./*caret*/name
            }
        """.trimIndent())

        assertQualifiedReferencedTo("FIELD_DEFINITION main.<anonymous struct>.name")
    }
}
