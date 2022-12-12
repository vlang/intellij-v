package org.vlang.lang.resolve

open class ResolveTrailingStructLiteralTest : ResolveTestBase() {
    fun `test only one param`() {
        mainFile("a.v", """
            module main

            struct Params {
                foo int
                boo int
            }
            
            fn foo(params Params) {}
            
            fn main() {
                foo(/*caret*/foo: 100)
                foo(/*caret*/foo: 100, /*caret*/boo: 200)
                foo(/*caret*/unknown: 200) // unknown field
                foo(foo: 100, /*caret*/unknown: 200, boo: 200) // unknown field
            }
        """.trimIndent())

        assertReferencedTo("FIELD_DEFINITION foo")
        assertReferencedTo("FIELD_DEFINITION foo")
        assertReferencedTo("FIELD_DEFINITION boo")
        assertUnresolved()
        assertUnresolved()
    }

    fun `test with one extra param`() {
        mainFile("a.v", """
            module main

            struct Params {
                foo int
                boo int
            }
            
            fn foo(id int, params Params) {}
            
            fn main() {
                foo(100, /*caret*/foo: 100)
                foo(200, /*caret*/foo: 100, /*caret*/boo: 200)
                foo(200, foo: 100, boo: 200, /*caret*/unknown: 200) // unknown field
                foo(/*caret*/foo: 100) // error
            }
        """.trimIndent())

        assertReferencedTo("FIELD_DEFINITION foo")
        assertReferencedTo("FIELD_DEFINITION foo")
        assertReferencedTo("FIELD_DEFINITION boo")
        assertUnresolved()
        assertUnresolved()
    }

    fun `test with one extra trailing param`() {
        mainFile("a.v", """
            module main

            struct Params {
                foo int
                boo int
            }
            
            fn foo(id int, params Params, age int) {}
            
            fn main() {
                // not allowed
                foo(100, /*caret*/foo: 100, 100)
                foo(200, /*caret*/foo: 100, /*caret*/boo: 200, 100)
                foo(/*caret*/foo: 100)
            }
        """.trimIndent())

        assertUnresolved()
        assertUnresolved()
        assertUnresolved()
        assertUnresolved()
    }

    fun `test trailing param inside other`() {
        mainFile("a.v", """
            module main

            struct Boo {
                name   string
                script string
            }
            
            struct Foo {
                name  string
                age   int
                other &Boo
            }
            
            fn take_foo(foo Foo)
            fn take_boo(foo Boo)
            
            fn main() {
                take_foo(
                    /*caret*/other: take_boo(
                        /*caret*/script: ""
                        /*caret*/name: ""
                    )
                    /*caret*/name: "hello"
                )
            }

        """.trimIndent())

        assertQualifiedReferencedTo("FIELD_DEFINITION main.Foo.other")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.Boo.script")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.Boo.name")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.Foo.name")
    }
}
