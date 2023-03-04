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

    fun `test embedded structs names`() = checkIncludes(
        """
        module main
        
        struct Embedded {
            name string
        }
        
        struct Name {
            Embedded
            age int
        }
        
        fn main() {
            n := Name{}
            n./*caret*/
        }
        """.trimIndent(), 0,
        "Embedded", "age", "name"
    )

    fun `test several embedded structs names`() = checkIncludes(
        """
        module main
        
        struct Embedded {
            name string
        }
        
        struct Embedded2 {
            city string
        }
        
        struct Name {
            Embedded
            Embedded2
            age int
        }
        
        fn main() {
            n := Name{}
            n./*caret*/
        }
        """.trimIndent(), 0,
        "Embedded", "age", "name", "Embedded2", "city"
    )
}
