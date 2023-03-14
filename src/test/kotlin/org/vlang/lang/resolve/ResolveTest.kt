package org.vlang.lang.resolve

class ResolveTest : ResolveTestBase() {
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

    fun `test resolve field call`() {
        mainFile("a.v", """
            struct Foo {
                cb fn () string
            }
            
            fn main() {
                foo := Foo{}
                foo.c<caret>b()
            }
        """.trimIndent())

        assertReferencedTo("FIELD_DEFINITION cb")
    }

    fun `test resolve variable in their definition`() {
        mainFile("a.v", """
            fn main() {
                foo := /*caret*/foo
                boo := /*caret*/foo
                goo := 100 + /*caret*/goo
            }
        """.trimIndent())

        assertUnresolved()
        assertReferencedTo("VAR_DEFINITION foo")
        assertUnresolved()
    }

    fun `test resolve enum methods`() {
        mainFile("a.v", """
            enum Color { red }
            
            fn (c Color) foo() {}
            
            fn main() {
                c := Color.red
                c.f<caret>oo()
            }
        """.trimIndent())

        assertQualifiedReferencedTo("METHOD_DECLARATION unnamed.Color.foo")
    }

    fun `test offset of fields`() {
        mainFile("a.v", """
            struct User {
                age int
            }
            
            fn main() {
                __offsetof(User, /*caret*/age)
            }

        """.trimIndent())

        assertQualifiedReferencedTo("FIELD_DEFINITION unnamed.User.age")
    }

    fun `test resolve compile time fields field`() {
        mainFile("a.v", """
            module main
            
            struct Foo {
                a int
                b int
                fields []string
            }
            
            fn main() {
                foo := Foo{}
                ${"$"}for field in Foo./*caret*/fields {
                    foo./*caret*/fields
                }
            }
        """.trimIndent())

        assertQualifiedReferencedTo("FIELD_DEFINITION stubs.CompileTimeTypeInfo.fields")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.Foo.fields")
    }

    fun `test resolve _unlikely_ and _likely_`() {
        mainFile("a.v", """
            module main
            
            fn main() {
                if /*caret*/_unlikely_(true) {
                    println("unlikely")
                }
                if /*caret*/_likely_(true) {
                    println("likely")
                }
            }
        """.trimIndent())

        assertQualifiedReferencedTo("FUNCTION_DECLARATION stubs._unlikely_")
        assertQualifiedReferencedTo("FUNCTION_DECLARATION stubs._likely_")
    }

    fun `test resolve struct inside for without last statement`() {
        mainFile("a.v", """
            module main

            struct Foo {}
            
            fn main() {
                for i := 0; i < 100; {
                    /*caret*/Foo{}
                }
            }
        """.trimIndent())

        assertQualifiedReferencedTo("STRUCT_DECLARATION main.Foo")
    }
}
