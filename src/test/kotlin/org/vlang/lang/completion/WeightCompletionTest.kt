package org.vlang.lang.completion

class WeightCompletionTest : CompletionTestBase() {
    fun `test immutable receiver`() = checkOrderedEquals(
        """
        module main
        
        struct Foo {}
        fn (f &Foo) method_immutable() {}
        fn (mut f Foo) method_mutable() {}
        
        fn main() {
            foo := Foo{}
            foo./*caret*/
        }

        """.trimIndent(), 0,
        "method_immutable",
        "str",
        "method_mutable",
    )

    fun `test mutable receiver`() = checkOrderedEquals(
        """
        module main
        
        struct Foo {}
        fn (f &Foo) method_immutable() {}
        fn (mut f Foo) method_mutable() {}
        
        fn main() {
            mut foo := Foo{}
            foo./*caret*/
        }

        """.trimIndent(), 0,
        "method_immutable",
        "method_mutable",
        "str",
    )

    fun `test struct field completion inside method`() = checkOrderedEquals(
        """
        module main
        
        struct Foo {
            field int
        }
        
        fn (f &Foo) method() {
            fiel/*caret*/
        }
        """.trimIndent(), 0,
        "f.field",
    )

    fun `test struct method completion inside method`() = checkOrderedEquals(
        """
        module main
        
        struct Foo {}
        
        fn (f &Foo) method() {}
        
        fn (f &Foo) method2() {
            meth/*caret*/
        }
        """.trimIndent(), 0,
        "f.method",
        "f.method2",
        "@METHOD",
    )
}
