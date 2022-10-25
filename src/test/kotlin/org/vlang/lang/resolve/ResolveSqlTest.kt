package org.vlang.lang.resolve

open class ResolveSqlTest : ResolveTestBase() {
    fun `test sql left part of expression`() {
        mainFile("a.v", """
            module testing

            [table: 'code_storage']
            struct CodeStorage {
                id   int    [primary; sql: serial]
                code string [nonull]
                hash string [nonull]
            }
            
            fn main() {
                hash := ""
            
                found := sql db {
                    select from CodeStorage where <caret>hash == <caret>hash
                }
            
                count := sql db {
                    select count from CodeStorage where <caret>hash == <caret>hash
                }
            }
        """.trimIndent())

        assertReferencedTo("FIELD_DEFINITION hash")
        assertReferencedTo("VAR_DEFINITION hash")
        assertReferencedTo("FIELD_DEFINITION hash")
        assertReferencedTo("VAR_DEFINITION hash")
    }

    fun `test sql order by`() {
        mainFile("a.v", """
            module testing

            [table: 'code_storage']
            struct CodeStorage {
                id   int    [primary; sql: serial]
                code string [nonull]
                hash string [nonull]
            }
            
            fn main() {
                hash := ""
            
                found := sql db {
                    select from CodeStorage where hash == hash order by <caret>hash
                }
            
                count := sql db {
                    select count from CodeStorage where hash == hash order by <caret>hash
                }
            }
        """.trimIndent())

        assertReferencedTo("FIELD_DEFINITION hash")
        assertReferencedTo("FIELD_DEFINITION hash")
    }

}
