package org.vlang.ide.templates

import com.intellij.openapi.project.guessProjectDir
import com.intellij.psi.PsiDirectory
import org.vlang.ide.refactoring.rename.VlangNamesValidator
import java.util.*

object VlangTemplateUtil {
    private const val DEFAULT_MODULE_NAME = "main"
    private const val ATTRIBUTE_MODULE_NAME = "V_MODULE_NAME"
    private const val DEFAULT_SOURCE_DIR = "src"
    private val IDENTIFIER_REGEX = Regex("[a-zA-Z_][a-zA-Z0-9_]*")

    fun setModuleNameAttribute(directory: PsiDirectory, props: Properties) {
        var moduleName = getModuleName(directory)
        if (VlangNamesValidator.isKeyword(moduleName, null)) {
            moduleName = "@$moduleName"
        }
        props.setProperty(ATTRIBUTE_MODULE_NAME, moduleName)
    }

    private fun getModuleName(directory: PsiDirectory): String {
        val projectDir = directory.project.guessProjectDir() ?: return DEFAULT_MODULE_NAME
        if (directory.virtualFile == projectDir) {
            return DEFAULT_MODULE_NAME
        }

        val name = directory.name

        if (name == DEFAULT_SOURCE_DIR) {
            return DEFAULT_MODULE_NAME
        }

        if (!name.matches(IDENTIFIER_REGEX)) {
            return DEFAULT_MODULE_NAME
        }

        return name
    }
}
