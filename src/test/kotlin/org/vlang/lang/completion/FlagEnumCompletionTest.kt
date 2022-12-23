package org.vlang.lang.completion

class FlagEnumCompletionTest : CompletionTestBase() {
    fun `test methods for enum with flag attribute`() = checkEquals(
        """
        module main
            
        [flag]
        enum Permissions {
            read  // = 0b0001
            write // = 0b0010
            other // = 0b0100
        }
        
        fn main() {
            p := Permissions.read
            p./*caret*/
        }
        """.trimIndent(),
        1,
        "all", "has", "set", "toggle", "clear", "str", "other", "read", "write",
    )

    fun `test methods for enum without flag attribute`() = checkEquals(
        """
        module main
            
        enum Permissions {
            read  // = 0b0001
            write // = 0b0010
            other // = 0b0100
        }
        
        fn main() {
            p := Permissions.read
            p./*caret*/
        }
        """.trimIndent(),
        1,
        "str", "other", "read", "write",
    )

    fun `test variants for enum fetch`() = checkEquals(
        """
        module main
        
        [flag]
        enum Permissions {
            read  // = 0b0001
            write // = 0b0010
            other // = 0b0100
        }
        
        fn main() {
            p := Permissions.read
            p.has(./*caret*/)
        }
        """.trimIndent(),
        1,
        "other", "read", "write",
    )
}
