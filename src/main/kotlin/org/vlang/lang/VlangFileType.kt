package org.vlang.lang

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.vfs.VirtualFile
import org.vlang.ide.ui.VIcons

object VlangFileType : LanguageFileType(VlangLanguage) {
    override fun getName() = "V"
    override fun getDescription() = "V file"
    override fun getDefaultExtension() = "v"
    override fun getIcon() = VIcons.V
    override fun getCharset(file: VirtualFile, content: ByteArray): String = "UTF-8"
}
