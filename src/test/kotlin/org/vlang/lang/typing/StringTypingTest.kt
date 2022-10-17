package org.vlang.lang.typing

class StringTypingTest : TypingTestBase() {
    fun `test single quote string type`() = type(
        """
        fn main() { <caret> }
        """.trimIndent(),
        "'",
        """
        fn main() { '' }
        """.trimIndent()
    )

    fun `test double quote string type`() = type(
        """
        fn main() { <caret> }
        """.trimIndent(),
        "\"",
        """
        fn main() { "" }
        """.trimIndent()
    )

    fun `test delete open double quote string`() = delete(
        """
        fn main() { "<caret>" }
        """.trimIndent(),
        """
        fn main() {  }
        """.trimIndent()
    )

    fun `test delete close double quote string`() = delete(
        """
        fn main() { ""<caret> }
        """.trimIndent(),
        """
        fn main() { " }
        """.trimIndent()
    )

    fun `test auto close long template string`() = type(
        """
        fn main() { "<caret>" }
        """.trimIndent(),
        "\${",
        """
            fn main() { "$${"{"}}" }
        """.trimIndent()
    )

    fun `test delete long template string`() = delete(
        """
        fn main() { "$${"{"}<caret>}" }
        """.trimIndent(),
        """
            fn main() { "$" }
        """.trimIndent()
    )

    fun `test incomplete delete long template string`() = delete(
        """
        fn main() { "$${"{"}<caret>" }
        """.trimIndent(),
        """
            fn main() { "$" }
        """.trimIndent()
    )
}
