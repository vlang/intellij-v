package org.vlang.integration.resolve

import org.vlang.integration.IntegrationTestBase

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

    fun `test qualifier name with current module mane`() = doTest {
        myFixture.configureByText("a.v", """
            module foo
            
            fn boo() {
                /*caret 0*/foo./*caret 1*/boo()
            }
        """.trimIndent())

        assertReferencedTo(0, "MODULE foo")
        assertReferencedTo(1, "FUNCTION_DECLARATION foo.boo")
    }
}
