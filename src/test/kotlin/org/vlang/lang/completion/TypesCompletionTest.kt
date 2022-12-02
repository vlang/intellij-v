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
}
