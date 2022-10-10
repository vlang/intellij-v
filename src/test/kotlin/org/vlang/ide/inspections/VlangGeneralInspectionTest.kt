package org.vlang.ide.inspections

import org.vlang.ide.inspections.general.VlangAmbiguousImportInspection

class VlangGeneralInspectionTest : InspectionTestBase("general") {
    fun `test ambiguous imports`() = doTest("ambiguous_imports.v", VlangAmbiguousImportInspection())
}
