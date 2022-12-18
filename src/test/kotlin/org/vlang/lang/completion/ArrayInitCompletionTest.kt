package org.vlang.lang.completion

class ArrayInitCompletionTest : CompletionTestBase() {
    fun `test fields`() = checkEquals(
        """
        module main
        
        fn main() {
            arr := []int{/*caret*/}
        }

        """.trimIndent(), 0,
        "cap", "init", "len", "map",
    )
}
