package org.vlang.lang.rename

import org.vlang.ide.refactoring.rename.VlangRenameReceiverProcessor.Companion.RENAME_ALL_RECEIVERS
import org.vlang.ide.refactoring.rename.VlangRenameReceiverProcessor.Companion.RENAME_ONLY_CURRENT_RECEIVER

class RenameReceiverTest : RenameBaseTest() {
    fun `test rename only current receivers`() = doReceiverTest(
        """
        struct Person {
        	name string
        }

        fn (/*caret*/this &Person) method() {
            println(this.name)
        }

        fn (this &Person) method1() {
        	println(this.name)
        }
        """.trimIndent(),
        """
        struct Person {
        	name string
        }

        fn (f &Person) method() {
            println(f.name)
        }

        fn (this &Person) method1() {
        	println(this.name)
        }
        """.trimIndent(),
        "f",
        RENAME_ONLY_CURRENT_RECEIVER,
    )

    fun `test rename all receivers`() = doReceiverTest(
        """
        struct Person {
        	name string
        }

        fn (/*caret*/this &Person) method() {
            println(this.name)
        }

        fn (this &Person) method1() {
        	println(this.name)
        }
        """.trimIndent(),
        """
        struct Person {
        	name string
        }

        fn (f &Person) method() {
            println(f.name)
        }

        fn (f &Person) method1() {
        	println(f.name)
        }
        """.trimIndent(),
        "f",
        RENAME_ALL_RECEIVERS,
    )
}
