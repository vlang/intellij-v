package org.vlang.project

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.shelf.ShelveChangesManager.PostStartupActivity

class VlangPostStartupActivity : PostStartupActivity() {
    override fun runActivity(project: Project) {

        // TODO: think about how to handle this better
//        val stdlib = VlangConfiguration.getInstance(project).stdlibLocation ?: return
//        val table = LibraryTablesRegistrar.getInstance().getLibraryTable(project)
//
//        val stdLibrary = table.libraries.find { it.name == "V Standard Library" }
//        if (stdLibrary != null) {
//            return
//        }
//
//        runWriteActionAndWait {
//            val lib = table.createLibrary("V Standard library")
//            val model = lib.modifiableModel
//            model.addRoot(stdlib, OrderRootType.SOURCES)
//            model.commit()
//
//            val modules = ModuleManager.getInstance(project).modules
//            modules.forEach {
//                ModuleRootModificationUtil.addDependency(it, lib)
//            }
//        }

    }
}
