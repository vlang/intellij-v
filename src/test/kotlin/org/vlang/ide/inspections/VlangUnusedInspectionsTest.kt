package org.vlang.ide.inspections

import org.vlang.ide.inspections.unused.VlangUnusedImportInspection
import org.vlang.ide.inspections.unused.VlangUnusedParameterInspection

class VlangUnusedInspectionsTest : InspectionTestBase("unused") {
    fun `test unused imports`() = doTest("unused_imports.v", VlangUnusedImportInspection())
    fun `test unused parameters`() = doTest("unused_params.v", VlangUnusedParameterInspection())
    fun `test unused receiver`() = doTest("unused_receiver.v", VlangUnusedParameterInspection())
}
