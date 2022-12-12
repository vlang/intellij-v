package org.vlang.lang.resolve

open class ResolveStructInitLiteralTest : ResolveTestBase() {
    fun `test one inside other`() {
        mainFile("a.v", """
            module main

            struct Boo {
                script string
                name   string
            }
            
            struct Foo {
                name  string
                age   int
                other &Boo
            }
            
            fn main() {
                foo := Foo{
                    /*caret*/name: 'hello'
                    /*caret*/age: 10
                    /*caret*/other: Boo{
                        /*caret*/script: ''
                        /*caret*/name: ''
                    }
                }
            }
        """.trimIndent())

        assertQualifiedReferencedTo("FIELD_DEFINITION main.Foo.name")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.Foo.age")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.Foo.other")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.Boo.script")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.Boo.name")
    }
}
