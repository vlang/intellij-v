package org.vlang.lang.lexer

import com.intellij.ide.plugins.PluginUtil
import com.intellij.ide.plugins.PluginUtilImpl
import com.intellij.mock.MockApplication
import com.intellij.openapi.util.registry.Registry
import com.intellij.testFramework.LexerTestCase

abstract class LexerTestBase : LexerTestCase() {
    override fun getDirPath() = "src/test/resources/lexer"

    override fun createLexer() = VlangLexer()

    @Suppress("UnstableApiUsage")
    override fun setUp() {
        super.setUp()
        val app = MockApplication.setUp(testRootDisposable)
        app.registerService(PluginUtil::class.java, PluginUtilImpl())
        Registry.markAsLoaded()
    }

    fun match(code: String, tokens: String) {
        doTest(code, tokens)
    }
}
