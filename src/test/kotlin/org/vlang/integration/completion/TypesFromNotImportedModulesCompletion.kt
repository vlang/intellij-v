package org.vlang.integration.completion

import org.vlang.integration.IntegrationTestBase

class TypesFromNotImportedModulesCompletion : IntegrationTestBase() {
    fun `test struct init from different module`() = doTest {
        directory("mod") {
            file("mod.v", """
                module mod
                
                pub type Renderer = voidptr
            """.trimIndent())
        }

        file("a.v", """
            module foo
            
            fn main() {
                Rend/*caret 0*/
            }
        """.trimIndent())

        completion(caretIndex = 0) {
            includes("mod.Renderer")
        }

        finishCompletion()

        checkFile("a.v", """
            module foo
            
            import mod
            
            fn main() {
                mod.Rendererd/*caret 0*/
            }
        """.trimIndent())
    }
}