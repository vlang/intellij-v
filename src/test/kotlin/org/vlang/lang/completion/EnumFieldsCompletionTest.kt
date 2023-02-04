package org.vlang.lang.completion

class EnumFieldsCompletionTest : CompletionTestBase() {
    fun `test context function call`() = checkIncludes(
        """
        module main
            
        enum Permissions {
            read  
            write
            other
        }
        
        fn take(p Permissions) {}
        
        fn main() {
            take(/*caret*/)
        }
        """.trimIndent(),
        1,
         ".other", ".read", ".write",
    )

    fun `test context assignment`() = checkIncludes(
        """
        module main
            
        enum Permissions {
            read  
            write
            other
        }
        
        fn main() {
            a := Permissions.read
            a = /*caret*/
        }
        """.trimIndent(),
        1,
        ".other", ".read", ".write",
    )
}
