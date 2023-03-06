package org.vlang.lang.resolve

open class ResolveErrVariableTest : ResolveTestBase() {
    fun `test err in or and else guard expressions`() {
        mainFile("a.v", """
            module main
            
            fn get() ?int {
                return error('')
            }
            
            fn main() {
                get() or { 
                    println(/*caret*/err)
                }
                
                if _ := get() {
                    // ...
                } else {
                    println(/*caret*/err)
                    
                    if /*caret*/err == "" {
                        // ...
                    }
                }
            }
        """.trimIndent())

        assertReferencedTo("CONST_DEFINITION err")
        assertReferencedTo("CONST_DEFINITION err")
        assertReferencedTo("CONST_DEFINITION err")
    }

    fun `test err inside if expression inside if guard`() {
        mainFile("a.v", """
            module main
            
            fn foo() !int {}
            
            fn main() {
                if a := foo() {
            
                } else {
                    if true {
                        println(/*caret*/err)
                    }
                }
            }
        """.trimIndent())

        assertReferencedTo("CONST_DEFINITION err")
    }

    fun `test err inside call expr with or block`() {
        mainFile("a.v", """
            module main
            
            fn foo() !int {}
            fn foo2(str string) !int
            
            fn main() {
                foo() or {
                    foo1(/*caret*/err) or {
                        println(/*caret*/err)
                    }
                }
            }
        """.trimIndent())

        assertReferencedTo("CONST_DEFINITION err")
        assertReferencedTo("CONST_DEFINITION err")
    }
}
