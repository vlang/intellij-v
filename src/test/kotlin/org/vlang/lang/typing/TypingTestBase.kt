package org.vlang.lang.typing

import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.testFramework.fixtures.BasePlatformTestCase

abstract class TypingTestBase : BasePlatformTestCase() {
    override fun getTestDataPath() = "src/test/resources/typing"

    fun type(text: String, stringToType: String, after: String) {
        myFixture.configureByText("a.v", text)
        myFixture.type(stringToType)
        myFixture.checkResult(after)
    }

    fun delete(text: String, after: String) {
        myFixture.configureByText("a.v", text)
        myFixture.performEditorAction(IdeActions.ACTION_EDITOR_BACKSPACE)
        myFixture.checkResult(after)
    }
}
