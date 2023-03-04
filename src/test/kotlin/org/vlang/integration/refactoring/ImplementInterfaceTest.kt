package org.vlang.integration.refactoring

import org.vlang.integration.IntegrationTestBase

class ImplementInterfaceTest : IntegrationTestBase() {
    fun `test implementation interface from third level module`() = doTest {
        directory("mod") {
            file("mod.v", """
                module mod
                """.trimIndent())

            directory("sub") {
                file("sub.v", """
                module sub
                
                pub type MyString = string
                
                pub interface IFoo {
                    name MyString
                    foo(a MyString, b MyString) MyString
                }
                
                """.trimIndent())
            }
        }

        file("a.v", """
            module foo
            
            struct /*caret 0*/Foo {}
            
            fn main() {
            }
        """.trimIndent())

        implementInterface(0, "mod.sub.IFoo")

        checkFile("a.v", """
            module foo
            
            import mod.sub
            
            struct /*caret 0*/Foo {
            	name sub.MyString
            }
            
            fn (f &Foo) foo(a sub.MyString, b sub.MyString) sub.MyString {
            	panic('not implemented')
            }
            
            fn main() {
            }
        """.trimIndent())
    }
}