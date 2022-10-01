package org.vlang.ide.templates

import com.intellij.openapi.project.guessProjectDir
import com.intellij.psi.PsiDirectory
import java.util.*

object VlangTemplateUtil {
    private const val DEFAULT_MODULE_NAME = "main"
    private const val ATTRIBUTE_MODULE_NAME = "V_MODULE_NAME"
    private const val DEFAULT_SOURCE_DIR = "src"

    fun setModuleNameAttribute(directory: PsiDirectory, props: Properties) {
        props.setProperty(ATTRIBUTE_MODULE_NAME, getModuleName(directory))
    }

    private fun getModuleName(directory: PsiDirectory): String {
        val projectDir = directory.project.guessProjectDir() ?: return DEFAULT_MODULE_NAME
        if (directory.virtualFile == projectDir) {
            return DEFAULT_MODULE_NAME
        }

        if (directory.name == DEFAULT_SOURCE_DIR) {
            return DEFAULT_MODULE_NAME
        }

        return directory.name
    }
}
