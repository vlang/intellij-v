package org.vlang.ide.inspections

import org.vlang.ide.inspections.unused.VlangUnusedVariableInspection
import org.vlang.ide.inspections.unused.VlangUnusedVariableInspection.Companion.RENAME_TO_UNDERSCORE_FIX

class VlangUnusedVariableInspectionsTest : InspectionTestBase("unused/variable") {
    fun `test simple`() = doTestQuickFix("simple.v", VlangUnusedVariableInspection(), RENAME_TO_UNDERSCORE_FIX)
    fun `test template`() = doTest("template.v", VlangUnusedVariableInspection())
    fun `test multi assign`() = doTest("multi_assign.v", VlangUnusedVariableInspection())
    fun `test for loop`() = doTest("for_loop.v", VlangUnusedVariableInspection())
}
