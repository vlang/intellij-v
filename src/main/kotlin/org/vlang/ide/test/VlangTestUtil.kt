package org.vlang.ide.test

import org.vlang.lang.psi.VlangFunctionDeclaration

object VlangTestUtil {
    fun isTestFunction(fn: VlangFunctionDeclaration): Boolean {
        return fn.name.startsWith("test_")
    }
}
