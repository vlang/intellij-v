package io.vlang.ide.inspections

import io.vlang.ide.inspections.validityIssues.VlangCallMutableMethodByImmutableValueInspection

class VlangValiditylInspectionTest : InspectionTestBase("validity") {
    fun `test call mutable method by immutable value`() =
        doTest("call_mutable_method_by_immutable_value.v", VlangCallMutableMethodByImmutableValueInspection())
}
