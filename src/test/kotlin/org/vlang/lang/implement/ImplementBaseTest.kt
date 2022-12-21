package org.vlang.lang.implement

import com.intellij.testFramework.TestModeFlags
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.intellij.lang.annotations.Language
import org.vlang.ide.refactoring.VlangImplementMethodsHandler
import org.vlang.lang.psi.VlangFile

abstract class ImplementBaseTest : BasePlatformTestCase() {
    fun doTest(@Language("vlang") before: String, @Language("vlang") after: String, interfaceName: String) {
        val text = before.replace(CARET, CARET_ORIGINAL)
        val file = myFixture.configureByText("main.v", text) as VlangFile

        val interfaceToImplement = file.getInterfaces().find { it.name == interfaceName }
        TestModeFlags.set(VlangImplementMethodsHandler.TESTING_INTERFACE_TO_IMPLEMENT, interfaceToImplement, testRootDisposable)
        VlangImplementMethodsHandler().invoke(project, myFixture.editor, myFixture.file)

        myFixture.checkResult(after)
    }

    companion object {
        const val CARET = "/*caret*/"
        const val CARET_ORIGINAL = "<caret>"
    }
}
