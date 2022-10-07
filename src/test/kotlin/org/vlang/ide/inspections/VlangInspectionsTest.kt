package org.vlang.ide.inspections

class VlangInspectionsTest : InspectionTestBase() {
    fun `test redundant parentheses`() = doTest(VlangRedundantParenthesesInspection(), "redundant_parentheses.v")
}
