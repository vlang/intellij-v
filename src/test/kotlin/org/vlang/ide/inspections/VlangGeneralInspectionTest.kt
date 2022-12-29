package org.vlang.ide.inspections

import org.vlang.ide.inspections.general.VlangAmbiguousImportInspection
import org.vlang.ide.inspections.general.VlangNonExhaustiveMatchInspection

class VlangGeneralInspectionTest : InspectionTestBase("general") {
    fun `test ambiguous imports`() = doTest("ambiguous_imports.v", VlangAmbiguousImportInspection())

    fun `test non exhaustive match`() =
        doTestQuickFix("non_exhaustive_match.v", VlangNonExhaustiveMatchInspection(), "Add missing branches")

    fun `test non exhaustive match add else quick fix`() =
        doTestQuickFix("non_exhaustive_match_add_else.v", VlangNonExhaustiveMatchInspection(), "Add else branch")
}
