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

    fun `test closure without param name`() = doTestCompletion("""
        module completion

        fn foo(cb fn (int)) ?int {
        	return 0
        }

        fn main() {
        	foo(fn<caret>)
        }
    """.trimIndent(), """
        module completion

        fn foo(cb fn (int)) ?int {
        	return 0
        }

        fn main() {
        	foo(fn (it int) {
        		
        	})
        }
    """.trimIndent())

    fun `test closure without params name`() = doTestCompletion("""
        module completion

        fn foo(cb fn (int, string)) ?int {
        	return 0
        }

        fn main() {
        	foo(fn<caret>)
        }
    """.trimIndent(), """
        module completion

        fn foo(cb fn (int, string)) ?int {
        	return 0
        }

        fn main() {
        	foo(fn (it int, it1 string) {
        		
        	})
        }
    """.trimIndent())

    fun `test closure for struct key`() = doTestCompletion("""
        module main

        struct WithCallback {
        	cb fn (foo int) ?bool
        }
        
        fn main() {
        	WithCallback{
        		cb: fn<caret>
        	}
        }
    """.trimIndent(), """
        module main

        struct WithCallback {
        	cb fn (foo int) ?bool
        }
        
        fn main() {
        	WithCallback{
        		cb: fn (foo int) ?bool {
        			
        		}
        	}
        }
    """.trimIndent())

    fun `test closure as default field value`() = doTestCompletion("""
        module main

        struct WithCallback {
        	cb fn (foo int) ?bool = fn<caret>
        }
    """.trimIndent(), """
        module main

        struct WithCallback {
        	cb fn (foo int) ?bool = fn (foo int) ?bool {
        		
        	}
        }
    """.trimIndent())

    fun `test closure after assign`() = doTestCompletion("""
        module main

        fn main() {
        	a := fn () {}
            
        	a = fn<caret>
        }
    """.trimIndent(), """
        module main
        
        fn main() {
        	a := fn () {}
            
        	a = fn () {
        		
        	}
        }
    """.trimIndent())
}
