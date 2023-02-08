package org.vlang.integration.goto

import org.vlang.integration.IntegrationTestBase
import org.vlang.lang.search.*

class GotoTest : IntegrationTestBase() {
    fun `test interface with method`() = doTest {
        file("a.v", """
            module main
            
            interface /*caret 0*/Reader {
            	/*caret 1*/read()
            }

            struct /*caret 2*/Name {}

            fn (n Name) /*caret 3*/read() {}
        """.trimIndent())

        assertSearch(0, VlangInheritorsSearch, "main.Name")
        assertSearch(1, VlangMethodInheritorsSearch, "main.Name.read")
        assertSearch(2, VlangSuperSearch, "main.Reader")
        assertSearch(3, VlangSuperMethodSearch, "main.Reader.read")
    }

    fun `test interface with field`() = doTest {
        file("a.v", """
            module main
            
            interface /*caret 0*/Reader {
            	/*caret 1*/my_name string
            }

            struct /*caret 2*/Name {
                /*caret 3*/my_name string
            }
        """.trimIndent())

        assertSearch(0, VlangInheritorsSearch, "main.Name")
        assertSearch(1, VlangFieldInheritorsSearch, "main.Name.my_name")
        assertSearch(2, VlangSuperSearch, "main.Reader")
        assertSearch(3, VlangSuperFieldSearch, "main.Reader.my_name")
    }

    fun `test interface with method and field`() = doTest {
        file("a.v", """
            module main
            
            interface /*caret 0*/Reader {
                /*caret 1*/read() !int
            	/*caret 2*/my_name string
            }

            struct /*caret 3*/Name {
                /*caret 4*/my_name string
            }
            
            fn (n &Name) /*caret 5*/read() !int {
                
            }
        """.trimIndent())

        assertSearch(0, VlangInheritorsSearch, "main.Name")
        assertSearch(1, VlangMethodInheritorsSearch, "main.Name.read")
        assertSearch(2, VlangFieldInheritorsSearch, "main.Name.my_name")
        assertSearch(3, VlangSuperSearch, "main.Reader")
        assertSearch(4, VlangSuperFieldSearch, "main.Reader.my_name")
        assertSearch(5, VlangSuperMethodSearch, "main.Reader.read")
    }

    fun `test single interface method`() = doTest {
        file("a.v", """
            module main
            
            interface /*caret 0*/Reader {
            	 /*caret 1*/read()
            }

            struct /*caret 2*/Name {}

            fn (n Name) /*caret 3*/read() {}
        """.trimIndent())

        assertSearch(0, VlangInheritorsSearch, "main.Name")
        assertSearch(1, VlangMethodInheritorsSearch, "main.Name.read")
        assertSearch(2, VlangSuperSearch, "main.Reader")
        assertSearch(3, VlangSuperMethodSearch, "main.Reader.read")
    }

    fun `test single interface with complex signature`() = doTest {
        file("a.v", """
            module main
            
            interface /*caret 0*/Reader {
            	/*caret 1*/read([]string) !int
            }

            struct /*caret 2*/Name {}

            fn (n Name) /*caret 3*/read([]string) !int {}
        """.trimIndent())

        assertSearch(0, VlangInheritorsSearch, "main.Name")
        assertSearch(1, VlangMethodInheritorsSearch, "main.Name.read")
        assertSearch(2, VlangSuperSearch, "main.Reader")
        assertSearch(3, VlangSuperMethodSearch, "main.Reader.read")
    }

    fun `test single interface and struct with reference receiver`() = doTest {
        file("a.v", """
            module main
            
            interface /*caret 0*/Reader {
            	/*caret 1*/read()
            }

            struct /*caret 2*/Name {}

            fn (n &Name) /*caret 3*/read() {}
        """.trimIndent())

        assertSearch(0, VlangInheritorsSearch, "main.Name")
        assertSearch(1, VlangMethodInheritorsSearch, "main.Name.read")
        assertSearch(2, VlangSuperSearch, "main.Reader")
        assertSearch(3, VlangSuperMethodSearch, "main.Reader.read")
    }

    fun `test several interfaces`() = doTest {
        file("a.v", """
            module main
            
            interface /*caret 0*/Reader {
            	/*caret 1*/read()
            }
            
            interface /*caret 2*/Writer {
            	/*caret 3*/write()
            }

            struct /*caret 4*/Name {}

            fn (n Name) /*caret 5*/read() {}
            fn (n Name) /*caret 6*/write() {}
        """.trimIndent())

        assertSearch(0, VlangInheritorsSearch, "main.Name")
        assertSearch(1, VlangMethodInheritorsSearch, "main.Name.read")
        assertSearch(2, VlangInheritorsSearch, "main.Name")
        assertSearch(3, VlangMethodInheritorsSearch, "main.Name.write")
        assertSearch(4, VlangSuperSearch, "main.Reader", "main.Writer")
        assertSearch(5, VlangSuperMethodSearch, "main.Reader.read")
        assertSearch(6, VlangSuperMethodSearch, "main.Writer.write")
    }

