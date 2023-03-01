package org.vlang.ide.annotators

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import java.io.File

abstract class AnnotatorTestBase(private val baseDir: String = "") : BasePlatformTestCase() {
    override fun getTestDataPath() = "src/test/resources/annotators" + File.separator + baseDir

    protected fun doTestQuickFix(fixtureFile: String, quickFix: String? = null) {
        myFixture.configureByFile(fixtureFile)
        myFixture.testHighlighting(true, false, true)

        val qfFile = fixtureFile.replace(".v", ".after.v")
        if (File(myFixture.testDataPath + "/" + qfFile).exists()) {
            myFixture.getAllQuickFixes()
                .filter { quickFix == null || it.familyName == quickFix }
                .forEach { myFixture.launchAction(it) }
            myFixture.checkResultByFile(qfFile)
        }
    }

    protected fun doTest(fixtureFile: String) {
        myFixture.configureByFile(fixtureFile)
        myFixture.testHighlighting(true, false, true)
    }
}
