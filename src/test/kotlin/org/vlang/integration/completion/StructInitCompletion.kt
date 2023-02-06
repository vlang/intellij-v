package org.vlang.integration.completion

import com.intellij.codeInsight.lookup.Lookup
import org.vlang.integration.IntegrationTestBase

class StructInitCompletion : IntegrationTestBase() {
    fun `test struct init from different module`() = doTest {
        directory("mod") {
            file("mod.v", """
                module mod
                
                pub struct Foo {
                    idx int
                pub:
                    name string
                mut:
                    age  int
                pub mut:
                    other bool
                }
            """.trimIndent())
        }

        file("a.v", """
            module foo
            
            import mod
            
            fn main() {
                f := mod.Foo{
                    name: 'foo',
                    /*caret 0*/
                }
            }
        """.trimIndent())

        completion(caretIndex = 0) {
            equalsTo("other")
            // age is not visible
            // idx is not visible
        }
    }

    fun `test fill all fields in struct init from different module`() = doTest {
        directory("mod") {
            file("mod.v", """
                module mod
                
                pub struct Foo {
                    idx int
                pub:
                    name string
                mut:
                    age  int
                pub mut:
                    other bool
                }
            """.trimIndent())
        }

        file("a.v", """
            module foo
            
            import mod
            
            fn main() {
            	f := mod.Foo{
            		/*caret 0*/
            	}
            }
        """.trimIndent())

        completion(caretIndex = 0) {
            myFixture.finishLookup(Lookup.NORMAL_SELECT_CHAR)
        }

        checkFile("a.v", """
            module foo
            
            import mod
            
            fn main() {
            	f := mod.Foo{
            		name: ''
            		other: false	/*caret 0*/
            	}
            }
        """.trimIndent())
    }

    fun `test fill all fields in struct with struct from different module`() = doTest {
        directory("mod") {
            file("mod.v", """
                module mod
                
                pub struct Foo {
                    idx int
                }
            """.trimIndent())
        }

        file("a.v", """
            module foo
            
            import mod
            
            struct Boo {
                foo   mod.Foo
                other int
            }
            
            fn main() {
            	f := Boo{
            		/*caret 0*/
            	}
            }
        """.trimIndent())

        completion(caretIndex = 0) {
            myFixture.finishLookup(Lookup.NORMAL_SELECT_CHAR)
        }

        checkFile("a.v", """
            module foo
            
            import mod
            
            struct Boo {
                foo   mod.Foo
                other int
            }
            
            fn main() {
            	f := Boo{
            		foo: mod.Foo{}
            		other: 0	/*caret 0*/
            	}
            }
        """.trimIndent())
    }
}