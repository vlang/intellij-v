package org.vlang.lang.implement

class ImplementTest : ImplementBaseTest() {
    fun `test single method`() = doTest(
        """
        interface Writer {
            write(buf []u8)
        }
        
        struct /*caret*/Name {}
        """.trimIndent(),
        """
        interface Writer {
            write(buf []u8)
        }
        
        struct Name {}
        
        fn (n &Name) write(buf []u8) {
        	panic('not implemented')
        }
        """.trimIndent(),
        "Writer",
    )

    fun `test single mutable method`() = doTest(
        """
        interface Writer {
        mut:
            write(buf []u8)
        }
        
        struct /*caret*/Name {}
        """.trimIndent(),
        """
        interface Writer {
        mut:
            write(buf []u8)
        }
        
        struct Name {}
        
        fn (mut n Name) write(buf []u8) {
        	panic('not implemented')
        }
        """.trimIndent(),
        "Writer",
    )

    fun `test single method that already implemented`() = doTest(
        """
        interface Writer {
            write(buf []u8)
        }
        
        struct /*caret*/Name {}
        
        fn (n &Name) write(buf []u8) {
        	// ...
        }
        """.trimIndent(),
        """
        interface Writer {
            write(buf []u8)
        }
        
        struct Name {}
        
        fn (n &Name) write(buf []u8) {
        	// ...
        }
        """.trimIndent(),
        "Writer",
    )

    fun `test single method with other struct method with non default receiver name`() = doTest(
        """
        interface Writer {
            write(buf []u8)
        }
        
        struct /*caret*/Name {}
        
        fn (name &Name) some() {
        	// ...
        }
        """.trimIndent(),
        """
        interface Writer {
            write(buf []u8)
        }
        
        struct Name {}
        
        fn (name &Name) write(buf []u8) {
        	panic('not implemented')
        }
        
        fn (name &Name) some() {
        	// ...
        }
        """.trimIndent(),
        "Writer",
    )

    fun `test several methods`() = doTest(
        """
        interface Writer {
            write(buf []u8)
            write2(buf []byte) !int
        }
        
        struct /*caret*/Name {}
        """.trimIndent(),
        """
        interface Writer {
            write(buf []u8)
            write2(buf []byte) !int
        }
        
        struct Name {}
        
        fn (n &Name) write(buf []u8) {
        	panic('not implemented')
        }
        
        fn (n &Name) write2(buf []byte) !int {
        	panic('not implemented')
        }
        """.trimIndent(),
        "Writer",
    )

    fun `test single mutable and single immutable method`() = doTest(
        """
        interface Writer {
            write(buf []u8)
        mut:
            write2(buf []byte) !int
        }
        
        struct /*caret*/Name {}
        """.trimIndent(),
        """
        interface Writer {
            write(buf []u8)
        mut:
            write2(buf []byte) !int
        }
        
        struct Name {}
        
        fn (n &Name) write(buf []u8) {
        	panic('not implemented')
        }
        
        fn (mut n Name) write2(buf []byte) !int {
        	panic('not implemented')
        }
        """.trimIndent(),
        "Writer",
    )

    fun `test several methods with one already implemented`() = doTest(
        """
        interface Writer {
            write(buf []u8)
            write2(buf []byte) !int
        }
        
        struct /*caret*/Name {}
        
        fn (n &Name) write2(buf []byte) !int {
        	// ...
        }
        """.trimIndent(),
        """
        interface Writer {
            write(buf []u8)
            write2(buf []byte) !int
        }
        
        struct Name {}
        
        fn (n &Name) write(buf []u8) {
        	panic('not implemented')
        }
        
        fn (n &Name) write2(buf []byte) !int {
        	// ...
        }
        """.trimIndent(),
        "Writer",
    )

