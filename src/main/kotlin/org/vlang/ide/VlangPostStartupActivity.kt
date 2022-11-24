package org.vlang.ide

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
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
