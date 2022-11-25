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

    fun `test simple modules`() = doTest {
        myFixture.copyDirectoryToProject("fixtures/SimpleModules", "")

        myFixture.configureByText("main.v", """
            module main
            
            import mymodule
            import mymodule2
            import mymodule.inner
            
            fn main() {
                /*caret 0*/mymodule./*caret 1*/my_func()
                /*caret 2*/inner./*caret 3*/inner()
                /*caret 4*/mymodule2./*caret 5*/my_func2()
            }
        """.trimIndent())

        assertReferencedTo(0, "IMPORT_NAME mymodule")
        assertReferencedTo(1, "FUNCTION_DECLARATION mymodule.my_func")
        assertReferencedTo(2, "IMPORT_NAME mymodule.inner")
        assertReferencedTo(3, "FUNCTION_DECLARATION mymodule.inner.inner")
        assertReferencedTo(4, "IMPORT_NAME mymodule2")
        assertReferencedTo(5, "FUNCTION_DECLARATION mymodule2.my_func2")
    }

    fun `test src based project`() = doTest {
        myFixture.copyDirectoryToProject("fixtures/SrcBasedSources", "")

        myFixture.configureByText("main.v", """
            module main
            
            import mymodule
            import mymodule.inner
            
            fn main() {
                /*caret 0*/mymodule./*caret 1*/my_func()
                /*caret 2*/inner./*caret 3*/inner()
            }
        """.trimIndent())

        assertReferencedTo(0, "IMPORT_NAME mymodule")
        assertReferencedTo(1, "FUNCTION_DECLARATION mymodule.my_func")
        assertReferencedTo(2, "IMPORT_NAME mymodule.inner")
        assertReferencedTo(3, "FUNCTION_DECLARATION mymodule.inner.inner")
    }

    fun `test local modules project`() = doTest {
        myFixture.copyDirectoryToProject("fixtures/LocalModuleSources", "")

        myFixture.configureByText("main.v", """
            module main
            
            import mymodule
            import mymodule.inner
            
            fn main() {
                /*caret 0*/mymodule./*caret 1*/my_func()
                /*caret 2*/inner./*caret 3*/inner()
            }
        """.trimIndent())

        assertReferencedTo(0, "IMPORT_NAME mymodule")
        assertReferencedTo(1, "FUNCTION_DECLARATION mymodule.my_func")
        assertReferencedTo(2, "IMPORT_NAME mymodule.inner")
        assertReferencedTo(3, "FUNCTION_DECLARATION mymodule.inner.inner")
    }

    fun `test imports resolve`() = doTest {
        myFixture.copyDirectoryToProject("fixtures/SimpleModules", "")
        myFixture.configureByText("main.v", """
            module main

            import /*caret 0*/mymodule./*caret 1*/inner

            fn main() {}
        """.trimIndent())

        assertReferencedTo(0, "DIRECTORY /src/mymodule")
        assertReferencedTo(1, "DIRECTORY /src/mymodule/inner")
    }

//    fun `test import from simple plain module`() = doTest {
//        myFixture.configureByText("main.v", """
//            module main
//
//            import simple
//
//            fn main() {
//                /*caret 0*/simple./*caret 1*/simple_func()
//            }
//        """.trimIndent())
//
//        assertReferencedTo(0, "IMPORT_NAME simple")
//        assertReferencedTo(1, "FUNCTION_DECLARATION mymodule.my_func")
//    }

    fun `test complex type methods`() = doTest {
        myFixture.copyDirectoryToProject("fixtures/ComplexTypeMethods", "")
        myFixture.configureByText("main.v", """
            module main

            import inner

            fn main() {
                arr := []inner.Foo{}
                arr./*caret 0*/foo()
                
                mp := map[string]inner.Foo{}
                mp./*caret 1*/boo()
            }
        """.trimIndent())

        assertReferencedTo(0, "METHOD_DECLARATION inner.foo")
        assertReferencedTo(1, "METHOD_DECLARATION inner.boo")
    }
}
