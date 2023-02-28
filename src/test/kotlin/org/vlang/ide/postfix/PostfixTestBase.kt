package org.vlang.ide.postfix

import com.intellij.testFramework.fixtures.BasePlatformTestCase

abstract class PostfixTestBase : BasePlatformTestCase() {
    override fun getTestDataPath() = "src/test/resources/postfix"

    protected fun doTest(fixtureFile: String, textToType: String = "") {
        myFixture.configureByFile(fixtureFile)
        myFixture.type(textToType + "\t")
        val resFile = fixtureFile.replace(".v", ".after.v")
        myFixture.checkResultByFile(resFile, true)
    }
}
