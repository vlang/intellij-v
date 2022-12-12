package org.vlang.lang.rename

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.intellij.lang.annotations.Language

abstract class RenameBaseTest : BasePlatformTestCase() {
    fun doTest(@Language("vlang") before: String, @Language("vlang") after: String, renameTo: String) {
        val text = before.replace(CARET, CARET_ORIGINAL)
        myFixture.configureByText("main.v", text)
        myFixture.renameElementAtCaret(renameTo)
        myFixture.checkResult(after)
    }

    companion object {
        const val CARET = "/*caret*/"
        const val CARET_ORIGINAL = "<caret>"
    }
}
