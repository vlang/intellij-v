package org.vlang.sdk

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.libraries.LibraryType
import com.intellij.openapi.roots.libraries.NewLibraryConfiguration
import com.intellij.openapi.roots.libraries.PersistentLibraryKind
import com.intellij.openapi.roots.libraries.ui.LibraryEditorComponent
import com.intellij.openapi.roots.libraries.ui.LibraryPropertiesEditor
import com.intellij.openapi.vfs.VirtualFile
import org.vlang.ide.ui.VIcons
import javax.swing.Icon
import javax.swing.JComponent

class VlangLibraryType : LibraryType<VlangLibraryProperties>(LIBRARY_KIND) {
    override fun getCreateActionName(): String? {
        return null
    }

    override fun createNewLibrary(
        parentComponent: JComponent,
        contextDirectory: VirtualFile?,
        project: Project
    ): NewLibraryConfiguration? {
        return null
    }

    override fun createPropertiesEditor(editorComponent: LibraryEditorComponent<VlangLibraryProperties?>): LibraryPropertiesEditor? {
        return null
    }

    override fun getIcon(properties: VlangLibraryProperties?): Icon? {
        return VIcons.V
    }

    companion object {
        const val VLANG_PACKAGES_LIBRARY_NAME = "V Library"

        val LIBRARY_KIND: PersistentLibraryKind<VlangLibraryProperties> =
            object : PersistentLibraryKind<VlangLibraryProperties>("VlangLibraryType") {
                override fun createDefaultProperties(): VlangLibraryProperties {
                    return VlangLibraryProperties()
                }
            }
    }
}