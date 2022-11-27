package org.vlang.lang.completion

class StructsCompletionTest : CompletionTestBase() {
    fun `test generic struct`() = doTestCompletion(
        """
        module main
        
        struct Array<T> {
            data []T
        }
        
        fn main() {
            Arr<caret>
        }
        """.trimIndent(),
        """
        module main
        
        struct Array<T> {
            data []T
        }
        
        fn main() {
            Array[T]{<caret>}
        }
        """.trimIndent()
    )

    fun `test new name for generic struct instantiation`() = doTestCompletion(
        """
        module main
        
        struct Array<T> {
            data []T
        }
        
        fn main() {
            Ar<caret>[T]{}
        }
        """.trimIndent(),
        """
        module main
        
        struct Array<T> {
            data []T
        }
        
        fn main() {
            Array<caret>[T]{}
        }
        """.trimIndent()
    )
}
