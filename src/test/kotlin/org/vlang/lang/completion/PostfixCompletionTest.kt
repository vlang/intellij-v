package org.vlang.lang.completion

class PostfixCompletionTest : CompletionTestBase() {
    fun `test arg`() = doTestPostfix(
        """
        module main
        
        fn main() {
            100.arg/*caret*/
        }
        """.trimIndent(),
        """
        module main

        fn main() {
            (100)
        }
        """.trimIndent()
    )

    fun `test par`() = doTestPostfix(
        """
        module main
        
        fn main() {
            100.par/*caret*/
        }
        """.trimIndent(),
        """
        module main

        fn main() {
            (100)
        }
        """.trimIndent()
    )

    fun `test unsafe`() = doTestPostfix(
        """
        module main
        
        fn main() {
            foo().unsafe/*caret*/
        }
        """.trimIndent(),
        """
        module main

        fn main() {
            unsafe { foo() }
        }
        """.trimIndent()
    )
}
