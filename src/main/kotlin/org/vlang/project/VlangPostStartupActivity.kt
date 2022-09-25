package org.vlang.project

import com.intellij.ProjectTopics
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootEvent
import com.intellij.openapi.roots.ModuleRootListener
import com.intellij.openapi.vcs.changes.shelf.ShelveChangesManager.PostStartupActivity
import com.intellij.util.indexing.FileBasedIndexImpl
import org.vlang.sdk.VlangSdkUtil

class VlangPostStartupActivity : PostStartupActivity() {
    override fun runActivity(project: Project) {
        val connection = project.messageBus.connect()

        // TODO:
        connection.subscribe(ProjectTopics.PROJECT_ROOTS, object : ModuleRootListener {
            override fun rootsChanged(event: ModuleRootEvent) {
                if (event.isCausedByWorkspaceModelChangesOnly) {
                    val sdk = VlangSdkUtil.getSdkSrcDir(project, null)
                    val root = sdk?.findChild("vlib") ?: return

                    FileBasedIndexImpl.getInstance().requestReindex(root)
                    return
                }
            }
        })
    }
}
