package org.vlang.lang.completion

class StructFieldsModifiersCompletionTest : CompletionTestBase() {
    fun `test simple mut`() = checkEquals(
        """
        module main
        
        struct Book {
            mut/*caret*/
        }
        """.trimIndent(), 0,
        "mut:", "pub mut:",
    )

    fun `test simple pub`() = checkEquals(
        """
        module main
        
        struct Book {
            pub/*caret*/
        }
        """.trimIndent(), 0,
        "pub mut:", "pub:",
    )

    fun `test mut in struct has pub`() = checkEquals(
        """
        module main
        
        struct Book {
        pub:
            a int
            mut/*caret*/
        }
        """.trimIndent(), 0,
        "mut:", "pub mut:",
    )

    fun `test mut in struct has all modifiers`() = checkEquals(
        """
        module main
        
        struct Book {
        pub:
            a int
        mut:
            b int
        pub mut:
            c int
            mut/*caret*/
        }
        """.trimIndent(), 0,
    )
}
