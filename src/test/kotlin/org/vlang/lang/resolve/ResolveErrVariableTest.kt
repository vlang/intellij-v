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
}
