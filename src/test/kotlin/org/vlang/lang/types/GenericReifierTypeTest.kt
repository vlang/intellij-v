package org.vlang.lang.types

class GenericReifierTypeTest : TypeTestBase() {
    fun `test reify array`() = doTest(
        """
        fn foo<T>(a []T) T {
            return a
        }
        
        expr_type(foo([1, 2]), 'int')
        """.trimIndent()
    )

    fun `test reify map`() = doTest(
        """
        fn foo<T>(a map[string]T) T {
            return a
        }
        
        expr_type(foo({'': 100}), 'int')
        """.trimIndent()
    )

    fun `test reify option`() = doTest(
        """
        fn foo<T>(a ?T) T {
            return a
        }
        
        fn option() ?int { return 100 }
        expr_type(foo(option()), 'int')
        """.trimIndent()
    )

    fun `test reify result`() = doTest(
        """
        fn foo<T>(a !T) T {
            return a
        }
        
        fn result() !int { return 100 }
        expr_type(foo(result()), 'int')
        """.trimIndent()
    )

    fun `test reify pointer`() = doTest(
        """
        fn foo<T>(a &T) T {
            return a
        }
        
        fn pointer() &int { return 100 }
        expr_type(foo(pointer()), 'int')
        """.trimIndent()
    )

    fun `test reify channel`() = doTest(
        """
        fn foo<T>(a chan T) T {
            return a
        }
        
        fn get_chan() chan int { return 100 }
        expr_type(foo(get_chan()), 'int')
        """.trimIndent()
    )

    fun `test reify shared`() = doTest(
        """
        fn foo<T>(a shared T) T {
            return a
        }
        
        fn get_shared() shared int { return 100 }
        expr_type(foo(get_shared()), 'int')
        """.trimIndent()
    )

    fun `test reify tuple`() = doTest(
        """
        fn foo<T>(a (T, string)) T {
            return a
        }
        
        fn get_tuple() (int, string) { return 100, '' }
        expr_type(foo(get_tuple()), 'int')
        """.trimIndent()
    )

    fun `test reify function`() = doTest(
        """
        fn foo<T>(a fn () T) T {
            return a
        }
        
        fn get_int() int { return 100 }
        
        expr_type(foo(get_int), 'int')
        """.trimIndent()
    )

    fun `test reify function 2-1`() = doTest(
        """
        fn foo<T>(a fn (T)) T {
            return a
        }

        fn take_int(a int) {}

        expr_type(foo(take_int), 'int')
        """.trimIndent()
    )

    fun `test reify function 2`() = doTest(
        """
        fn foo<T>(a fn (a T)) T {
            return a
        }
        
        fn take_int(a int) {}
        
        expr_type(foo(take_int), 'int')
        """.trimIndent()
    )

    fun `test reify function 3`() = doTest(
        """
        fn foo<T, U>(a fn (a T) U) U {
            return a
        }
        
        fn take_int(a int) string {}
        
        expr_type(foo(take_int), 'string')
        """.trimIndent()
    )

    fun `test reify complex`() = doTest(
        """
        fn foo<T>(a (int, map[string][]fn (a T))) T {
            return a
        }
        
        fn take_complex() (int, map[string][]fn (a string)) {}
        
        expr_type(foo(take_complex()), 'string')
        """.trimIndent()
    )

    fun `test reify instantiation`() = doTest(
        """
        struct Test<T, U> {}

        fn get_t() Test<string, int> {}
        
        fn take_t<T>(t Test<T, int>) T {}
        
        expr_type(take_t(get_t()), 'string')
        """.trimIndent()
    )
}
