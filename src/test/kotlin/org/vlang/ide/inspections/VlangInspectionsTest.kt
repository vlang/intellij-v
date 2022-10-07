package org.vlang.ide.inspections

class VlangInspectionsTest : InspectionTestBase() {
    fun `test redundant parentheses`() = doTest("redundant_parentheses.v", VlangRedundantParenthesesInspection())
    fun `test duplicates`()            = doTest("duplicateField/duplicates.v", VlangDuplicateFieldInspection())
}
