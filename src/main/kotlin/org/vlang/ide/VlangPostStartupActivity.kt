package org.vlang.ide

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.util.registry.Registry
import com.jetbrains.cidr.execution.debugger.evaluation.CidrValue
import org.vlang.configurations.VlangProjectSettingsState.Companion.projectSettings
import org.vlang.configurations.VlangToolchainsState
import org.vlang.projectWizard.VlangToolchainFlavor

class VlangPostStartupActivity : StartupActivity {
    override fun runActivity(project: Project) {
        val knownToolchains = VlangToolchainsState.getInstance().knownToolchains

        val needFindToolchains = knownToolchains.isEmpty()
        if (needFindToolchains) {
            val toolchainCandidates = setupToolchainCandidates()
            if (toolchainCandidates.isEmpty()) {
                return
            }
            project.projectSettings.setToolchain(project, toolchainCandidates.first())
            return
        }

        if (project.projectSettings.toolchainLocation.isEmpty() && knownToolchains.isNotEmpty()) {
            project.projectSettings.setToolchain(project, knownToolchains.first())
        }

        Registry.get("cidr.debugger.value.stringEvaluator").setValue(false)
        Registry.get("cidr.debugger.lldb.statics").setValue(false)
        project.putUserData(CidrValue.DO_NOT_SHOW_ADDRESSES, true)
    }

    companion object {
        fun setupToolchainCandidates(): List<String> {
            val toolchainCandidates = VlangToolchainFlavor.getApplicableFlavors()
                .flatMap { it.suggestHomePaths() }
                .distinct()
                .map { it.toString() }

            if (toolchainCandidates.isEmpty()) {
                return emptyList()
            }

            VlangToolchainsState.getInstance().knownToolchains = toolchainCandidates.toSet()
            return toolchainCandidates
        }
    }
}
