package org.vlang.lang.rename

class RenameTest : RenameBaseTest() {
    fun `test fun`() = doTest(
        """
        fn <caret>foo() {}
        fn main() {
            foo();
        }
        """,
        """
        fn bar() {}
        fn main() {
            bar();
        }
        """,
        "bar"
    )

    fun `test struct`() = doTest(
        """
        struct <caret>Foo {}
        fn main() {
            foo := Foo{}
        }
        """,
        """
        struct Bar {}
        fn main() {
            foo := Bar{}
        }
        """,
        "Bar"
    )

    fun `test struct field`() = doTest(
        """
        struct Foo {
            <caret>foo int
        }
        fn main() {
            foo := Foo{foo: 1}
            foo.foo = 2
        }
        """,
        """
        struct Foo {
            bar int
        }
        fn main() {
            foo := Foo{bar: 1}
            foo.bar = 2
        }
        """,
        "bar"
    )

    fun `test struct method`() = doTest(
        """
        struct Foo {
            foo int
        }
        fn (f Foo) <caret>foo() {
            f.foo = 1
        }
        fn main() {
            foo := Foo{foo: 1}
            foo.foo()
        }
        """,
        """
        struct Foo {
            foo int
        }
        fn (f Foo) bar() {
            f.foo = 1
        }
        fn main() {
            foo := Foo{foo: 1}
            foo.bar()
        }
        """,
        "bar"
    )

    fun `test enum`() = doTest(
        """
        enum <caret>Foo {
            a
        }
        fn main() {
            foo := Foo.a
        }
        """,
        """
        enum Bar {
            a
        }
        fn main() {
            foo := Bar.a
        }
        """,
        "Bar"
    )

    fun `test enum field`() = doTest(
        """
        enum Foo {
			<caret>a
        }
        fn main() {
            foo := Foo.a
        }
        """,
        """
        enum Foo {
			b
        }
        fn main() {
            foo := Foo.b
        }
        """,
        "b"
    )

    fun `test const`() = doTest(
        """
        const <caret>foo = 1
        fn main() {
            foo = 2
        }
        """,
        """
        const bar = 1
        fn main() {
            bar = 2
        }
        """,
        "bar"
    )

    fun `test type alias`() = doTest(
        """
        type <caret>Foo = int
        fn main() Foo {
        }
        """,
        """
        type Bar = int
        fn main() Bar {
        }
        """,
        "Bar"
    )

    fun `test type alias generic`() = doTest(
        """
        type <caret>Foo<T> = T
        fn main() Foo<int> {
        }
        """,
        """
        type Bar<T> = T
        fn main() Bar<int> {
        }
        """,
        "Bar"
    )

    fun `test var`() = doTest(
        """
        fn main() {
            <caret>foo := 1
            foo = 2
        }
        """,
        """
        fn main() {
            bar := 1
            bar = 2
        }
        """,
        "bar"
    )

    fun `test params`() = doTest(
        """
        fn foo(<caret>x int) {
            x = 1
        }
        fn main() {
            foo(2)
        }
        """,
        """
        fn foo(y int) {
            y = 1
        }
        fn main() {
            foo(2)
        }
        """,
        "y"
    )

    fun `test interface`() = doTest(
        """
        interface Foo {
            foo int
            <caret>foo()
        }
        fn main() {
            foo := Foo{}
            foo.foo()
            foo.foo
        }
        """,
        """
        interface Foo {
            foo int
            bar()
        }
        fn main() {
            foo := Foo{}
            foo.bar()
            foo.foo
        }
        """,
        "bar"
    )

    fun `test import alias`() = doTest(
        """
        import foo as <caret>bar
        fn main() {
            bar.foo()
        }
        """,
        """
        import foo as baz
        fn main() {
            baz.foo()
        }
        """,
        "baz"
    )
}
