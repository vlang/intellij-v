package org.vlang.ide.inspections

import org.vlang.ide.inspections.unused.VlangUnusedImportInspection

class VlangUnusedInspectionsTest : InspectionTestBase("unused") {
    fun `test unused imports`() = doTest("unused_imports.v", VlangUnusedImportInspection())
}
