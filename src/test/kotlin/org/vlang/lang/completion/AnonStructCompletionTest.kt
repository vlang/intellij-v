package org.vlang.lang.completion

class AnonStructCompletionTest : CompletionTestBase() {
    fun `test simple anon struct`() = checkEquals(
        """
        module main
        
        struct Book {
            author struct {
                name string
                age  int
            }
        
            title string
        }
        
        fn main() {
            book := Book{}
            book.author./*caret*/
        }

        """.trimIndent(), 0,
        "name", "age",
    )
}
