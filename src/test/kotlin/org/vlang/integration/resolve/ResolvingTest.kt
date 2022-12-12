package org.vlang.integration.resolve

import org.vlang.integration.IntegrationTestBase

class ResolvingTest : IntegrationTestBase() {
    fun `test hello world`() = doTest {
        file("a.v", """
            fn main() {
                /*caret 0*/println("Hello, World!")
            }
        """.trimIndent())

        assertReferencedTo(0, "FUNCTION_DECLARATION builtin.println")
    }

    fun `test builtin resolved with prefix`() = doTest {
        file("a.v", """
            fn main() /*caret 0*/string {
                /*caret 1*/println("Hello, World!")
            }
        """.trimIndent())

        assertReferencedTo(0, "STRUCT_DECLARATION builtin.string")
        assertReferencedTo(1, "FUNCTION_DECLARATION builtin.println")
    }

    fun `test qualifier name with current module mane`() = doTest {
        file("a.v", """
            module foo
            
            fn boo() {
                /*caret 0*/foo./*caret 1*/boo()
            }
        """.trimIndent())

        assertReferencedTo(0, "MODULE foo")
        assertReferencedTo(1, "FUNCTION_DECLARATION foo.boo")
    }

    fun `test resolve module with same name with param`() = doTest {
        myFixture.copyDirectoryToProject("modules/simple", "simple")
        file("a.v", """
            module main
            
            import /*caret 0*/simple
            
            fn foo(simple /*caret 1*/simple./*caret 2*/SimpleStruct) {}
        """.trimIndent())

        assertReferencedTo(0, "MODULE simple")
        assertReferencedTo(1, "MODULE simple")
        assertReferencedTo(2, "STRUCT_DECLARATION simple.SimpleStruct")
    }
}
