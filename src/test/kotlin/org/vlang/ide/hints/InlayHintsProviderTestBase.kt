package org.vlang.ide.hints

import com.intellij.codeInsight.hints.InlayHintsProvider
import com.intellij.testFramework.utils.inlays.InlayHintsProviderTestCase

@Suppress("UnstableApiUsage")
abstract class InlayHintsProviderTestBase : InlayHintsProviderTestCase() {
    protected fun run(testFile: String, provider: InlayHintsProvider<*> = VlangInlayHintsProvider()) {
        val expectedFile = myFixture.configureByFile("$testFile.expected")
        val expectedText = expectedFile.text

        doTestProvider("a.v", expectedText, provider)
    }

    override fun getTestDataPath() = "src/test/resources/hints"
}
