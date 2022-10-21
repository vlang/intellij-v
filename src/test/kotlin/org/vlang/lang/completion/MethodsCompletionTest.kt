package org.vlang.lang.completion

class MethodsCompletionTest : CompletionTestBase() {
    fun `test method`() = doTestCompletion(
        """
        module main
        
        struct Test {}
        
        fn (t Test) foo() {}
        
        fn main() {
            t := Test{}
            t.fo<caret>
        }
        """.trimIndent(),
        """
        module main
        
        struct Test {}
        
        fn (t Test) foo() {}
        
        fn main() {
            t := Test{}
            t.foo()<caret>
        }
        """.trimIndent()
    )

    fun `test method inside method`() = doTestCompletion(
        """
        module main
        
        struct Test {}
        
        pub fn (t Test) foo() {
            t.fo<caret>
        }
        """.trimIndent(),
        """
        module main
        
        struct Test {}
        
        pub fn (t Test) foo() {
            t.foo()<caret>
        }
        """.trimIndent()
    )
}
