package org.vlang.lang.rename

import com.intellij.testFramework.fixtures.BasePlatformTestCase

abstract class RenameBaseTest : BasePlatformTestCase() {
    fun doTest(before: String, after: String, renameTo: String) {
        myFixture.configureByText("main.v", before)
        myFixture.renameElementAtCaret(renameTo)
        myFixture.checkResult(after)
    }
}
