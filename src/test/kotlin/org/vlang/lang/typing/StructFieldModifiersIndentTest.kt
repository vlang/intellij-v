package org.vlang.lang.typing

class StructFieldModifiersIndentTest : TypingTestBase() {
    fun `test simple mut`() = type(
        """
        struct Foo {
        	mut<caret>
        }
        """.trimIndent(),
        ":",
        """
        struct Foo {
        mut:<caret>
        }
        """.trimIndent()
    )

    fun `test simple pub mut`() = type(
        """
        struct Foo {
        	pub mut<caret>
        }
        """.trimIndent(),
        ":",
        """
        struct Foo {
        pub mut:<caret>
        }
        """.trimIndent()
    )

    fun `test mut before fields`() = type(
        """
        struct Foo {
        	mut<caret>
        	a int
        }
        """.trimIndent(),
        ":",
        """
        struct Foo {
        mut:<caret>
        	a int
        }
        """.trimIndent()
    )

    fun `test pub before fields`() = type(
        """
        struct Foo {
        	pub<caret>
        	a int
        }
        """.trimIndent(),
        ":",
        """
        struct Foo {
        pub:<caret>
        	a int
        }
        """.trimIndent()
    )

    fun `test pub after fields`() = type(
        """
        struct Foo {
        	a int
        	pub<caret>
        }
        """.trimIndent(),
        ":",
        """
        struct Foo {
        	a int
        pub:<caret>
        }
        """.trimIndent()
    )

    fun `test pub after fields before fields`() = type(
        """
        struct Foo {
        	a int
        	pub<caret>
        	b int
        }
        """.trimIndent(),
        ":",
        """
        struct Foo {
        	a int
        pub:<caret>
        	b int
        }
        """.trimIndent()
    )

    // TODO: fix this behavior
    fun `test mut after fields before fields`() = type(
        """
        struct Foo {
        	a int
        	mut<caret>
        	b int
        }
        """.trimIndent(),
        ":",
        """
        struct Foo {
        	a int
        	mut:<caret>
        	b int
        }
        """.trimIndent()
    )
}
