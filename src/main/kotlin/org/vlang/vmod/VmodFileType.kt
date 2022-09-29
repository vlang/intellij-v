package org.vlang.vmod

import com.intellij.openapi.fileTypes.LanguageFileType
import org.vlang.ide.ui.VIcons

class VmodFileType : LanguageFileType(VmodLanguage.INSTANCE) {
    override fun getName() = "V Module"

    override fun getDescription() = "V module file"

    override fun getDefaultExtension() = FILE_EXTENSION

    override fun getIcon() = VIcons.Module

    companion object {
        const val FILE_EXTENSION = "mod"
        val INSTANCE = VmodFileType()
    }
}
