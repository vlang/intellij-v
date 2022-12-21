package org.vlang.integration.goto

import org.vlang.integration.IntegrationTestBase

class GotoTest : IntegrationTestBase() {
    fun `test single interface`() = doTest {
        file("a.v", """
            interface Reader {
            	read()
            }

            struct /*caret 0*/Name {}

            fn (n Name) read() {}
        """.trimIndent())

        assertGotoSuper(0, "Reader")
    }

    fun `test single interface and struct with reference receiver`() = doTest {
        file("a.v", """
            interface Reader {
            	read()
            }

            struct /*caret 0*/Name {}

            fn (n &Name) read() {}
        """.trimIndent())

        assertGotoSuper(0, "Reader")
    }

    fun `test several interfaces`() = doTest {
        file("a.v", """
            interface Reader {
            	read()
            }
            
            interface Writer {
            	write()
            }

            struct /*caret 0*/Name {}

            fn (n Name) read() {}
            fn (n Name) write() {}
        """.trimIndent())

        assertGotoSuper(0, "Reader", "Writer")
    }

    fun `test embedded interfaces`() = doTest {
        file("a.v", """
            interface Reader {
            	read()
            }
            
            interface Writer {
            	write()
            }
            
            interface ReaderWriter {
            	read()
            	write()
            }

            struct /*caret 0*/Name {}

            fn (n Name) read() {}
            fn (n Name) write() {}
        """.trimIndent())

        assertGotoSuper(0, "Reader", "ReaderWriter", "Writer")
    }

// TODO: support this case
//
//    fun `test single interface and struct with embedding`() = doTest {
//        file("a.v", """
//            interface Reader {
//            	read()
//            }
//
//            struct /*caret 0*/ReaderImpl {}
//            struct /*caret 1*/Name {
//                ReaderImpl
//            }
//
//            fn (n ReaderImpl) read() {}
//        """.trimIndent())
//
//        assertGotoSuper(0, "Reader")
//        assertGotoSuper(1, "Reader")
//    }
}
