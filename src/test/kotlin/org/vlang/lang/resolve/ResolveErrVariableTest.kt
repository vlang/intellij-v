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
                    println(er<caret>r)
                }
                
                if _ := get() {
                    // ...
                } else {
                    println(er<caret>r)
                    
                    if er<caret>r == "" {
                        // ...
                    }
                }
            }
        """.trimIndent())

        setupBuiltin()
        assertReferencedTo("INTERFACE_DECLARATION IError")
        assertReferencedTo("INTERFACE_DECLARATION IError")
        assertReferencedTo("INTERFACE_DECLARATION IError")
    }
}
