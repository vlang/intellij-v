package org.vlang.lang

import com.intellij.openapi.fileTypes.LanguageFileType
import org.vlang.ide.ui.VIcons

class VlangFileType : LanguageFileType(VlangLanguage.INSTANCE) {
    override fun getName() = "V"

    override fun getDescription() = "V language file"

    override fun getDefaultExtension() = FILE_EXTENSION

    override fun getIcon() = VIcons.V

    companion object {
        const val FILE_EXTENSION = "v"
        val INSTANCE = VlangFileType()
    }
}
