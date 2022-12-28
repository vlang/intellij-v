package org.vlang.lang.completion

class ArrayInitCompletionTest : CompletionTestBase() {
    fun `test fields`() = checkEquals(
        """
        module main
        
        fn main() {
            arr := []int{/*caret*/}
        }

        """.trimIndent(), 0,
        "cap", "init", "len",
    )

    fun `test second field`() = checkEquals(
        """
        module main
        
        fn main() {
            arr := []int{len: 100, /*caret*/}
        }

        """.trimIndent(), 0,
        "cap", "init", "len",
    )

    fun `test field value`() = checkIncludes(
        """
        module main
        
        fn main() {
            arr := []int{len: /*caret*/}
        }

        """.trimIndent(), 0,
        "unsafe",
    )
}
