package org.vlang.ide.hints

import org.vlang.ide.hints.VlangEnumValuesInlayHintsProvider.Settings

class InlayHintsProviderTest : InlayHintsProviderTestBase() {
    fun `test no hint for _ variable`() = run("underscore_var.v", VlangInlayHintsProvider())
    fun `test ranges`() = run("ranges.v", VlangInlayHintsProvider())
    fun `test chain method`() = run("chain_method.v", VlangChainMethodInlayHintsProvider())
    fun `test enum fields`() = run("enum_fields.v", VlangEnumValuesInlayHintsProvider())
    fun `test flag enum fields with only decimal`() =
        run("enum_fields_2.v", VlangEnumValuesInlayHintsProvider(), Settings(showOnlyDecimal = true))
    fun `test err variable`() = run("err_variable.v", VlangInlayHintsProvider())
    fun `test obvious cases`() = run("obvious_cases.v", VlangInlayHintsProvider())
}
