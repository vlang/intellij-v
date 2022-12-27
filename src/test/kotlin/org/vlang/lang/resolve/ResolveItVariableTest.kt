package org.vlang.lang.resolve

open class ResolveItVariableTest : ResolveTestBase() {
    fun `test it in function call without param name for function type`() {
        mainFile("a.v", """
            module main

            fn main() {
                [1, 2, 3].filter(/*caret*/it > 100)
                [1, 2, 3].any(/*caret*/it > 100)
                [1, 2, 3].map(/*caret*/it > 100)
            }
        """.trimIndent())

        assertReferencedTo("PARAM_DEFINITION null")
        assertReferencedTo("PARAM_DEFINITION null")
        assertReferencedTo("PARAM_DEFINITION null")
    }

    fun `test it type in array methods`() {
        mainFile("a.v", """
            module main
            
            struct Foo {
                age int
            }
            
            fn main() {
                arr := [Foo{}]
                arr.map(/*caret*/it./*caret*/age)
                arr.filter(/*caret*/it./*caret*/age > 100)
                arr.any(/*caret*/it./*caret*/age > 100)
            }
        """.trimIndent())

        assertReferencedTo("PARAM_DEFINITION null")
        assertReferencedTo("FIELD_DEFINITION age")
        assertReferencedTo("PARAM_DEFINITION null")
        assertReferencedTo("FIELD_DEFINITION age")
        assertReferencedTo("PARAM_DEFINITION null")
        assertReferencedTo("FIELD_DEFINITION age")
    }

    fun `test it inside other call`() {
        mainFile("a.v", """
            module main
            
            struct Foo {
                age int
            }
            
            pub fn sum[T](array []T) !T {}
            pub fn split(input string) []string {}
            
            fn process_input(input string) []int {
                mut result := split(input)
                    .map(sum(/*caret*/split(/*caret*/it)./*caret*/map(/*caret*/it)) or { 0 })
                result.sort(a > b)
                println(result./*caret*/all(/*caret*/it == 1))
                return result
            }
        """.trimIndent())

        assertReferencedTo("FUNCTION_DECLARATION split")
        assertReferencedTo("PARAM_DEFINITION null")
        assertReferencedTo("METHOD_DECLARATION map")
        assertReferencedTo("PARAM_DEFINITION null")
        assertReferencedTo("METHOD_DECLARATION all")
        assertReferencedTo("PARAM_DEFINITION null")
    }

    fun `test for propagation call`() {
        mainFile("a.v", """
            module main
            
            fn read_lines(s string) []string {}

            fn main() {
                read_lines('ropes-part1.input')!.map(/*caret*/it)
            }
        """.trimIndent())

        assertReferencedTo("PARAM_DEFINITION null")
    }
}
