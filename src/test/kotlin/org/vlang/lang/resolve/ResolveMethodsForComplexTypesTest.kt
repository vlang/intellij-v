package org.vlang.lang.resolve

open class ResolveMethodsForComplexTypesTest : ResolveTestBase() {
    fun `test method of array`() {
        mainFile("a.v", """
            module main

            type Test = int
            
            fn get() []Test {
                return [Test(1), 2]
            }
            
            fn (arr []Test) fun() {}
            
            fn main() {
                arr := get()
                arr.fu<caret>n()
            }
        """.trimIndent())

        assertReferencedTo("METHOD_DECLARATION fun")
    }

    fun `test method of map`() {
        mainFile("a.v", """
            module main

            type Test = int
            
            fn get() map[string]Test {
                return {}
            }
            
            fn (arr map[string]Test) fun() {}
            
            fn main() {
                arr := get()
                arr.fu<caret>n()
            }
        """.trimIndent())

        assertReferencedTo("METHOD_DECLARATION fun")
    }

    fun `test method of pointer`() {
        mainFile("a.v", """
            module main

            type Test = string
            
            fn get() &Test {
                a := ''
                return &a
            }
            
            fn (arr &Test) fun() {}
            
            fn main() {
                arr := get()
                arr.fu<caret>n()
            }
        """.trimIndent())

        assertReferencedTo("METHOD_DECLARATION fun")
    }
}
