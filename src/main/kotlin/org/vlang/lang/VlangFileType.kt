package org.vlang.lang

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.IconLoader

class VlangFileType : LanguageFileType(VlangLanguage.INSTANCE) {
    override fun getName() = "vlang"

    override fun getDescription() = "Vlang language file"

    override fun getDefaultExtension() = FILE_EXTENSION

    override fun getIcon() = IconLoader.getIcon("/icons/vlang.svg", this::class.java)

    companion object {
        const val FILE_EXTENSION = "v"
        val INSTANCE = VlangFileType()
    }
}
