package org.vlang.lang.resolve

open class ResolveVariablesInScriptsTest : ResolveTestBase() {
    fun `test simple variable`() {
        mainFile("a.v", """
            name := 'V'
            println('Hello, ${"\${"}/*caret*/name}!')
        """.trimIndent())

        assertReferencedTo("VAR_DEFINITION name")
    }

    fun `test inside if`() {
        mainFile("a.v", """
            name := 'V'
            if true {
                other := 'V'
                /*caret*/name
                /*caret*/other
            }
            /*caret*/name
            /*caret*/other
        """.trimIndent())

        assertReferencedTo("VAR_DEFINITION name")
        assertReferencedTo("VAR_DEFINITION other")
        assertReferencedTo("VAR_DEFINITION name")
        assertUnresolved()
    }

    fun `test inside for`() {
        mainFile("a.v", """
            for i := 0; i < 10; i++ {
                /*caret*/i
            }
            /*caret*/i
        """.trimIndent())

        assertReferencedTo("VAR_DEFINITION i")
        assertUnresolved()
    }
}
