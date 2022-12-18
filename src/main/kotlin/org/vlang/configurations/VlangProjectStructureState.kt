package org.vlang.configurations

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.psi.PsiManager
import com.intellij.util.xmlb.XmlSerializerUtil
import org.vlang.lang.psi.VlangFile
import org.vlang.utils.isVlangFile

@State(
    name = "V Project",
    storages = [Storage(StoragePathMacros.WORKSPACE_FILE)]
)
class VlangProjectStructureState : PersistentStateComponent<VlangProjectStructureState?> {
    companion object {
        val Project.projectStructure
            get() = service<VlangProjectStructureState>()
    }

    var libraryWithTopModule = false
    var srcBased = false
    var withLocalModules = false

    override fun getState() = this

    override fun loadState(state: VlangProjectStructureState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    fun determineProjectStructure(project: Project) {
        val projectDir = project.guessProjectDir() ?: return

        srcBased = projectDir.findChild("src") != null
        withLocalModules = projectDir.findChild("modules") != null && projectDir.findChild("v.mod") != null

        val manager = PsiManager.getInstance(project)
        val containTopLevelNonMainModule = projectDir.children.asSequence()
            .filter { it.isVlangFile }
            .any {
                val psi = manager.findFile(it) as? VlangFile ?: return@any false
                psi.getModuleName() == projectDir.name
            }

        libraryWithTopModule = containTopLevelNonMainModule
    }
}
