package org.vlang.lang.resolve

class ResolveFieldsForSumTypeTest : ResolveTestBase() {
    fun `test simple`() {
        mainFile("a.v", """
            module main
            
            struct Foo {
                name string = 'Foo'
                age int
            }
            
            struct Boo {
                name string = 'Boo'
            }
            
            type Union = Boo | Foo
            
            fn main() {
                foo := Union(Foo{})
                println(foo./*caret*/name)
                println(foo./*caret*/age)
            }
        """.trimIndent())

        assertQualifiedReferencedTo("FIELD_DEFINITION main.Boo.name")
        assertQualifiedReferencedTo("FIELD_DEFINITION main.Foo.age")
    }

}
