package org.vlang.lang.completion

class TypesCompletionTest : CompletionTestBase() {
    fun `test map type`() = doTestCompletion(
        """
        module main
        
        fn main() map/*caret*/ {}
        """.trimIndent(),
        """
        module main
        
        fn main() map[string]int {}
        """.trimIndent()
    )

    fun `test chan type`() = doTestCompletion(
        """
        module main
        
        fn main() chan/*caret*/ {}
        """.trimIndent(),
        """
        module main
        
        fn main() chan int {}
        """.trimIndent()
    )
}
