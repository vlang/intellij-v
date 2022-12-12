package org.vlang.integration.resolve

import org.vlang.integration.IntegrationTestBase

class ImportResolvingTest : IntegrationTestBase() {
    fun `test simple modules`() = doTest {
        myFixture.copyDirectoryToProject("fixtures/SimpleModules", "")
        file("main.v", """
            module main
            
            import mymodule
            import mymodule2
            import mymodule.inner
            import mymodule.inner.sub
            
            fn main() {
                /*caret 0*/mymodule./*caret 1*/my_func()
                /*caret 2*/inner./*caret 3*/inner()
                /*caret 4*/mymodule2./*caret 5*/my_func2()
                /*caret 6*/sub./*caret 7*/sub()
            }
        """.trimIndent())

        assertReferencedTo(0, "MODULE mymodule")
        assertReferencedTo(1, "FUNCTION_DECLARATION mymodule.my_func")
        assertReferencedTo(2, "MODULE mymodule.inner")
        assertReferencedTo(3, "FUNCTION_DECLARATION mymodule.inner.inner")
        assertReferencedTo(4, "MODULE mymodule2")
        assertReferencedTo(5, "FUNCTION_DECLARATION mymodule2.my_func2")
        assertReferencedTo(6, "MODULE mymodule.inner.sub")
        assertReferencedTo(7, "FUNCTION_DECLARATION mymodule.inner.sub.sub")
    }

    fun `test src based project`() = doTest {
        myFixture.copyDirectoryToProject("fixtures/SrcBasedSources", "")

        file("main.v", """
            module main
            
            import mymodule
            import mymodule.inner
            
            fn main() {
                /*caret 0*/mymodule./*caret 1*/my_func()
                /*caret 2*/inner./*caret 3*/inner()
            }
        """.trimIndent())

        assertReferencedTo(0, "MODULE mymodule")
        assertReferencedTo(1, "FUNCTION_DECLARATION mymodule.my_func")
        assertReferencedTo(2, "MODULE mymodule.inner")
        assertReferencedTo(3, "FUNCTION_DECLARATION mymodule.inner.inner")
    }

    fun `test local modules project`() = doTest {
        myFixture.copyDirectoryToProject("fixtures/LocalModuleSources", "")

        file("main.v", """
            module main
            
            import mymodule
            import mymodule.inner
            
            fn main() {
                /*caret 0*/mymodule./*caret 1*/my_func()
                /*caret 2*/inner./*caret 3*/inner()
            }
        """.trimIndent())

        assertReferencedTo(0, "MODULE mymodule")
        assertReferencedTo(1, "FUNCTION_DECLARATION mymodule.my_func")
        assertReferencedTo(2, "MODULE mymodule.inner")
        assertReferencedTo(3, "FUNCTION_DECLARATION mymodule.inner.inner")
    }

    fun `test imports resolve`() = doTest {
        myFixture.copyDirectoryToProject("fixtures/SimpleModules", "")
        file("main.v", """
            module main

            import /*caret 0*/mymodule./*caret 1*/inner

            fn main() {}
        """.trimIndent())

        assertReferencedTo(0, "MODULE mymodule")
        assertReferencedTo(1, "MODULE mymodule.inner")
    }

    fun `test complex type methods`() = doTest {
        myFixture.copyDirectoryToProject("fixtures/ComplexTypeMethods", "")
        file("main.v", """
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

    fun `test simple import`() = doTest {
        myFixture.copyDirectoryToProject("fixtures/SimpleModules", "")
       file("a.v", """
            import /*caret 0*/mymodule
            
            fn main() {
                /*caret 1*/mymodule./*caret 2*/my_func()
            }
        """.trimIndent())

        assertReferencedTo(0, "MODULE mymodule")
        assertReferencedTo(1, "MODULE mymodule")
        assertReferencedTo(2, "FUNCTION_DECLARATION mymodule.my_func")
    }

    fun `test simple import alias`() = doTest {
        myFixture.copyDirectoryToProject("fixtures/SimpleModules", "")
        file("a.v", """
            import /*caret 0*/mymodule as /*caret 1*/alias
            
            fn main() {
                /*caret 2*/alias./*caret 3*/my_func()
            }
        """.trimIndent())

        assertReferencedTo(0, "MODULE mymodule")
        assertReferencedTo(1, "MODULE mymodule")
        assertReferencedTo(2, "IMPORT_ALIAS alias")
        assertReferencedTo(3, "FUNCTION_DECLARATION mymodule.my_func")
    }

    fun `test selective import`() = doTest {
        myFixture.copyDirectoryToProject("fixtures/SimpleModules", "")
        file("a.v", """
            import /*caret 0*/mymodule {
                /*caret 1*/my_func
            }
            
            fn main() {
                /*caret 2*/my_func()
            }
        """.trimIndent())

        assertReferencedTo(0, "MODULE mymodule")
        assertReferencedTo(1, "FUNCTION_DECLARATION mymodule.my_func")
        assertReferencedTo(2, "FUNCTION_DECLARATION mymodule.my_func")
    }

    fun `test selective import with alias`() = doTest {
        myFixture.copyDirectoryToProject("fixtures/SimpleModules", "")
        file("a.v", """
            import /*caret 0*/mymodule as /*caret 1*/alias {
                /*caret 2*/my_func
            }
            
            fn main() {
                /*caret 3*/my_func()
                /*caret 4*/alias./*caret 5*/my_func()
            }
        """.trimIndent())

        assertReferencedTo(0, "MODULE mymodule")
        assertReferencedTo(1, "MODULE mymodule")
        assertReferencedTo(2, "FUNCTION_DECLARATION mymodule.my_func")
        assertReferencedTo(3, "FUNCTION_DECLARATION mymodule.my_func")
        assertReferencedTo(4, "IMPORT_ALIAS alias")
        assertReferencedTo(5, "FUNCTION_DECLARATION mymodule.my_func")
    }
}
