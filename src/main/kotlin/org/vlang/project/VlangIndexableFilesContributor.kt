package org.vlang.project

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ContentIterator
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileFilter
import com.intellij.util.indexing.IndexingBundle
import com.intellij.util.indexing.roots.IndexableFilesContributor
import com.intellij.util.indexing.roots.IndexableFilesIterationMethods
import com.intellij.util.indexing.roots.IndexableFilesIterator
import com.intellij.util.indexing.roots.kind.IndexableSetOrigin
import com.intellij.util.indexing.roots.kind.ProjectFileOrDirOrigin
import org.vlang.configurations.VlangConfiguration
import java.util.function.Predicate

@Suppress("UnstableApiUsage")
class VlangIndexableFilesContributor : IndexableFilesContributor {
    internal class MyIterator(private val dir: VirtualFile) : IndexableFilesIterator {
        private fun getName() = "V Standard Library"

        override fun getDebugName(): String = "V Standard Library"

        override fun getIndexingProgressText(): String {
            val name = getName()
            if (name.isNotEmpty()) {
                return IndexingBundle.message("indexable.files.provider.indexing.named.provider", name)
            }
            return IndexingBundle.message("indexable.files.provider.indexing.additional.dependencies")
        }

        override fun getRootsScanningProgressText(): String {
            val name = getName()
            if (name.isNotEmpty()) {
                return IndexingBundle.message("indexable.files.provider.scanning.library.name", name)
            }
            return IndexingBundle.message("indexable.files.provider.scanning.additional.dependencies")
        }

        override fun getOrigin(): IndexableSetOrigin {
            return object : ProjectFileOrDirOrigin {
                override val fileOrDir: VirtualFile
                    get() = dir
            }
        }

        override fun iterateFiles(project: Project, fileIterator: ContentIterator, fileFilter: VirtualFileFilter): Boolean {
            return IndexableFilesIterationMethods.iterateRoots(project, mutableListOf(dir), fileIterator, fileFilter)
        }

        override fun getRootUrls(project: Project): MutableSet<String> {
            return mutableSetOf(dir.url)
        }
    }

    override fun getIndexableFiles(project: Project): MutableList<IndexableFilesIterator> {
        val stdlib = VlangConfiguration.getInstance(project).stdlibLocation ?: return mutableListOf()

        return mutableListOf(
            MyIterator(stdlib)
        )
    }

    override fun getOwnFilePredicate(project: Project): Predicate<VirtualFile> {
        return Predicate { file ->
            file.extension == "v"
        }
    }
}
