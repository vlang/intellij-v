package io.vlang.lang.parser

import io.vlang.lang.VlangParserDefinition

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
    fun `test before block expr`() = doTest(true)
    fun `test struct init inside for loop without last statement`() = doTest(true)
    fun `test multiline operators`() = doTest(true)
}
