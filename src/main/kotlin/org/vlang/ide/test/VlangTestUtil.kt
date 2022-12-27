package org.vlang.ide.test

import org.vlang.lang.psi.VlangFunctionDeclaration

object VlangTestUtil {
    fun isTestFunction(fn: VlangFunctionDeclaration): Boolean {
        return fn.name.startsWith("test_")
    }

    fun isMetaTestFunction(fn: VlangFunctionDeclaration): Boolean {
        return fn.name == "testsuite_begin" || fn.name == "testsuite_end"
    }
}
