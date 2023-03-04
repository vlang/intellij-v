package org.vlang.ide.inspections

import org.vlang.ide.inspections.general.VlangDuplicateFieldInspection
import org.vlang.ide.inspections.general.VlangUnresolvedLabelInspection
import org.vlang.ide.inspections.general.VlangVariableRedeclarationInspection
import org.vlang.ide.inspections.probableBugs.VlangControlFlowWithEmptyBodiesInspection
import org.vlang.ide.inspections.probableBugs.VlangVariableNotCapturedInspection
import org.vlang.ide.inspections.style.VlangRedundantParenthesesInspection
import org.vlang.ide.inspections.unsafe.VlangLabelOutsideUnsafeInspection
import org.vlang.ide.inspections.unsafe.VlangNilOutsideUnsafeInspection
import org.vlang.ide.inspections.validityIssues.VlangReassignImmutableSymbolInspection

class VlangInspectionsTest : InspectionTestBase() {
    fun `test redundant parentheses`()        = doTest("redundant_parentheses.v", VlangRedundantParenthesesInspection())
    fun `test duplicates`()                   = doTest("duplicateField/duplicates.v", VlangDuplicateFieldInspection())
    fun `test nil outside unsafe`()           = doTest("unsafe/outside/nil.v", VlangNilOutsideUnsafeInspection())
    fun `test goto outside unsafe`()          = doTest("unsafe/outside/goto.v", VlangLabelOutsideUnsafeInspection())
    fun `test unresolved label`()             = doTest("unresolved/label.v", VlangUnresolvedLabelInspection())
    fun `test assign to immutable`()          = doTest("validity/assign_to_immutable.v", VlangReassignImmutableSymbolInspection())
    fun `test empty control flow statement`() = doTest("probableBugs/ctrlflow_with_empty_body.v", VlangControlFlowWithEmptyBodiesInspection())
    fun `test variable redeclaration`()       = doTest("general/variable_redeclaration.v", VlangVariableRedeclarationInspection())
    fun `test not captured variable`()        = doTest("probableBugs/not_captured_variable.v", VlangVariableNotCapturedInspection())
    fun `test not captured variable deep nested`() = doTest("probableBugs/not_captured_variable_deep.v", VlangVariableNotCapturedInspection())
    fun `test not captured receiver`()        = doTest("probableBugs/not_captured_receiver.v", VlangVariableNotCapturedInspection())
    fun `test append to immutable`()          = doTest("validity/append_to_immutable.v", VlangReassignImmutableSymbolInspection())
    fun `test change value by index of immutable`() = doTest("validity/change_value_by_index_of_immutable.v", VlangReassignImmutableSymbolInspection())
    fun `test mutation immutably captured variable`() = doTest("validity/mutation_immutably_captured_variable.v", VlangReassignImmutableSymbolInspection())
    fun `test mutation immutable for variable`() = doTest("validity/mutation_immutable_for_variable.v", VlangReassignImmutableSymbolInspection())
}
