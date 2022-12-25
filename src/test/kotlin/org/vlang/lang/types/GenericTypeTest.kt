package org.vlang.lang.types

class GenericTypeTest : TypeTestBase() {
    fun `test implicit one parameter func`() = doTest(
        """
        fn tuple(string) (int, int) { return 1, 2 }
        struct Foo {}
            
        fn mirror<T>(a T) T {
            return a
        }
        
        expr_type(mirror(1), 'int')
        expr_type(mirror('string'), 'string')
        expr_type(mirror(Foo{}), 'Foo')
        expr_type(mirror([Foo{}]), '[]Foo')
        expr_type(mirror({'': Foo{}}), 'map[string]Foo')
        expr_type(mirror(tuple('')), '(int, int)')
        expr_type(mirror(tuple), 'fn (string) (int, int)')
        """.trimIndent()
    )

    fun `test implicit two parameter func`() = doTest(
        """
        fn tuple(string) (int, int) { return 1, 2 }
        struct Foo {}
            
        fn mirror<T, U>(a T, b U) U {
            return a
        }
        
        expr_type(mirror(0, 1), 'int')
        expr_type(mirror(0, 'string'), 'string')
        expr_type(mirror(0, Foo{}), 'Foo')
        expr_type(mirror(0, [Foo{}]), '[]Foo')
        expr_type(mirror(0, {'': Foo{}}), 'map[string]Foo')
        expr_type(mirror(0, tuple('')), '(int, int)')
        expr_type(mirror(0, tuple), 'fn (string) (int, int)')
        """.trimIndent()
    )

    fun `test explicit one parameter func`() = doTest(
        """
        fn tuple(string) (int, int) { return 1, 2 }
        struct Foo {}
            
        fn mirror<T>(a T) T {
            return a
        }
        
        expr_type(mirror<int>(1), 'int')
        expr_type(mirror<string>('string'), 'string')
        expr_type(mirror<Foo>(Foo{}), 'Foo')
        expr_type(mirror<[]Foo>([Foo{}]), '[]Foo')
        expr_type(mirror<map[string]Foo>({'': Foo{}}), 'map[string]Foo')
        expr_type(mirror<(int, int)>(tuple('')), '(int, int)')
        expr_type(mirror<fn (string) (int, int)>(tuple), 'fn (string) (int, int)')
        """.trimIndent()
    )

    fun `test explicit two parameter func`() = doTest(
        """
        fn tuple(string) (int, int) { return 1, 2 }
        struct Foo {}
            
        fn mirror<T, U>(a T, b U) U {
            return a
        }
        
        expr_type(mirror<int, int>(0, 1), 'int')
        expr_type(mirror<int, string>(0, 'string'), 'string')
        expr_type(mirror<int, Foo>(0, Foo{}), 'Foo')
        expr_type(mirror<int, []Foo>(0, [Foo{}]), '[]Foo')
        expr_type(mirror<int, map[string]Foo>(0, {'': Foo{}}), 'map[string]Foo')
        expr_type(mirror<int, (int, int)>(0, tuple('')), '(int, int)')
        expr_type(mirror<int, fn (string) (int, int)>(0, tuple), 'fn (string) (int, int)')
        """.trimIndent()
    )

    fun `test complex return type`() = doTest(
        """
        fn mirror<T>(a T) ?&T {
            return a
        }
        
        expr_type(mirror<int>(1), '?&int')
        
        fn mirror2<T>(a T) map[string]?&T {
            return a
        }
        
        expr_type(mirror2<int>(1), 'map[string]?&int')
        """.trimIndent()
    )

    fun `test single generic struct with method without generics`() = doTest(
        """
        struct Foo<T> {}
        
        fn (f Foo<T>) bar() T {}
        
        fn main() {
            foo := Foo<int>{}
            expr_type(foo.bar(), 'int')
        }
        """.trimIndent()
    )

    fun `test single reference generic struct with method without generics`() = doTest(
        """
        struct Foo<T> {}
        
        fn (f Foo<T>) bar() T {}
        
        fn main() {
            foo := &Foo<int>{}
            expr_type(foo.bar(), 'int')
        }
        """.trimIndent()
    )

    fun `test two generic struct with method without generics`() = doTest(
        """
        struct Foo<T, U> {}
        
        fn (f Foo<T, U>) bar() U {}
        
        fn main() {
            foo := Foo<int, string>{}
            expr_type(foo.bar(), 'string')
        }
        """.trimIndent()
    )

    fun `test single generic struct nested in self with method without generics`() = doTest(
        """
        struct Foo<T> {}
        
        fn (f Foo<T>) bar() T {}
        
        fn main() {
            foo := Foo<Foo<int>>{}
            expr_type(foo, 'Foo[Foo[int]]')
            expr_type(foo.bar(), 'Foo[int]')
            expr_type(foo.bar().bar(), 'int')
        }
        """.trimIndent()
    )

    fun `test single generic struct with method with generic`() = doTest(
        """
        struct Foo<T> {}
        
        fn (f Foo<T>) bar<U>() U {}
        
        fn main() {
            foo := Foo<int>{}
            expr_type(foo.bar<string>(), 'string')
        }
        """.trimIndent()
    )

    fun `test single generic interface with method without generic`() = doTest(
        """
        module main
        
        interface Test<T> {
            foo() T
        }
        
        fn foo(t Test<int>) {
            expr_type(t.foo(), 'int')
        }

        """.trimIndent()
    )

    fun `test alias for generic struct`() = doTest(
        """
        struct Foo<T> {}
        
        fn (f Foo<T>) bar() T {}
        
        type FooT = Foo<int>
        type FooT2 = Foo<string>
        
        fn main() {
            foo := FooT{}
            expr_type(foo.bar(), 'int')
            foo2 := FooT2{}
            expr_type(foo2.bar(), 'string')
        }
        """.trimIndent()
    )

    fun `test alias for generic struct with field`() = doTest(
        """
        struct Foo<T> {
            field T
        }
        
        type FooT = Foo<int>
        type FooT2 = Foo<string>
        
        fn main() {
            foo := FooT{}
            expr_type(foo.field, 'int')
            foo2 := FooT2{}
            expr_type(foo2.field, 'string')
        }
        """.trimIndent()
    )

    fun `test alias for generic struct with field with optional and result type`() = doTest(
        """
        struct Foo<T> {
            field T
        }
        
        type FooT = Foo<int>
        type FooT2 = Foo<string>
        
        fn get_result() ?FooT {
            return FooT{}
        }
        
        fn get_optional() !FooT2 {
            return FooT2{}
        }
        
        fn main() {
            foo := get_result()
            expr_type(foo?.field, 'int')
            foo2 := get_optional()
            expr_type(foo2!.field, 'string')
        }
        """.trimIndent()
    )

    fun `test call with new syntax`() = doTest(
        """
        fn some[T]() T {}
        
        fn main() {
            expr_type(some(), 'T')
            expr_type(some[int](), 'int')
            expr_type(some[string](), 'string')
        }
        """.trimIndent()
    )

    fun `test generic struct init`() = doTest(
        """
        struct Foo[T] {
            field T
        }
        
        fn main() {
            foo := Foo[int]{
                field: 1
            }
            
            expr_type(foo.field, 'int')
        }
        """.trimIndent()
    )
}
