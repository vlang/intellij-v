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
                color = ./*caret*/green
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
                ./*caret*/green {}
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
                color Colors = ./*caret*/green
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
                color + ./*caret*/green
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
                take_color(./*caret*/green)
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
                    color: ./*caret*/green
                }
            }
        """.trimIndent())

        assertReferencedTo("ENUM_FIELD_DEFINITION green")
    }

    fun `test in array in binary expr`() {
        mainFile("a.v", """
            module main

            enum Colors {
                red
                green
            }

            fn main() {
                mut color := Colors.red
                if color in [./*caret*/red, ./*caret*/green] {

                }
            }
        """.trimIndent())

        assertReferencedTo("ENUM_FIELD_DEFINITION red")
        assertReferencedTo("ENUM_FIELD_DEFINITION green")
    }

    fun `test in array in binary expr 2`() {
        mainFile("a.v", """
            module main

            enum Colors {
                red
                green
            }

            fn main() {
                mut colors := [Colors.red]
                assert colors == [./*caret*/red, ./*caret*/green]
            }
        """.trimIndent())

        assertReferencedTo("ENUM_FIELD_DEFINITION red")
        assertReferencedTo("ENUM_FIELD_DEFINITION green")
    }

    fun `test in array`() {
        mainFile("a.v", """
            module main

            enum Colors {
                red
                green
            }

            fn main() {
                mut colors := [Colors./*caret*/red, ./*caret*/green]
            }
        """.trimIndent())

        assertReferencedTo("ENUM_FIELD_DEFINITION red")
        assertReferencedTo("ENUM_FIELD_DEFINITION green")
    }

    fun `test return`() {
        mainFile("a.v", """
            module main
            
            enum Colors {
                red
                green
            }
            
            fn foo() Colors {
                return ./*caret*/green
            }
        """.trimIndent())

        assertReferencedTo("ENUM_FIELD_DEFINITION green")
    }

    fun `test inside has and all methods`() {
        mainFile("a.v", """
            module main
            
            [flag]
            enum Permissions {
                read  // = 0b0001
                write // = 0b0010
                other // = 0b0100
            }
            
            fn main() {
                p := Permissions.read
                assert p.has(./*caret*/read | ./*caret*/other)
            
                p1 := Permissions.read | .write
                assert p1.has(./*caret*/write)
            }
        """.trimIndent())

        assertReferencedTo("ENUM_FIELD_DEFINITION read")
        assertReferencedTo("ENUM_FIELD_DEFINITION other")
        assertReferencedTo("ENUM_FIELD_DEFINITION write")
    }

    fun `test bit op`() {
        mainFile("a.v", """
            module main

            enum Colors {
                red
                green
            }

            fn main() {
                mut colors := Colors.red
                
                colors = ./*caret*/red | ./*caret*/green
            }
        """.trimIndent())

        assertReferencedTo("ENUM_FIELD_DEFINITION red")
        assertReferencedTo("ENUM_FIELD_DEFINITION green")
    }

    fun `test with unpacking struct init`() {
        mainFile("a.v", """
            module main

            enum Colors {
                red
                green
            }
            
            struct Colorful {
                color Colors
                name string
            }
            
            other := Colorful{}
            
            c := Colorful{
                ...other
                color: ./*caret*/green
            }

        """.trimIndent())

        assertReferencedTo("ENUM_FIELD_DEFINITION green")
    }
}
