package org.vlang.ide

import com.intellij.ide.BrowserUtil
import com.intellij.ide.plugins.InstalledPluginsState
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.util.registry.Registry
import com.jetbrains.cidr.execution.debugger.evaluation.CidrValue
import org.vlang.configurations.VlangProjectStructureState.Companion.projectStructure
import org.vlang.notifications.VlangNotification
import org.vlang.projectWizard.VlangToolchainFlavor
import org.vlang.toolchain.VlangKnownToolchainsState
import org.vlang.toolchain.VlangToolchain
import org.vlang.toolchain.VlangToolchainService.Companion.toolchainSettings
import javax.swing.SwingUtilities.invokeLater

class VlangPostStartupActivity : StartupActivity {
    override fun runActivity(project: Project) {
        val knownToolchains = VlangKnownToolchainsState.getInstance().knownToolchains
        val toolchainSettings = project.toolchainSettings

        val needFindToolchains = knownToolchains.isEmpty()
        if (needFindToolchains) {
            val toolchainCandidates = setupToolchainCandidates()
            if (toolchainCandidates.isEmpty()) {
                return
            }

            toolchainSettings.setToolchain(project, VlangToolchain.fromPath(toolchainCandidates.first()))
            return
        }

        if (toolchainSettings.isNotSet() && knownToolchains.isNotEmpty()) {
            toolchainSettings.setToolchain(project, VlangToolchain.fromPath(knownToolchains.first()))
        }

        Registry.get("cidr.debugger.value.stringEvaluator").setValue(false)
        Registry.get("cidr.debugger.lldb.statics").setValue(false)

        try {
            project.putUserData(CidrValue.DO_NOT_SHOW_ADDRESSES, true)
        } catch (e: NoClassDefFoundError) {
            // ignore
        }

        showVoscaNotification()
        checkUpdates(project)

        invokeLater {
            project.projectStructure.determineProjectStructure(project)
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

            VlangKnownToolchainsState.getInstance().knownToolchains = toolchainCandidates.toSet()
            return toolchainCandidates
        }

        private fun showVoscaNotification() {
            val key = "vosca.notification.shown"
            val isShown = PropertiesComponent.getInstance().getBoolean(key)
            if (isShown) {
                return
            }

            VlangNotification("IntelliJ V is now a VOSCA project!")
                .withActions(
                    VlangNotification.Action("Learn more about VOSCA...") { _, notification ->
                        BrowserUtil.browse("https://blog.vosca.dev/introducing-association/")
                        PropertiesComponent.getInstance().setValue(key, true)
                        notification.expire()
                    }
                )
                .withActions(
                    VlangNotification.Action("Don't show again") { _, notification ->
                        PropertiesComponent.getInstance().setValue(key, true)
                        notification.expire()
                    }
                )
                .show(null)
        }

        private fun checkUpdates(project: Project) {
            val hasNewerVersion = InstalledPluginsState.getInstance().hasNewerVersion(PLUGIN_ID)
            if (!hasNewerVersion) {
                return
            }

            val plugin = PluginManagerCore.getPlugin(PLUGIN_ID) ?: return
            val currentVersion = plugin.version

            val dontShowForVersion = PropertiesComponent.getInstance(project).getValue(DONT_SHOW_UPDATE_NOTIFICATION)
            if (dontShowForVersion != null && dontShowForVersion == currentVersion) {
                // skip any update for current version
                return
            }

            VlangNotification("New version of the V plugin is available")
                .withActions(
                    VlangNotification.Action("Update") { _, notification ->
                        invokeLater {
                            ShowSettingsUtil.getInstance().showSettingsDialog(project, "Plugins")
                            notification.expire()
                        }
                    }
                )
                .withActions(
                    VlangNotification.Action("What's new?") { _, notification ->
                        BrowserUtil.browse("${PLUGIN_URL}/versions")
                        notification.expire()
                    }
                )
                .withActions(
                    VlangNotification.Action("Don't show again") { _, notification ->
                        PropertiesComponent.getInstance(project).setValue(DONT_SHOW_UPDATE_NOTIFICATION, currentVersion, null)
                        notification.expire()
                    }
                )
                .show(project)
        }

        private val PLUGIN_ID = PluginId.getId("org.vlang")
        private val PLUGIN_URL = "https://plugins.jetbrains.com/plugin/20287-vlang"
        private const val DONT_SHOW_UPDATE_NOTIFICATION = "org.vlang.dont.show.update.notification"
    }
}
