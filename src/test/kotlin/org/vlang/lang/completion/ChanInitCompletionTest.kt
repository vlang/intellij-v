package org.vlang.lang.completion

class ChanInitCompletionTest : CompletionTestBase() {
    fun `test fields`() = checkEquals(
        """
        module main
        
        fn main() {
            ch := chan int{/*caret*/}
        }

        """.trimIndent(), 0,
        "cap"
    )

    fun `test second field`() = checkEquals(
        """
        module main
        
        fn main() {
            ch := chan int{cap: 100/*caret*/}
        }

        """.trimIndent(), 0,
    )

    fun `test field value`() = checkIncludes(
        """
        module main
        
        fn main() {
            ch := chan int{cap: /*caret*/}
        }

        """.trimIndent(), 0,
        "unsafe",
    )
}