    fun `test embedded interfaces`() = doTest {
        file("a.v", """
            module main
            
            interface /*caret 0*/Reader {
            	/*caret 1*/read()
            }
            
            interface /*caret 2*/Writer {
            	/*caret 3*/write()
            }
            
            interface /*caret 4*/ReaderWriter {
            	/*caret 5*/read()
            	/*caret 6*/write()
            }

            struct /*caret 7*/Name {}

            fn (n Name) /*caret 8*/read() {}
            fn (n Name) /*caret 9*/write() {}
        """.trimIndent())

        assertSearch(0, VlangInheritorsSearch, "main.Name")
        assertSearch(1, VlangMethodInheritorsSearch, "main.Name.read")
        assertSearch(2, VlangInheritorsSearch, "main.Name")
        assertSearch(3, VlangMethodInheritorsSearch, "main.Name.write")
        assertSearch(4, VlangInheritorsSearch, "main.Name")
        assertSearch(5, VlangMethodInheritorsSearch, "main.Name.read")
        assertSearch(6, VlangMethodInheritorsSearch, "main.Name.write")
        assertSearch(7, VlangSuperSearch, "main.Reader", "main.ReaderWriter", "main.Writer")
        assertSearch(8, VlangSuperMethodSearch, "main.Reader.read", "main.ReaderWriter.read")
        assertSearch(9, VlangSuperMethodSearch, "main.Writer.write", "main.ReaderWriter.write")
    }

    fun `test interface and struct with different signature`() = doTest {
        file("a.v", """
            interface /*caret 0*/Reader {
            	/*caret 1*/read()
            }

            struct /*caret 2*/Name {}

            fn (n Name) /*caret 3*/read(name string) {}
        """.trimIndent())

        assertNoSearchResults(0, VlangInheritorsSearch)
        assertNoSearchResults(1, VlangMethodInheritorsSearch)
        assertNoSearchResults(2, VlangSuperSearch)
        assertNoSearchResults(3, VlangSuperMethodSearch)
    }

    fun `test single interface and struct with embedding fields`() = doTest {
        file("a.v", """
            module main
            
            interface Component {
            	id string
                name string
            }
            
            struct IdOwner {
                id string
            }

            struct /*caret 0*/Name {
                IdOwner
                name string
            }
        """.trimIndent())

        assertSearch(0, VlangSuperSearch, "main.Component")
    }

    fun `test single interface and struct with embedding methods`() = doTest {
        file("a.v", """
            module main
            
            interface Reader {
            	read()
            }

            struct /*caret 0*/ReaderImpl {}
            struct /*caret 1*/Name {
                ReaderImpl
            }

            fn (n ReaderImpl) read() {}
        """.trimIndent())

        assertSearch(0, VlangSuperSearch, "main.Reader")
        assertSearch(1, VlangSuperSearch, "main.Reader")
    }

    fun `test two interfaces and struct with embedding methods`() = doTest {
        file("a.v", """
            module main
            
            interface Reader {
            	read()
            }
            
            interface Writer {
            	write()
            }

            struct /*caret 0*/ReaderImpl {}
            struct /*caret 1*/WriterImpl {}
            struct /*caret 2*/Name {
                ReaderImpl
                WriterImpl
            }

            fn (n ReaderImpl) read() {}
            fn (n WriterImpl) write() {}
        """.trimIndent())

        assertSearch(0, VlangSuperSearch, "main.Reader")
        assertSearch(1, VlangSuperSearch, "main.Writer")
        assertSearch(2, VlangSuperSearch, "main.Reader", "main.Writer")
    }

    fun `test single interface and struct with embedding fields and methods`() = doTest {
        file("a.v", """
            module main
            
            interface Reader {
                id string
            	read()
            }

            struct /*caret 0*/ReaderImpl {
                id string
            }
            struct /*caret 1*/Name {
                ReaderImpl
            }

            fn (n ReaderImpl) read() {}
        """.trimIndent())

        assertSearch(0, VlangSuperSearch, "main.Reader")
        assertSearch(1, VlangSuperSearch, "main.Reader")
    }
}
