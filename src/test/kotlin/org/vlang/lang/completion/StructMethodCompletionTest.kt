package org.vlang.lang.completion

class StructMethodCompletionTest : CompletionTestBase() {
    fun `test simple struct`() = doTestCompletion(
        """
        module main
        
        struct Book {}
        
        fn/*caret*/
        """.trimIndent(),
        """
        module main
        
        struct Book {}
        
        fn (b &Book) method()  {
        	
        }
        """.trimIndent()
    )

    fun `test struct with methods`() = doTestCompletion(
        """
        module main
        
        struct Book {}
        
        fn (book &Book) some() {}
        
        fn/*caret*/
        """.trimIndent(),
        """
        module main
        
        struct Book {}
        
        fn (book &Book) some() {}
        
        fn (book &Book) method()  {
        	
        }
        """.trimIndent()
    )

    fun `test struct with value method`() = doTestCompletion(
        """
        module main
        
        struct Book {}
        
        fn (book Book) get() {}
        
        fn/*caret*/
        """.trimIndent(),
        """
        module main
        
        struct Book {}
        
        fn (book Book) get() {}
        
        fn (book Book) method()  {
        	
        }
        """.trimIndent()
    )

    fun `test struct with mixed methods`() = doTestCompletion(
        """
        module main
        
        struct Book {}
        
        fn (book Book) get() {}
        fn (book &Book) set() {}
        
        fn/*caret*/
        """.trimIndent(),
        """
        module main
        
        struct Book {}
        
        fn (book Book) get() {}
        fn (book &Book) set() {}
        
        fn (book &Book) method()  {
        	
        }
        """.trimIndent()
    )

    fun `test struct with method method`() = doTestCompletion(
        """
        module main
        
        struct Book {}
        
        fn (b &Book) method() {}
        
        fn/*caret*/
        """.trimIndent(),
        """
        module main
        
        struct Book {}
        
        fn (b &Book) method() {}
        
        fn (b &Book) method1()  {
        	
        }
        """.trimIndent()
    )

    fun `test generic struct`() = doTestCompletion(
        """
        module main
        
        struct Book[T] {}
        
        fn/*caret*/
        """.trimIndent(),
        """
        module main
        
        struct Book[T] {}
        
        fn (b &Book[T]) method()  {
        	
        }
        """.trimIndent()
    )

    fun `test several structs`() = doTestCompletion(
        """
        module main
        
        struct Book {}
        struct Author {}
        
        fn/*caret*/
        """.trimIndent(),
        """
        module main
        
        struct Book {}
        struct Author {}
        
        fn 
        """.trimIndent()
    )
}
