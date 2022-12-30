package org.vlang.ide.inspections

import org.vlang.ide.inspections.validityIssues.VlangCallMutableMethodByImmutableValueInspection

class VlangValiditylInspectionTest : InspectionTestBase("validity") {
    fun `test call mutable method by immutable value`() =
        doTest("call_mutable_method_by_immutable_value.v", VlangCallMutableMethodByImmutableValueInspection())
}
