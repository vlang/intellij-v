package org.vlang.ide.annotators

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import java.io.File

abstract class AnnotatorTestBase(private val baseDir: String = "") : BasePlatformTestCase() {
    override fun getTestDataPath() = "src/test/resources/annotators" + File.separator + baseDir

    protected fun doTest(fixtureFile: String) {
        myFixture.configureByFile(fixtureFile)
        myFixture.testHighlighting(true, false, true)
    }
}
