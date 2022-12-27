package org.vlang.ide.inspections

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import java.io.File

abstract class InspectionTestBase(private val baseFolder: String = "") : BasePlatformTestCase() {
    override fun getTestDataPath() = "src/test/resources/inspections" + File.separator + baseFolder

    protected fun doTestQuickFix(fixtureFile: String, inspectionToEnable: VlangBaseInspection, quickFix: String) {
        myFixture.enableInspections(inspectionToEnable)

        myFixture.configureByFile(fixtureFile)
        myFixture.testHighlighting(true, false, true)

        val qfFile = fixtureFile.replace(".v", ".after.v")
        if (File(myFixture.testDataPath + "/" + qfFile).exists()) {
            myFixture.getAllQuickFixes()
                .filter { it.familyName == quickFix }
                .forEach { myFixture.launchAction(it) }
            myFixture.checkResultByFile(qfFile)
        }
    }

    protected fun doTest(fixtureFile: String, vararg inspectionsToEnable: VlangBaseInspection) {
        inspectionsToEnable.forEach {
            myFixture.enableInspections(it)
        }
        myFixture.configureByFile(fixtureFile)
        myFixture.testHighlighting(true, false, true)

        val qfFile = fixtureFile.replace(".v", ".after.v")
        if (File(myFixture.testDataPath + "/" + qfFile).exists()) {
            myFixture.getAllQuickFixes().forEach { myFixture.launchAction(it) }
            myFixture.checkResultByFile(qfFile)
        }
    }
}
