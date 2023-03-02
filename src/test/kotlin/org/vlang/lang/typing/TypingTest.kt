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

    fun `test comma in result type`() = type(
        """
        fn foo() string<caret> {}
        """.trimIndent(),
        ",",
        """
        fn foo() (string,<caret>) {}
        """.trimIndent()
    )

    fun `test second comma in result type`() = type(
        """
        fn foo() (string,<caret>) {}
        """.trimIndent(),
        ",",
        """
        fn foo() (string,,<caret>) {}
        """.trimIndent()
    )

    fun `test second comma in result type after last type`() = type(
        """
        fn foo() (string, int<caret>) {}
        """.trimIndent(),
        ",",
        """
        fn foo() (string, int,<caret>) {}
        """.trimIndent()
    )

    fun `test comma in result type after )`() = type(
        """
        fn foo() (string, int)<caret> {}
        """.trimIndent(),
        ",",
        """
        fn foo() (string, int),<caret> {}
        """.trimIndent()
    )

    fun `test comma in result type with space after first type`() = type(
        """
        fn foo() string <caret> {}
        """.trimIndent(),
        ",",
        """
        fn foo() string ,<caret> {}
        """.trimIndent()
    )
}
