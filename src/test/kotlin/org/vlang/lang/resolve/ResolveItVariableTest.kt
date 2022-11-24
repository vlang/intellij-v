package org.vlang.lang.resolve

// TODO: disable for now, turn back
open class ResolveItVariableTest : ResolveTestBase() {
//    fun `test it in function call without param name for function type`() {
//        mainFile("a.v", """
//            module main
//
//            fn sort(cb fn (int) bool) {}
//
//            fn main() {
//                sort(<caret>it > 100)
//            }
//        """.trimIndent())
//
//        assertReferencedTo("PARAM_DEFINITION null")
//    }
//
//    fun `test it in function call with param name for function type`() {
//        mainFile("a.v", """
//            module main
//
//            fn sort(cb fn (it int) bool) {}
//
//            fn main() {
//                sort(<caret>it > 100)
//            }
//        """.trimIndent())
//
//        assertReferencedTo("PARAM_DEFINITION it")
//    }
//
//    fun `test it in function call with function type with none params`() {
//        mainFile("a.v", """
//            module main
//
//            fn sort(cb fn () bool) {}
//
//            fn main() {
//                sort(<caret>it > 100)
//            }
//        """.trimIndent())
//
//        assertUnresolved()
//    }

    fun `test it in function call with function type with more than one param`() {
        mainFile("a.v", """
            module main
            
            fn sort(cb fn (int, int) bool) {}
            
            fn main() {
                sort(<caret>it > 100)
            }
        """.trimIndent())

        assertUnresolved()
    }
}
