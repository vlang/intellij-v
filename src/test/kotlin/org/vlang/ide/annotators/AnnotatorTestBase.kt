package org.vlang.ide.annotators

import com.intellij.testFramework.fixtures.BasePlatformTestCase

abstract class AnnotatorTestBase : BasePlatformTestCase() {
    override fun getTestDataPath() = "src/test/resources/annotators"

    protected fun doTest(fixtureFile: String) {
        myFixture.configureByFile(fixtureFile)
        myFixture.testHighlighting(true, false, true)
    }
}
