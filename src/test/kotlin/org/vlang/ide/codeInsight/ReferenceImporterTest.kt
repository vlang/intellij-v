package org.vlang.ide.codeInsight

class ReferenceImporterTest : ReferenceImporterTestBase() {
    fun `test simple`() = doTest(
        """
            module main

            fn main() {
                os.read_file('')
            }
        """.trimIndent(),
        """
            module main
            
            import os
            
            fn main() {
                os.read_file('')
            }
        """.trimIndent()
    )

    fun `test with private member`() = doTest(
        """
            module main

            fn main() {
                os.f_ok // private member
            }
        """.trimIndent(),
        """
            module main
            
            import os
            
            fn main() {
                os.f_ok // private member
            }
        """.trimIndent()
    )

    fun `test several nested`() = doTest(
        """
            module main

            fn main() {
                nested.nested_fn()
                sub.Sub{}
            }
        """.trimIndent(),
        """
            module main
            
            import nested
            import nested.sub
            
            fn main() {
                nested.nested_fn()
                sub.Sub{}
            }
        """.trimIndent()
    )

    fun `test without module name`() = doTest(
        """
            struct Foo {}
            
            fn main() {
                os.read_file('')
            }
        """.trimIndent(),
        """
            import os
            
            struct Foo {}
            
            fn main() {
                os.read_file('')
            }
        """.trimIndent()
    )
}
