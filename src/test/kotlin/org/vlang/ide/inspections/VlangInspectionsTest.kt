package org.vlang.ide.inspections

import org.vlang.ide.inspections.general.VlangDuplicateFieldInspection
import org.vlang.ide.inspections.general.VlangUnresolvedLabelInspection
import org.vlang.ide.inspections.style.VlangRedundantParenthesesInspection
import org.vlang.ide.inspections.unsafe.VlangLabelOutsideUnsafeInspection
import org.vlang.ide.inspections.unsafe.VlangNilOutsideUnsafeInspection
import org.vlang.ide.inspections.validityIssues.VlangReassignImmutableSymbolInspection

class VlangInspectionsTest : InspectionTestBase() {
    fun `test redundant parentheses`() = doTest("redundant_parentheses.v", VlangRedundantParenthesesInspection())
    fun `test duplicates`()            = doTest("duplicateField/duplicates.v", VlangDuplicateFieldInspection())
    fun `test nil outside unsafe`()    = doTest("unsafe/outside/nil.v", VlangNilOutsideUnsafeInspection())
    fun `test goto outside unsafe`()   = doTest("unsafe/outside/goto.v", VlangLabelOutsideUnsafeInspection())
    fun `test unresolved label`()      = doTest("unresolved/label.v", VlangUnresolvedLabelInspection())
    fun `test assign to immutable`()   = doTest("validity/assign_to_immutable.v", VlangReassignImmutableSymbolInspection())
}
