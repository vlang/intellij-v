package org.vlang.lang.psi.impl

import com.intellij.openapi.util.Key
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiPolyVariantReferenceBase
import com.intellij.util.containers.ContainerUtil
import org.vlang.lang.psi.VlangImportSpec
import org.vlang.lang.psi.VlangReferenceExpressionBase

abstract class VlangReferenceBase<T : VlangReferenceExpressionBase>(element: T, range: TextRange?) :
    PsiPolyVariantReferenceBase<T>(element, range) {

    companion object {
        val IMPORT_USERS = Key.create<List<PsiElement>>("IMPORT_USERS")
        val ACTUAL_NAME = Key.create<String>("ACTUAL_NAME")
        val SEARCH_NAME = Key.create<String>("SEARCH_NAME")

        @JvmStatic
        protected fun getPath(file: PsiFile?): String? {
            if (file == null) return null
            val virtualFile = file.originalFile.virtualFile
            return virtualFile?.path
        }

        private fun putIfAbsent(importSpec: VlangImportSpec, usage: PsiElement) {
            synchronized(importSpec) {
                val newUsages = mutableListOf(usage)
                newUsages.addAll(IMPORT_USERS[importSpec, ContainerUtil.emptyList()])
                importSpec.putUserData(IMPORT_USERS, newUsages)
            }
        }
    }
}
