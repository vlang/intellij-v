package org.vlang.lang.lexer

class LexerTest : LexerTestBase() {
    fun `test simple main function`() = match(
        """
        fn main() {}
        """.trimIndent(),
        """
        VlangTokenType.fn ('fn')
        VlangTokenType.VLANG_WHITESPACE (' ')
        VlangTokenType.identifier ('main')
        VlangTokenType.( ('(')
        VlangTokenType.) (')')
        VlangTokenType.VLANG_WHITESPACE (' ')
        VlangTokenType.{ ('{')
        VlangTokenType.} ('}')
        """.trimIndent()
    )

    fun `test hash comment`() = match(
        """
        # comment
        """.trimIndent(),
        """
        VlangTokenType.VLANG_HASH_COMMENT ('# comment')
        """.trimIndent()
    )

    fun `test hash lbrack`() = match(
        """
        #[1..-1]
        """.trimIndent(),
        """
        VlangTokenType.#[ ('#[')
        VlangTokenType.int ('1')
        VlangTokenType... ('..')
        VlangTokenType.- ('-')
        VlangTokenType.int ('1')
        VlangTokenType.] (']')
        """.trimIndent()
    )

    fun `test attribute`() = match(
        """
        [inline]
        """.trimIndent(),
        """
        VlangTokenType.[ ('[')
        VlangTokenType.identifier ('inline')
        VlangTokenType.] (']')
        """.trimIndent()
    )

    fun `test special idents`() = match(
        """
        fn JS.something() {}
        fn C.foo.bar() {}
        fn JS.foo1.bar2() {}
        """.trimIndent(),
        """
       VlangTokenType.fn ('fn')
       VlangTokenType.VLANG_WHITESPACE (' ')
       VlangTokenType.identifier ('JS.something')
       VlangTokenType.( ('(')
       VlangTokenType.) (')')
       VlangTokenType.VLANG_WHITESPACE (' ')
       VlangTokenType.{ ('{')
       VlangTokenType.} ('}')
       VlangTokenType.<NL> ('')
       VlangTokenType.VLANG_WS_NEW_LINES ('\n')
       VlangTokenType.fn ('fn')
       VlangTokenType.VLANG_WHITESPACE (' ')
       VlangTokenType.identifier ('C.foo.bar')
       VlangTokenType.( ('(')
       VlangTokenType.) (')')
       VlangTokenType.VLANG_WHITESPACE (' ')
       VlangTokenType.{ ('{')
       VlangTokenType.} ('}')
       VlangTokenType.<NL> ('')
       VlangTokenType.VLANG_WS_NEW_LINES ('\n')
       VlangTokenType.fn ('fn')
       VlangTokenType.VLANG_WHITESPACE (' ')
       VlangTokenType.identifier ('JS.foo1.bar2')
       VlangTokenType.( ('(')
       VlangTokenType.) (')')
       VlangTokenType.VLANG_WHITESPACE (' ')
       VlangTokenType.{ ('{')
       VlangTokenType.} ('}')
        """.trimIndent()
    )

    fun `test not in, not is`() = match(
        """
        a !in b
        a !is b
        
        a ! in b
        a ! is b
        
        !inside
        !is_number
        """.trimIndent(),
        """
        VlangTokenType.identifier ('a')
        VlangTokenType.VLANG_WHITESPACE (' ')
        VlangTokenType.!in ('!in')
        VlangTokenType.VLANG_WHITESPACE (' ')
        VlangTokenType.identifier ('b')
        VlangTokenType.<NL> ('')
        VlangTokenType.VLANG_WS_NEW_LINES ('\n')
        VlangTokenType.identifier ('a')
        VlangTokenType.VLANG_WHITESPACE (' ')
        VlangTokenType.!is ('!is')
        VlangTokenType.VLANG_WHITESPACE (' ')
        VlangTokenType.identifier ('b')
        VlangTokenType.<NL> ('')
        VlangTokenType.VLANG_WS_NEW_LINES ('\n\n')
        VlangTokenType.identifier ('a')
        VlangTokenType.VLANG_WHITESPACE (' ')
        VlangTokenType.! ('!')
        VlangTokenType.VLANG_WHITESPACE (' ')
        VlangTokenType.in ('in')
        VlangTokenType.VLANG_WHITESPACE (' ')
        VlangTokenType.identifier ('b')
        VlangTokenType.<NL> ('')
        VlangTokenType.VLANG_WS_NEW_LINES ('\n')
        VlangTokenType.identifier ('a')
        VlangTokenType.VLANG_WHITESPACE (' ')
        VlangTokenType.! ('!')
        VlangTokenType.VLANG_WHITESPACE (' ')
        VlangTokenType.is ('is')
        VlangTokenType.VLANG_WHITESPACE (' ')
        VlangTokenType.identifier ('b')
        VlangTokenType.<NL> ('')
        VlangTokenType.VLANG_WS_NEW_LINES ('\n\n')
        VlangTokenType.! ('!')
        VlangTokenType.identifier ('inside')
        VlangTokenType.<NL> ('')
        VlangTokenType.VLANG_WS_NEW_LINES ('\n')
        VlangTokenType.! ('!')
        VlangTokenType.identifier ('is_number')
        """.trimIndent()
    )
}

