package org.vlang.lang.parser

import org.vlang.lang.VlangParserDefinition

class ParserTest : ParserTestBase("parser", "v", VlangParserDefinition()) {
    fun `test simple`() = doTest(true)
    fun `test help`() = doTest(true)
    fun `test imports`() = doTest(true)
    fun `test semantic analyzer`() = doTest(true)
    fun `test big integer`() = doTest(true)
    fun `test path tracing`() = doTest(true)
    fun `test quick sort`() = doTest(true)
    fun `test string interpolation`() = doTest(true)
    fun `test match`() = doTest(true)
    fun `test literal value after match`() = doTest(true)
    fun `test compile time for`() = doTest(true)
    fun `test is as expression`() = doTest(true)
    fun `test function literal`() = doTest(true)
    fun `test select`() = doTest(true)
    fun `test dot expression`() = doTest(true)
    fun `test globals`() = doTest(true)
    fun `test nil keyword`() = doTest(true)
    fun `test expr inside par`() = doTest(true)
    fun `test decode`() = doTest(true)
    fun `test chars`() = doTest(true)
    fun `test top level expressions`() = doTest(true)
    fun `test sql`() = doTest(true)
}
