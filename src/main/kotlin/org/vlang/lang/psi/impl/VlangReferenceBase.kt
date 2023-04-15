package org.vlang.lang.psi.impl

import com.intellij.openapi.util.Key
import com.intellij.openapi.util.KeyWithDefaultValue
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiPolyVariantReferenceBase
import org.vlang.lang.psi.VlangReferenceExpressionBase

abstract class VlangReferenceBase<T : VlangReferenceExpressionBase>(element: T, range: TextRange?) :
    PsiPolyVariantReferenceBase<T>(element, range) {

    protected val project = element.project

    companion object {
        val ACTUAL_NAME = Key.create<String>("ACTUAL_NAME")
        val SEARCH_NAME = Key.create<String>("SEARCH_NAME")
        val MODULE_NAME = Key.create<String>("MODULE_NAME")
        val NEED_QUALIFIER_NAME = Key.create<Boolean>("NEED_QUALIFIER_NAME")
        val LOCAL_RESOLVE = KeyWithDefaultValue.create("LOCAL_RESOLVE", false)
        val NOT_PROCESS_METHODS = KeyWithDefaultValue.create("NOT_PROCESS_METHODS", false)
        val NOT_PROCESS_EMBEDDED_DEFINITION = KeyWithDefaultValue.create("NOT_PROCESS_EMBEDDED_DEFINITION", false)
        val PROCESS_PRIVATE_MEMBERS = KeyWithDefaultValue.create("PROCESS_PRIVATE_MEMBERS", false)

        @JvmStatic
        protected fun getPath(file: PsiFile?): String? {
            if (file == null) return null
            val virtualFile = file.originalFile.virtualFile
            return virtualFile?.path
        }
    }
}