    fun `test several methods with one not implemented because mutability`() = doTest(
        """
        interface Writer {
            write(buf []u8)
        mut:
            write2(buf []byte) !int
        }
        
        struct /*caret*/Name {}
        
        fn (n &Name) write2(buf []byte) !int {
        	// ...
        }
        """.trimIndent(),
        """
        interface Writer {
            write(buf []u8)
        mut:
            write2(buf []byte) !int
        }
        
        struct Name {}
        
        fn (n &Name) write(buf []u8) {
        	panic('not implemented')
        }
        
        fn (mut n Name) write2(buf []byte) !int {
        	panic('not implemented')
        }
        
        fn (n &Name) write2(buf []byte) !int {
        	// ...
        }
        """.trimIndent(),
        "Writer",
    )

    fun `test single field`() = doTest(
        """
        interface Writer {
            name string
        }
        
        struct /*caret*/Name {
        
        }
        """.trimIndent(),
        """
        interface Writer {
            name string
        }
        
        struct Name {
        	name string
        }
        """.trimIndent(),
        "Writer",
    )

    fun `test single field that already implemented`() = doTest(
        """
        interface Writer {
            name string
        }
        
        struct /*caret*/Name {
        	name string
        }
        """.trimIndent(),
        """
        interface Writer {
            name string
        }
        
        struct Name {
        	name string
        }
        """.trimIndent(),
        "Writer",
    )

    fun `test several fields`() = doTest(
        """
        interface Writer {
            name string
            age int
        }
        
        struct /*caret*/Name {
        
        }
        """.trimIndent(),
        """
        interface Writer {
            name string
            age int
        }
        
        struct Name {
        	name string
        	age int
        }
        """.trimIndent(),
        "Writer",
    )

    fun `test several fields with one already implemented`() = doTest(
        """
        interface Writer {
            name string
            age int
        }
        
        struct /*caret*/Name {
        	name string
        }
        """.trimIndent(),
        """
        interface Writer {
            name string
            age int
        }
        
        struct Name {
        	name string
        	age int
        }
        """.trimIndent(),
        "Writer",
    )

    fun `test several fields with one not implemented because mutability`() = doTest(
        """
        interface Writer {
        mut:
            name string
            age int
        }
        
        struct /*caret*/Name {
        	name string
        }
        """.trimIndent(),
        """
        interface Writer {
        mut:
            name string
            age int
        }
        
        struct Name {
        	name string
        	mut:
        	name string
        	age int
        }
        """.trimIndent(),
        "Writer",
    )


    fun `test single mutable and single immutable field`() = doTest(
        """
        interface Writer {
            name string
        mut:
            age int
        }
        
        struct /*caret*/Name {
        
        }
        """.trimIndent(),
        """
        interface Writer {
            name string
        mut:
            age int
        }
        
        struct Name {
        	name string
        	mut:
        	age int
        }
        """.trimIndent(),
        "Writer",
    )

    fun `test single field to struct with single immutable field`() = doTest(
        """
        interface Writer {
            name string
        }
        
        struct /*caret*/Name {
        	age int
        }
        """.trimIndent(),
        """
        interface Writer {
            name string
        }
        
        struct Name {
        	age int
        	name string
        }
        """.trimIndent(),
        "Writer",
    )

    fun `test immutable field interface and mutable field struct`() = doTest(
        """
        interface Writer {
            name string
        }
        
        struct /*caret*/Name {
        mut:
        	name string
        }
        """.trimIndent(),
        """
        interface Writer {
            name string
        }
        
        struct Name {
        mut:
        	name string
        }
        """.trimIndent(),
        "Writer",
    )

    fun `test immutable method interface and mutable method struct`() = doTest(
        """
        interface Writer {
            name() string
        }
        
        struct /*caret*/Name {
        }
        
        fn (mut n Name) name() string {
            return ''       
        }
        """.trimIndent(),
        """
        interface Writer {
            name() string
        }
        
        struct Name {
        }
        
        fn (mut n Name) name() string {
            return ''       
        }
        """.trimIndent(),
        "Writer",
    )
}
