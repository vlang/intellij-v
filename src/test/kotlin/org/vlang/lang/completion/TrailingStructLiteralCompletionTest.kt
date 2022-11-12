package org.vlang.lang.completion

import com.intellij.codeInsight.completion.CompletionType

class TrailingStructLiteralCompletionTest : CompletionTestBase() {
    fun `test only one param`() = doTestCompletion(
        """
        module main

        struct Params {
            foo int
            boo int
        }
        
        fn foo(params Params) {}

        fn main() {
        	foo(fo<caret>)
        }
    """.trimIndent(), """
        module main

        struct Params {
            foo int
            boo int
        }
        
        fn foo(params Params) {}

        fn main() {
        	foo(foo: <caret>)
        }
    """.trimIndent()
    )

    fun `test only one param 1`() = doTestCompletion(
        """
        module main

        struct Params {
            foo int
            boo int
        }
        
        fn foo(params Params) {}

        fn main() {
        	foo(foo: 100, <caret>)
        }
    """.trimIndent(), """
        module main

        struct Params {
            foo int
            boo int
        }
        
        fn foo(params Params) {}

        fn main() {
        	foo(foo: 100, boo: <caret>)
        }
    """.trimIndent()
    )

    fun `test only one param 2`() = doTestVariants(
        """
        module main

        struct Params {
            foo int
            boo int
        }
        
        fn foo(params Params) {}

        fn main() {
        	foo(<caret>)
        }
    """.trimIndent(),
        CompletionType.BASIC, 1, CheckType.INCLUDES,
        "foo", "boo",
    )

    fun `test with extra one param`() = doTestCompletion(
        """
        module main

        struct Params {
            foo int
            boo int
        }
        
        fn boo(id int, params Params) {}

        fn main() {
        	boo(100, fo<caret>)
        }
    """.trimIndent(), """
        module main

        struct Params {
            foo int
            boo int
        }
        
        fn boo(id int, params Params) {}

        fn main() {
        	boo(100, foo: <caret>)
        }
    """.trimIndent()
    )

    fun `test with extra one param 2`() = doTestCompletion(
        """
        module main

        struct Params {
            foo int
            boo int
        }
        
        fn boo(id int, params Params) {}

        fn main() {
        	boo(100, foo: 100, <caret>)
        }
    """.trimIndent(), """
        module main

        struct Params {
            foo int
            boo int
        }
        
        fn boo(id int, params Params) {}

        fn main() {
        	boo(100, foo: 100, boo: <caret>)
        }
    """.trimIndent()
    )

    fun `test with extra one param after struct`() = doTestVariants(
        """
        module main

        struct Params {
            foo int
            boo int
        }
        
        fn boo(params Params, id int) {}

        fn main() {
        	boo(fo<caret>)
        }
    """.trimIndent(),
        CompletionType.BASIC, 1, CheckType.EXCLUDES,
        "foo", "boo", )

    fun `test with extra one string param`() = doTestVariants(
        """
        module main
        
        struct string {
            len int
            cap int
        }

        struct Params {
            foo int
            boo int
        }
        
        fn boo(str string, params Params) {}

        fn main() {
        	boo('', <caret>)
        }
    """.trimIndent(),
        CompletionType.BASIC, 1, CheckType.EXCLUDES,
        "len", "cap", )
}
