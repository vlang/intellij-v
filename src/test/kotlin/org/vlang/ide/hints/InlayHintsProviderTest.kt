package org.vlang.ide.hints

class InlayHintsProviderTest : InlayHintsProviderTestBase() {
    fun `test no hint for _ variable`() = run("underscore_var.v")
    fun `test ranges`() = run("ranges.v")
    fun `test chain method`() = run("chain_method.v", VlangChainMethodInlayHintsProvider())
}
