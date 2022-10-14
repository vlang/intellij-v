package org.vlang.lang.completion

class ClosureCompletionTest : CompletionTestBase() {
    fun `test simple closure`() = doTestCompletion("""
        module completion

        fn foo(cb fn ()) ?int {
        	return 0
        }

        fn main() {
        	foo(fn<caret>)
        }
    """.trimIndent(), """
        module completion

        fn foo(cb fn ()) ?int {
        	return 0
        }

        fn main() {
        	foo(fn () {
        		
        	})
        }
    """.trimIndent())

    fun `test closure with params`() = doTestCompletion("""
        module completion

        fn foo(cb fn (age int)) ?int {
        	return 0
        }

        fn main() {
        	foo(fn<caret>)
        }
    """.trimIndent(), """
        module completion

        fn foo(cb fn (age int)) ?int {
        	return 0
        }

        fn main() {
        	foo(fn (age int) {
        		
        	})
        }
    """.trimIndent())

    fun `test closure with vararg params`() = doTestCompletion("""
        module completion

        fn foo(cb fn (ages ...int)) ?int {
        	return 0
        }

        fn main() {
        	foo(fn<caret>)
        }
    """.trimIndent(), """
        module completion

        fn foo(cb fn (ages ...int)) ?int {
        	return 0
        }

        fn main() {
        	foo(fn (ages ...int) {
        		
        	})
        }
    """.trimIndent())

    fun `test closure with modifiers`() = doTestCompletion("""
        module completion

        fn foo(cb fn (mut shared age int, name string)) ?int {
        	return 0
        }

        fn main() {
        	foo(fn<caret>)
        }
    """.trimIndent(), """
        module completion

        fn foo(cb fn (mut shared age int, name string)) ?int {
        	return 0
        }

        fn main() {
        	foo(fn (mut shared age int, name string) {
        		
        	})
        }
    """.trimIndent())
}
