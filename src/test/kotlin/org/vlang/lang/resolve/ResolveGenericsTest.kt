package org.vlang.lang.resolve

class ResolveGenericsTest : ResolveTestBase() {
    fun `test function generic parameters`() {
        mainFile("a.v", """
            fn foo[T](t /*caret*/T) {}
            fn foo[T, U](t /*caret*/T) /*caret*/U {
                return /*caret*/U(t)
            }
        """.trimIndent())

        assertReferencedTo("GENERIC_PARAMETER T")
        assertReferencedTo("GENERIC_PARAMETER T")
        assertReferencedTo("GENERIC_PARAMETER U")
        assertReferencedTo("GENERIC_PARAMETER U")
    }

    fun `test field generic parameters`() {
        mainFile("a.v", """
            struct Test[T] {
                name /*caret*/T
                other map[string]/*caret*/T
            }
        """.trimIndent())

        assertReferencedTo("GENERIC_PARAMETER T")
        assertReferencedTo("GENERIC_PARAMETER T")
    }

    fun `test interface method generic parameters`() {
        mainFile("a.v", """
            interface Test[T] {
                get() /*caret*/T
                set(a /*caret*/T)
                map(a map[string]/*caret*/T) []/*caret*/T
            }
        """.trimIndent())

        assertReferencedTo("GENERIC_PARAMETER T")
        assertReferencedTo("GENERIC_PARAMETER T")
        assertReferencedTo("GENERIC_PARAMETER T")
        assertReferencedTo("GENERIC_PARAMETER T")
    }

    fun `test method generic parameters`() {
        mainFile("a.v", """
            struct Test[T] {}
            
            fn (t Test[T]) name() /*caret*/T {}
            fn (t Test[T]) name(a /*caret*/T) /*caret*/T {}
            fn (t Test[T]) name(a map[string]/*caret*/T) /*caret*/T {}
        """.trimIndent())

        assertReferencedTo("GENERIC_PARAMETER T")
        assertReferencedTo("GENERIC_PARAMETER T")
        assertReferencedTo("GENERIC_PARAMETER T")
        assertReferencedTo("GENERIC_PARAMETER T")
        assertReferencedTo("GENERIC_PARAMETER T")
    }

    fun `test single generic struct with method with generic`() {
        mainFile("a.v", """
        struct Foo[T] {}
        
        fn (f Foo[T]) bar[U](a /*caret*/T) /*caret*/U {}
        """.trimIndent())

        assertReferencedTo("GENERIC_PARAMETER T")
        assertReferencedTo("GENERIC_PARAMETER U")
    }

    fun `test receiver generic parameter`() {
        mainFile("a.v", """
        struct Foo[T] {}
        
        fn (f Foo[/*caret*/T]) bar[U](a T) U {}
        """.trimIndent())

        assertReferencedTo("GENERIC_PARAMETER T")
    }

    fun `test two receiver generic parameter`() {
        mainFile("a.v", """
        struct Foo[T, U] {}
        
        fn (f Foo[/*caret*/T, /*caret*/U]) ba(a T) U {}
        """.trimIndent())

        assertReferencedTo("GENERIC_PARAMETER T")
        assertReferencedTo("GENERIC_PARAMETER U")
    }

    fun `test pointer receiver generic parameter`() {
        mainFile("a.v", """
        struct Foo[T] {}
        
        fn (f &Foo[/*caret*/T]) bar(a T) {}
        """.trimIndent())

        assertReferencedTo("GENERIC_PARAMETER T")
    }
}
