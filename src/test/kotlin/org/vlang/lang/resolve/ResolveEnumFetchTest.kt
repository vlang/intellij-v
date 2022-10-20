package org.vlang.lang.resolve

open class ResolveEnumFetchTest : ResolveTestBase() {
    fun `test in assign`() {
        mainFile("a.v", """
            module main
            
            enum Colors {
                red
                green
            }
            
            fn main() {
                mut color := Colors.red
                color = .<caret>green
            }
        """.trimIndent())

        assertReferencedTo("ENUM_FIELD_DEFINITION green")
    }

    fun `test in match`() {
        mainFile("a.v", """
            module main
            
            enum Colors {
                red
                green
            }
            
            fn main() {
                mut color := Colors.red
                match color {
                .<caret>green {}
                }
            }
        """.trimIndent())

        assertReferencedTo("ENUM_FIELD_DEFINITION green")
    }

    fun `test in default value`() {
        mainFile("a.v", """
            module main
            
            enum Colors {
                red
                green
            }
            
            struct WithColor {
                color Colors = .<caret>green
            }
        """.trimIndent())

        assertReferencedTo("ENUM_FIELD_DEFINITION green")
    }

    fun `test in binary expression`() {
        mainFile("a.v", """
            module main
            
            enum Colors {
                red
                green
            }
            
            fn main() {
                mut color := Colors.red
                color + .<caret>green
            }
        """.trimIndent())

        assertReferencedTo("ENUM_FIELD_DEFINITION green")
    }

    fun `test in call expression`() {
        mainFile("a.v", """
            module main
            
            enum Colors {
                red
                green
            }
            
            fn take_color(color Colors) {}
            
            fn main() {
                take_color(.<caret>green)
            }
        """.trimIndent())

        assertReferencedTo("ENUM_FIELD_DEFINITION green")
    }

    fun `test in literal expression`() {
        mainFile("a.v", """
            module main
            
            enum Colors {
                red
                green
            }
            
            struct WithColor {
                color Colors
            }
            
            fn main() {
                WithColor{
                    color: .<caret>green
                }
            }
        """.trimIndent())

        assertReferencedTo("ENUM_FIELD_DEFINITION green")
    }
}
