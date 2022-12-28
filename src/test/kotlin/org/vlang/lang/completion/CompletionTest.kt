package org.vlang.lang.completion

class CompletionTest : CompletionTestBase() {
    fun `test simple completion`() = checkIncludes(
        """
        module main
        
        struct Foo {
        }
        
        fn main() {
            /*caret*/
        }
        """.trimIndent(),
        1, "main", "Foo",
    )

    fun `test struct fields completion`() = checkEquals(
        """
        module main
        
        struct Foo {
        pub:
            name string
            age  int
        }
        
        fn main() {
            foo := Foo{}
            foo./*caret*/
        }
        """.trimIndent(),
        1, "name", "age", "str",
    )

    fun `test struct methods completion`() = checkEquals(
        """
        module main
        
        struct Foo {}
        
        fn (f Foo) bar() {}
        fn (f Foo) baz() {}
        
        fn main() {
            foo := Foo{}
            foo./*caret*/
        }
        """.trimIndent(),
        1, "bar", "baz", "str",
    )

    fun `test struct method and field with same name completion`() = checkEquals(
        """
        module main

        struct Foo {
            name string
        }
        
        fn (f Foo) name() {}
        
        fn main() {
            foo := Foo{}
            foo./*caret*/
        }
        """.trimIndent(),
        1, "name", "name", "str",
    )

    fun `test short string template completion`() = checkIncludes(
        """
        module main
        
        struct Foo {
        pub:
            name string
            age  int
        }
        
        fn main() {
            foo := Foo{}
            "${"$"}/*caret*/"
        }
        """.trimIndent(),
        1, "foo",
    )

    fun `test long string template member completion`() = checkEquals(
        """
        module main

        struct Foo {
        pub:
            name string
            age  int
        }

        fn main() {
            foo := Foo{}
            "${"$"}{foo./*caret*/}"
        }
        """.trimIndent(),
        1, "name", "age", "str",
    )

    fun `test long string template completion`() = checkIncludes(
        """
        module main
        
        struct Foo {
        pub:
            name string
            age  int
        }
        
        fn main() {
            foo := Foo{}
            "${"$"}{/*caret*/}"
        }
        """.trimIndent(),
        1, "foo",
    )

    fun `test struct name completion`() = doTestCompletion(
        """
        module main
        
        struct Name {}
        
        fn main() {
            Nam/*caret*/
        }
        """.trimIndent(),
        """
        module main
        
        struct Name {}
        
        fn main() {
            Name{}
        }
        """.trimIndent()
    )

    fun `test struct name before {} completion`() = doTestCompletion(
        """
        module main
        
        struct Name {}
        
        fn main() {
            Nam/*caret*/{}
        }
        """.trimIndent(),
        """
        module main
        
        struct Name {}
        
        fn main() {
            Name{}
        }
        """.trimIndent()
    )

    fun `test enum fetch`() = checkEquals(
        """
        module main
        
        enum Colors {
            red
            green
        }
        
        fn red() {}
        fn green() {}
        
        fn main() {
            a := Colors.red
            a = ./*caret*/
        }
        """.trimIndent(), 1, "red", "green",
    )

    fun `test map init`() = doTestCompletion(
        """
        module main
        
        fn main() {
            map/*caret*/
        }
        """.trimIndent(),
        """
        module main
        
        fn main() {
            map[string]int{}
        }
        """.trimIndent()
    )

    fun `test return in bool function`() = checkIncludes(
        """
        module main
        
        fn foo() bool {
            ret/*caret*/
        }
        """.trimIndent(), 0,
        "return true", "return false"
    )

    fun `test offset of fields`() = checkIncludes(
        """
        module main
        
        struct User {
            name string
            age  int
        }
        
        fn main() {
            __offsetof(User, /*caret*/)
        }
        """.trimIndent(), 0,
        "age", "name"
    )

    fun `test chan init`() = doTestCompletion(
        """
        module main
        
        fn main() {
            chan/*caret*/
        }
        """.trimIndent(),
        """
        module main
        
        fn main() {
            chan int{}
        }
        """.trimIndent()
    )

    fun `test label`() = checkEquals(
        """
        module main
        
        fn main() {
        label:
            goto /*caret*/
        }
        """.trimIndent(), 0,
        "label"
    )
}
