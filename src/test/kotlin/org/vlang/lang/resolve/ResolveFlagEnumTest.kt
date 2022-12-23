package org.vlang.lang.resolve

class ResolveFlagEnumTest : ResolveTestBase() {
    fun `test has and all methods`() {
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
                assert p./*caret*/has(.read | .other)
            
                p1 := Permissions.read | .write
                assert p1./*caret*/has(.write)
                assert p1./*caret*/all(.read | .write)
            }
        """.trimIndent())

        assertQualifiedReferencedTo("METHOD_DECLARATION stubs.FlagEnum.has")
        assertQualifiedReferencedTo("METHOD_DECLARATION stubs.FlagEnum.has")
        assertQualifiedReferencedTo("METHOD_DECLARATION stubs.FlagEnum.all")
    }
}
