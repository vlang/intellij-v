package org.vlang.integration.rename

import org.vlang.integration.IntegrationTestBase

class RenameTest : IntegrationTestBase() {
    fun `test rename struct method with super`() = doTest {
        file("a.v", """
            module main
            
            interface Reader {
            	read()
            }

            struct Name {}

            fn (n Name) /*caret 0*/read() {}
        """.trimIndent())

        renameElement(0, "read2")
        checkFile("a.v", """
            module main
            
            interface Reader {
            	read2()
            }

            struct Name {}

            fn (n Name) /*caret 0*/read2() {}
        """.trimIndent())
    }

    fun `test rename struct method without super`() = doTest {
        file("a.v", """
            module main
            
            interface Reader {
            	read(name string) !int
            }

            struct Name {}

            fn (n Name) /*caret 0*/read() {}
        """.trimIndent())

        renameElement(0, "read2")
        checkFile("a.v", """
            module main
            
            interface Reader {
            	read(name string) !int
            }

            struct Name {}

            fn (n Name) /*caret 0*/read2() {}
        """.trimIndent())
    }

    fun `test rename struct method with super and more inheritors`() = doTest {
        file("a.v", """
            module main
            
            interface Reader {
            	read()
            }

            struct Name {}

            fn (n Name) /*caret 0*/read() {}
            
            struct Name2 {}
            
            fn (n Name2) read() {}
        """.trimIndent())

        renameElement(0, "read2")
        checkFile("a.v", """
            module main
            
            interface Reader {
            	read2()
            }

            struct Name {}

            fn (n Name) /*caret 0*/read2() {}
            
            struct Name2 {}
            
            fn (n Name2) read2() {}
        """.trimIndent())
    }

    fun `test rename struct field with super`() = doTest {
        file("a.v", """
            module main
            
            interface Reader {
            	my_name string
            }

            struct Name {
                /*caret 0*/my_name string
            }
        """.trimIndent())

        renameElement(0, "my_name2")
        checkFile("a.v", """
            module main
            
            interface Reader {
            	my_name2 string
            }

            struct Name {
                /*caret 0*/my_name2 string
            }
        """.trimIndent())
    }

    fun `test rename struct field without super`() = doTest {
        file("a.v", """
            module main
            
            interface Reader {
            	my_name string
            }

            struct Name {
                /*caret 0*/my_name2 string
            }
        """.trimIndent())

        renameElement(0, "my_name3")
        checkFile("a.v", """
            module main
            
            interface Reader {
            	my_name string
            }

            struct Name {
                /*caret 0*/my_name3 string
            }
        """.trimIndent())
    }

    fun `test rename struct field with super and more inheritors`() = doTest {
        file("a.v", """
            module main
            
            interface Reader {
            	my_name string
            }

            struct Name {
                /*caret 0*/my_name string
            }
            
            struct Name2 {
                my_name string
            }
        """.trimIndent())

        renameElement(0, "my_name2")
        checkFile("a.v", """
            module main
            
            interface Reader {
            	my_name2 string
            }

            struct Name {
                /*caret 0*/my_name2 string
            }
            
            struct Name2 {
                my_name2 string
            }
        """.trimIndent())
    }

    fun `test rename interface method with inheritor`() = doTest {
        file("a.v", """
            module main
            
            interface Reader {
            	/*caret 0*/read()
            }

            struct Name {}

            fn (n Name) read() {}
        """.trimIndent())

        renameElement(0, "read2")
        checkFile("a.v", """
            module main
            
            interface Reader {
            	/*caret 0*/read2()
            }

            struct Name {}

            fn (n Name) read2() {}
        """.trimIndent())
    }

    fun `test rename interface method with more inheritor`() = doTest {
        file("a.v", """
            module main
            
            interface Reader {
            	/*caret 0*/read()
            }

            struct Name {}

            fn (n Name) read() {}
            
            struct Name2 {}

            fn (n Name2) read() {}
        """.trimIndent())

        renameElement(0, "read2")
        checkFile("a.v", """
            module main
            
            interface Reader {
            	/*caret 0*/read2()
            }

            struct Name {}

            fn (n Name) read2() {}
            
            struct Name2 {}

            fn (n Name2) read2() {}
        """.trimIndent())
    }

    fun `test rename interface field with inheritor`() = doTest {
        file("a.v", """
            module main
            
            interface Reader {
            	/*caret 0*/my_name string
            }

            struct Name {
                my_name string
            }
        """.trimIndent())

        renameElement(0, "my_name2")
        checkFile("a.v", """
            module main
            
            interface Reader {
            	/*caret 0*/my_name2 string
            }

            struct Name {
                my_name2 string
            }
        """.trimIndent())
    }

    fun `test rename interface field with no inheritor`() = doTest {
        file("a.v", """
            module main
            
            interface Reader {
            	/*caret 0*/my_name string
            }
        """.trimIndent())

        renameElement(0, "my_name2")
        checkFile("a.v", """
            module main
            
            interface Reader {
            	/*caret 0*/my_name2 string
            }
        """.trimIndent())
    }

    fun `test rename interface field with more inheritors`() = doTest {
        file("a.v", """
            module main
            
            interface Reader {
            	/*caret 0*/my_name string
            }

            struct Name {
                my_name string
            }
            
            struct Name2 {
                my_name string
            }
        """.trimIndent())

        renameElement(0, "my_name2")
        checkFile("a.v", """
            module main
            
            interface Reader {
            	/*caret 0*/my_name2 string
            }

            struct Name {
                my_name2 string
            }
            
            struct Name2 {
                my_name2 string
            }
        """.trimIndent())
    }

    fun `test rename variable with variable conflict`() = doTest {
        file("a.v", """
            module main
            
            fn main() {
                a := 1
                /*caret 0*/a2 := 2
            }
        """.trimIndent())

        renameWithConflicts(0, "a", setOf("Variable 'a' is already declared in function 'main'"))
    }

    fun `test rename variable with parameter conflict`() = doTest {
        file("a.v", """
            module main
            
            fn main(a int) {
                /*caret 0*/a2 := 2
            }
        """.trimIndent())

        renameWithConflicts(0, "a", setOf("Parameter 'a' is already declared in function 'main'"))
    }
}