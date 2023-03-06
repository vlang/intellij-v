package org.vlang.integration.completion

import org.vlang.integration.IntegrationTestBase

class GlobalVariableCompletion : IntegrationTestBase() {
    fun `test struct init from different module`() = doTest {
        directory("mod") {
            file("mod.v", """
                module mod
                
                __global global_name = "foo"
            """.trimIndent())
        }

        file("a.v", """
            module foo
            
            fn main() {
                glo/*caret 0*/
            }
        """.trimIndent())

        completion(caretIndex = 0) {
            includes("global_name")
        }

        finishCompletion()

        checkFile("a.v", """
            module foo
            
            import mod
            
            fn main() {
                global_nameo/*caret 0*/
            }
        """.trimIndent())
    }
}