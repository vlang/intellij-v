package org.vlang.lang.completion

class KeywordsCompletionTest : CompletionTestBase() {
    fun `test struct`() = doTestCompletion(
        """
        module main
        
        struct<caret>
        
        fn main() {}
        """.trimIndent(),
        """
        module main
        
        struct Name {
        	<caret>
        }
        
        fn main() {}
        """.trimIndent()
    )

    fun `test type`() = doTestCompletion(
        """
        module main
        
        type<caret>
        
        fn main() {}
        """.trimIndent(),
        """
        module main
        
        type Name = int
        
        fn main() {}
        """.trimIndent()
    )

    fun `test dump`() = doTestCompletion(
        """
        module main
        
        fn main() {
            dump<caret>
        }
        """.trimIndent(),
        """
        module main
        
        fn main() {
            dump(<caret>)
        }
        """.trimIndent()
    )

    fun `test isreftype`() = doTestCompletion(
        """
        module main
        
        fn main() {
            isreftype<caret>
        }
        """.trimIndent(),
        """
        module main
        
        fn main() {
            isreftype(<caret>)
        }
        """.trimIndent()
    )
}
