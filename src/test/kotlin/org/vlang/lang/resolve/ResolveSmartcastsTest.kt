package org.vlang.lang.resolve

class ResolveSmartcastsTest : ResolveTestBase() {
    fun `test simple smartcasts`() {
        mainFile("a.v", """
            module main
            
            interface IFoo {
                method() ?string
            }
            
            struct Foo {
                foo       string
                other_foo IFoo
            }
            
            fn (f &Foo) method() ?string {
                return none
            }
            
            struct Boo {
                boo       string
                other_boo IFoo
            }
            
            fn (b &Boo) method() ?string {
                return none
            }
            
            fn main(foo IFoo) {
                if foo is Foo {
                    foo./*caret*/foo
                    foo./*caret*/other_foo
                } else if foo is Boo {
                    foo./*caret*/boo
                    foo./*caret*/other_boo
                }
                
                if foo is Foo && foo./*caret*/foo == Foo{} {
                    // ...
                }
            }

        """.trimIndent())

        assertQualifiedReferencedTo("FIELD_DEFINITION main.Foo.foo")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.Foo.other_foo")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.Boo.boo")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.Boo.other_boo")

        assertQualifiedReferencedTo("FIELD_DEFINITION main.Foo.foo")
    }

    fun `test smartcasts for expression`() {
        mainFile("a.v", """
            module main
            
            interface IFoo {
                method() ?string
            }
            
            struct Foo {
                foo       string
                other_foo IFoo
            }
            
            fn (f &Foo) method() ?string {
                return none
            }
            
            struct Boo {
                boo       string
                other_boo IFoo
            }
            
            fn (b &Boo) method() ?string {
                return none
            }
            
            fn main(foo IFoo) {
                if foo is Foo {
                    if foo.other_foo is Boo {
                        foo.other_foo./*caret*/boo
                        foo.other_foo./*caret*/other_boo
                    }
                } else if foo is Boo {
                    if foo.other_boo is Foo {
                        foo.other_boo./*caret*/foo
                        foo.other_boo./*caret*/other_foo
                    }
                }
            }
        """.trimIndent())

        assertQualifiedReferencedTo("FIELD_DEFINITION main.Boo.boo")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.Boo.other_boo")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.Foo.foo")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.Foo.other_foo")
    }

    fun `test match smartcasts`() {
        mainFile("a.v", """
            module main
            
            interface IFoo {
                method() ?string
            }
            
            struct Foo {
                foo       string
                other_foo IFoo
            }
            
            fn (f &Foo) method() ?string {
                return none
            }
            
            struct Boo {
                boo       string
                other_boo IFoo
            }
            
            fn (b &Boo) method() ?string {
                return none
            }
            
            fn main(foo IFoo) {
                match foo {
                    Foo {
                        foo./*caret*/foo
                        foo./*caret*/other_foo
                    }
                    Boo {
                        foo./*caret*/boo
                        foo./*caret*/other_boo
                    }
                    else {
                        // ...
                    }
                }
            }

        """.trimIndent())

        assertQualifiedReferencedTo("FIELD_DEFINITION main.Foo.foo")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.Foo.other_foo")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.Boo.boo")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.Boo.other_boo")
    }
}
