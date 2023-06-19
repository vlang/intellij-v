package org.vlang.ide.inspections

import org.vlang.ide.inspections.general.*

class VlangGeneralInspectionTest : InspectionTestBase("general") {
    fun `test ambiguous imports`() = doTest("ambiguous_imports.v", VlangAmbiguousImportInspection())

    fun `test cycle import`() = doTest("circular/first/utils.v", VlangCircularImportInspection())

    fun `test non exhaustive match`() =
        doTestQuickFix("non_exhaustive_match.v", VlangNonExhaustiveMatchInspection(), "Add missing branches")

    fun `test non exhaustive match add else quick fix`() =
        doTestQuickFix("non_exhaustive_match_add_else.v", VlangNonExhaustiveMatchInspection(), "Add else branch")

    fun `test call arguments count mismatch`() = doTest("call_arguments_count_mismatch.v", VlangCallArgumentsCountMismatchInspection())
    fun `test raw result type`() = doTest("raw_result_type.v", VlangRawOptionOrResultTypeUsedInspection())
}
