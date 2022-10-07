package org.vlang.ide.inspections

import org.vlang.ide.inspections.general.VlangDuplicateFieldInspection
import org.vlang.ide.inspections.general.VlangUnresolvedLabelInspection
import org.vlang.ide.inspections.style.VlangRedundantParenthesesInspection
import org.vlang.ide.inspections.unsafe.VlangLabelOutsideUnsafeInspection
import org.vlang.ide.inspections.unsafe.VlangNilOutsideUnsafeInspection

class VlangInspectionsTest : InspectionTestBase() {
    fun `test redundant parentheses`() = doTest("redundant_parentheses.v", VlangRedundantParenthesesInspection())
    fun `test duplicates`()            = doTest("duplicateField/duplicates.v", VlangDuplicateFieldInspection())
    fun `test nil outside unsafe`()    = doTest("unsafe/outside/nil.v", VlangNilOutsideUnsafeInspection())
    fun `test goto outside unsafe`()   = doTest("unsafe/outside/goto.v", VlangLabelOutsideUnsafeInspection())
    fun `test unresolved label`()      = doTest("unresolved/label.v", VlangUnresolvedLabelInspection())
}
