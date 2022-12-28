package org.vlang.lang.completion

class TestCompletionTest : CompletionTestBase() {
    fun `test suite special functions inside non test files`() = checkExcludes(
        """
        fn/*caret*/
        """.trimIndent(), 1,
        "fn testsuite_begin", "fn testsuite_end"
    )

    fun `test suite special functions inside test function`() = checkExcludes(
        """
        // test
        fn main() {
          fn/*caret*/
        }
        """.trimIndent(), 1,
        "fn testsuite_begin", "fn testsuite_end"
    )

    fun `test suite special functions`() = checkIncludes(
        """
        // test
        fn/*caret*/
        """.trimIndent(), 1,
        "fn testsuite_begin", "fn testsuite_end"
    )

    fun `test suite special functions, with testsuite_begin`() = checkIncludes(
        """
        // test
        
        fn testsuite_begin() {}
        
        fn/*caret*/
        """.trimIndent(), 1,
         "fn testsuite_end"
    )

    fun `test suite special functions, with testsuite_end`() = checkIncludes(
        """
        // test
        
        fn testsuite_end() {}
        
        fn/*caret*/
        """.trimIndent(), 1,
        "fn testsuite_begin"
    )

    fun `test suite special functions, with testsuite_begin and testsuite_end`() = checkExcludes(
        """
        // test
        
        fn testsuite_begin() {}
        fn testsuite_end() {}
        
        fn/*caret*/
        """.trimIndent(), 1,
        "fn testsuite_begin", "fn testsuite_end"
    )
}
