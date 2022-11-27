package org.vlang.integration.resolve

class ResolvingTest : IntegrationTestBase() {
    fun `test hello world`() = doTest {
        myFixture.configureByText("a.v", """
            fn main() {
                /*caret 0*/println("Hello, World!")
            }
        """.trimIndent())

        assertReferencedTo(0, "FUNCTION_DECLARATION builtin.println")
    }

    fun `test builtin resolved with prefix`() = doTest {
        myFixture.configureByText("a.v", """
            fn main() /*caret 0*/string {
                /*caret 1*/println("Hello, World!")
            }
        """.trimIndent())

        assertReferencedTo(0, "STRUCT_DECLARATION builtin.string")
        assertReferencedTo(1, "FUNCTION_DECLARATION builtin.println")
    }
}
