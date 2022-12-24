package org.vlang.lang.rename

class RenameTest : RenameBaseTest() {
    fun `test fun`() = doTest(
        """
        fn /*caret*/foo() {}
        fn main() {
            foo();
        }
        """.trimIndent(),
        """
        fn bar() {}
        fn main() {
            bar();
        }
        """.trimIndent(),
        "bar"
    )

    fun `test struct`() = doTest(
        """
        struct /*caret*/Foo {}
        fn main() {
            foo := Foo{}
        }
        """.trimIndent(),
        """
        struct Bar {}
        fn main() {
            foo := Bar{}
        }
        """.trimIndent(),
        "Bar"
    )

    fun `test struct field`() = doTest(
        """
        struct Foo {
            /*caret*/foo int
        }
        fn main() {
            foo := Foo{foo: 1}
            foo.foo = 2
        }
        """.trimIndent(),
        """
        struct Foo {
            bar int
        }
        fn main() {
            foo := Foo{bar: 1}
            foo.bar = 2
        }
        """.trimIndent(),
        "bar"
    )

    fun `test struct method`() = doTest(
        """
        struct Foo {
            foo int
        }
        fn (f Foo) /*caret*/foo() {
            f.foo = 1
        }
        fn main() {
            foo := Foo{foo: 1}
            foo.foo()
        }
        """.trimIndent(),
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
        """.trimIndent(),
        "bar"
    )

    fun `test enum`() = doTest(
        """
        enum /*caret*/Foo {
            a
        }
        fn main() {
            foo := Foo.a
        }
        """.trimIndent(),
        """
        enum Bar {
            a
        }
        fn main() {
            foo := Bar.a
        }
        """.trimIndent(),
        "Bar"
    )

    fun `test enum field`() = doTest(
        """
        module main
        
        enum Foo {
            /*caret*/a
        }
        fn main() {
            foo := Foo.a
        }
        """.trimIndent(),
        """
        module main
        
        enum Foo {
        	b
        }
        fn main() {
            foo := Foo.b
        }
        """.trimIndent(),
        "b"
    )

    fun `test const`() = doTest(
        """
        const /*caret*/foo = 1
        fn main() {
            foo = 2
        }
        """.trimIndent(),
        """
        const bar = 1
        fn main() {
            bar = 2
        }
        """.trimIndent(),
        "bar"
    )

    fun `test type alias`() = doTest(
        """
        type /*caret*/Foo = int
        fn main() Foo {
        }
        """.trimIndent(),
        """
        type Bar = int
        fn main() Bar {
        }
        """.trimIndent(),
        "Bar"
    )

    fun `test type alias generic`() = doTest(
        """
        type /*caret*/Foo<T> = T
        fn main() Foo<int> {
        }
        """.trimIndent(),
        """
        type Bar<T> = T
        fn main() Bar<int> {
        }
        """.trimIndent(),
        "Bar"
    )

    fun `test var`() = doTest(
        """
        fn main() {
            /*caret*/foo := 1
            foo = 2
        }
        """.trimIndent(),
        """
        fn main() {
            bar := 1
            bar = 2
        }
        """.trimIndent(),
        "bar"
    )

    fun `test params`() = doTest(
        """
        fn foo(/*caret*/x int) {
            x = 1
        }
        fn main() {
            foo(2)
        }
        """.trimIndent(),
        """
        fn foo(y int) {
            y = 1
        }
        fn main() {
            foo(2)
        }
        """.trimIndent(),
        "y"
    )

    fun `test interface`() = doTest(
        """
        module main
        
        interface Foo {
            foo int
            /*caret*/foo()
        }
        fn main() {
            foo := Foo{}
            foo.foo()
            foo.foo
        }
        """.trimIndent(),
        """
        module main
        
        interface Foo {
            foo int
            bar()
        }
        fn main() {
            foo := Foo{}
            foo.bar()
            foo.foo
        }
        """.trimIndent(),
        "bar"
    )

    fun `test import alias`() = doTest(
        """
        import foo as /*caret*/bar
        
        fn main() {
            bar.foo()
        }
        """.trimIndent(),
        """
        import foo as baz
        
        fn main() {
            baz.foo()
        }
        """.trimIndent(),
        "baz"
    )
}
