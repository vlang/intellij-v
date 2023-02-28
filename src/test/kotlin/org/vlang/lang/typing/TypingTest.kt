package org.vlang.lang.typing

class TypingTest : TypingTestBase() {
    fun `test char type`() = type(
        """
        fn main() {
            <caret>
        }
        """.trimIndent(),
        "`",
        """
        fn main() {
            ``
        }
        """.trimIndent()
    )

    fun `test char delete`() = delete(
        """
        fn main() {
            `<caret>`
        }
        """.trimIndent(),
        """
        fn main() {
            
        }
        """.trimIndent()
    )

    fun `test enter 1`() = type(
        """
        <caret>fn main() {}
        """.trimIndent(),
        "\n",
        """
        
        fn main() {}
        """.trimIndent()
    )

    fun `test enter 2`() = type(
        """
        <caret>
        fn main() {}
        """.trimIndent(),
        "\n",
        """
        
        
        fn main() {}
        """.trimIndent()
    )

    fun `test enter 3`() = type(
        """
        fn main() {}<caret>
        """.trimIndent(),
        "\n",
        """
        fn main() {}
        
        """.trimIndent()
    )

    fun `test delete enter 1`() = delete(
        """
        fn main() {}
        <caret>
        """.trimIndent(),
        """
        fn main() {}
        """.trimIndent()
    )

    fun `test type } inside string interpolation`() = type(
        """
        "${"\${"}<caret>}"
        """.trimIndent(),
        "}",
        """
        "${"\${}"}"
        """.trimIndent()
    )
}
