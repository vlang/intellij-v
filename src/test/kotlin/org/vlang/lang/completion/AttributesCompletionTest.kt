package org.vlang.lang.completion

class AttributesCompletionTest : CompletionTestBase() {
    fun `test simple attribute`() = doTestCompletion(
        """
        module main
        
        [un/*caret*/]
        fn main() {}
        """.trimIndent(),
        """
        module main
        
        [unsafe]
        fn main() {}
        """.trimIndent()
    )

    fun `test json attribute`() = doTestCompletion(
        """
        module main
        
        struct Name {
            name string [js/*caret*/]
        }
        """.trimIndent(),
        """
        module main
        
        struct Name {
            name string [json: 'name']
        }
        """.trimIndent()
    )

    fun `test deprecated attribute`() = doTestCompletion(
        """
        module main
        
        struct Name {
            name string [depre/*caret*/]
        }
        """.trimIndent(),
        """
        module main
        
        struct Name {
            name string [deprecated: '']
        }
        """.trimIndent()
    )

    fun `test all attribute variants`() = checkEquals(
        """
        module main
        
        struct Name {
            name string [/*caret*/]
        }
        """.trimIndent(),
        1,
        "assert_continues",
        "callconv",
        "console",
        "deprecated",
        "deprecated_after",
        "direct_array_access",
        "export",
        "flag",
        "heap",
        "inline",
        "json",
        "live",
        "manualfree",
        "noinit",
        "noinline",
        "nonnull",
        "noreturn",
        "params",
        "primary",
        "required",
        "skip",
        "sql",
        "table",
        "typedef",
        "unsafe",
        "keep_args_alive",
    )
}
