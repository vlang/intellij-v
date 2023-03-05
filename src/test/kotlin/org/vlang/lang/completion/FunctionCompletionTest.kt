package org.vlang.lang.completion

class FunctionCompletionTest : CompletionTestBase() {
    fun `test function name completion`() = doTestCompletion(
        """
        module main
        
        fn foo() {}
        
        fn main() {
            fo<caret>
        }
        """.trimIndent(),
        """
        module main
        
        fn foo() {}
        
        fn main() {
            foo()<caret>
        }
        """.trimIndent()
    )

    fun `test function name before () completion`() = doTestCompletion(
        """
        module main
        
        fn foo() {}
        
        fn main() {
            fo<caret>()
        }
        """.trimIndent(),
        """
        module main
        
        fn foo() {}
        
        fn main() {
            foo()<caret>
        }
        """.trimIndent()
    )

    fun `test method name completion`() = doTestCompletion(
        """
        module main
        
        struct Test {}
        
        fn (t Test) foo() {}
        
        fn main() {
            test := Test{}
            test.fo<caret>
        }
        """.trimIndent(),
        """
        module main
        
        struct Test {}
        
        fn (t Test) foo() {}
        
        fn main() {
            test := Test{}
            test.foo()<caret>
        }
        """.trimIndent()
    )

    fun `test method name before () completion`() = doTestCompletion(
        """
        module main
        
        struct Test {}
        
        fn (t Test) foo() {}
        
        fn main() {
            test := Test{}
            test.fo<caret>()
        }
        """.trimIndent(),
        """
        module main
        
        struct Test {}
        
        fn (t Test) foo() {}
        
        fn main() {
            test := Test{}
            test.foo()<caret>
        }
        """.trimIndent()
    )

    fun `test function with several arguments completion`() = doTestCompletion(
        """
        module main
        
        fn foo(name string) {}
        
        fn main() {
            fo<caret>
        }
        """.trimIndent(),
        """
        module main
        
        fn foo(name string) {}
        
        fn main() {
            foo(<caret>)
        }
        """.trimIndent()
    )

    fun `test function with several arguments completion 2`() = doTestCompletion(
        """
        module main
        
        fn foo(age int, names ...string) {}
        
        fn main() {
            fo<caret>
        }
        """.trimIndent(),
        """
        module main
        
        fn foo(age int, names ...string) {}
        
        fn main() {
            foo(<caret>)
        }
        """.trimIndent()
    )

    fun `test generic function name completion`() = doTestCompletion(
        """
        module main
        
        fn foo[T]() {}
        
        fn main() {
            fo<caret>
        }
        """.trimIndent(),
        """
        module main
        
        fn foo[T]() {}
        
        fn main() {
            foo[<caret>T]()
        }
        """.trimIndent()
    )

    fun `test generic function name before less completion`() = doTestCompletion(
        """
        module main
        
        fn foo<T>() {}
        
        fn main() {
            fo<caret>[int]()
        }
        """.trimIndent(),
        """
        module main
        
        fn foo<T>() {}
        
        fn main() {
            foo[<caret>int]()
        }
        """.trimIndent()
    )

    fun `test generic function name completion 2`() = doTestCompletion(
        """
        module main
        
        fn foo[T, U, N]() {}
        
        fn main() {
            fo<caret>
        }
        """.trimIndent(),
        """
        module main
        
        fn foo[T, U, N]() {}
        
        fn main() {
            foo[<caret>T, U, N]()
        }
        """.trimIndent()
    )

    fun `test function in context type with same type`() = doTestCompletion(
        """
        module main
        
        fn foo() {}
        
        struct Name {
            cb fn ()
        }
        
        fn main() {
            n := Name{cb: fo<caret>}
        }
        """.trimIndent(),
        """
        module main
        
        fn foo() {}
        
        struct Name {
            cb fn ()
        }
        
        fn main() {
            n := Name{cb: foo}
        }
        """.trimIndent()
    )

    fun `test function in context type with same type with alias`() = doTestCompletion(
        """
        module main
        
        type Callback = fn ()
        
        fn foo() {}
        
        struct Name {
            cb Callback
        }
        
        fn main() {
            n := Name{cb: fo<caret>}
        }
        """.trimIndent(),
        """
        module main
        
        type Callback = fn ()
        
        fn foo() {}
        
        struct Name {
            cb Callback
        }
        
        fn main() {
            n := Name{cb: foo}
        }
        """.trimIndent()
    )

    fun `test function in context type with incomplete type`() = doTestCompletion(
        """
        module main
        
        fn foo() {}
        
        struct Name {
            cb fn () string
        }
        
        fn main() {
            n := Name{cb: fo<caret>}
        }
        """.trimIndent(),
        """
        module main
        
        fn foo() {}
        
        struct Name {
            cb fn () string
        }
        
        fn main() {
            n := Name{cb: foo}
        }
        """.trimIndent()
    )

    fun `test function in context type with non function type`() = doTestCompletion(
        """
        module main
        
        fn foo() {}
        
        struct Name {
            cb string
        }
        
        fn main() {
            n := Name{cb: fo<caret>}
        }
        """.trimIndent(),
        """
        module main
        
        fn foo() {}
        
        struct Name {
            cb string
        }
        
        fn main() {
            n := Name{cb: foo()}
        }
        """.trimIndent()
    )
}
