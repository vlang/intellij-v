package org.vlang.lang.parser

import org.vlang.lang.VlangParserDefinition

class ParserNewTest : ParserTestBase("parser/new", "v", VlangParserDefinition()) {
    fun `test structs`() = doTest(true)
    fun `test attributes`() = doTest(true)
    fun `test lock`() = doTest(true)
    fun `test enums`() = doTest(true)
    fun `test anon structs`() = doTest(true)
    fun `test dump`() = doTest(true)
    fun `test typeof`() = doTest(true)
    fun `test sizeof`() = doTest(true)
    fun `test offsetof`() = doTest(true)
    fun `test isreftype`() = doTest(true)
}
