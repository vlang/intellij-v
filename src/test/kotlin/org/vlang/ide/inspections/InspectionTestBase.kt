package org.vlang.ide.inspections

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import java.io.File

abstract class InspectionTestBase : BasePlatformTestCase() {

    override fun getTestDataPath() = "src/test/resources/inspections"

    protected fun doTest(inspectionToEnable: VlangBaseInspection, vararg fixtureFiles: String) {
        myFixture.enableInspections(inspectionToEnable)
        myFixture.configureByFiles(*fixtureFiles)
        myFixture.testHighlighting(true, false, true)

        // Quick-fix test
        fixtureFiles.forEach { fixtureFile ->
            val qfFile = fixtureFile.replace(".v", ".after.v")
            if (File(myFixture.testDataPath + "/" + qfFile).exists()) {
                myFixture.getAllQuickFixes().forEach { myFixture.launchAction(it) }
                myFixture.checkResultByFile(qfFile)
            }
        }
    }
}
