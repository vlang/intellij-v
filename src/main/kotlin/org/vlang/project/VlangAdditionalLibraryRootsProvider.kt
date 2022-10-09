package org.vlang.project

import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.AdditionalLibraryRootsProvider
import com.intellij.openapi.roots.SyntheticLibrary
import com.intellij.openapi.vfs.VirtualFile
import org.vlang.configurations.VlangConfiguration
import org.vlang.ide.ui.VIcons

class VlangAdditionalLibraryRootsProvider : AdditionalLibraryRootsProvider() {
    open class LibraryBase(private val name: String, private val sourceRoot: VirtualFile) : SyntheticLibrary(), ItemPresentation {
        override fun getSourceRoots() = listOf(sourceRoot)

        override fun getPresentableText() = name

        override fun getIcon(unused: Boolean) = VIcons.Vlang

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as LibraryBase

            if (name != other.name) return false
            if (sourceRoot != other.sourceRoot) return false

            return true
        }

        override fun hashCode() = sourceRoot.hashCode()
    }

    class StandardLibrary(sourceRoot: VirtualFile) : LibraryBase("V Standard Library", sourceRoot)
    class StandardModules(sourceRoot: VirtualFile) : LibraryBase("V Modules", sourceRoot)

    override fun getAdditionalProjectLibraries(project: Project): Collection<SyntheticLibrary> {
        val result = mutableListOf<SyntheticLibrary>()

        val sourceRoot = VlangConfiguration.getInstance(project).stdlibLocation
        if (sourceRoot != null) {
            result.add(StandardLibrary(sourceRoot))
        }
        val modulesRoot = VlangConfiguration.getInstance(project).modulesLocation
        if (modulesRoot != null) {
            result.add(StandardModules(modulesRoot))
        }

        return result
    }

    override fun getRootsToWatch(project: Project): Collection<VirtualFile> {
        if (ApplicationManager.getApplication().isUnitTestMode) return emptySet()

        val result = mutableListOf<VirtualFile>()

        val sourceRoot = VlangConfiguration.getInstance(project).stdlibLocation
        if (sourceRoot != null) {
            result.add(sourceRoot)
        }

        val modulesRoot = VlangConfiguration.getInstance(project).modulesLocation
        if (modulesRoot != null) {
            result.add(modulesRoot)
        }

        return result
    }
}
