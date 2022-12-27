package org.vlang.ide.inspections

import org.vlang.ide.inspections.unused.VlangUnusedVariableInspection

class VlangUnusedVariableInspectionsTest : InspectionTestBase("unused/variable") {
    fun `test simple`() = doTestQuickFix("simple.v", VlangUnusedVariableInspection(), "Rename to _")
    fun `test simple with delete`() = doTestQuickFix("simple_delete.v", VlangUnusedVariableInspection(), "Delete variable")
    fun `test template`() = doTest("template.v", VlangUnusedVariableInspection())
    fun `test multi assign`() = doTest("multi_assign.v", VlangUnusedVariableInspection())
    fun `test for loop`() = doTest("for_loop.v", VlangUnusedVariableInspection())
}
