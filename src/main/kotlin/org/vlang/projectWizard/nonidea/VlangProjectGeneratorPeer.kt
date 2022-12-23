package org.vlang.projectWizard.nonidea

import com.intellij.platform.GeneratorPeerImpl
import com.intellij.ui.dsl.builder.TopGap
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import org.vlang.configurations.VlangProjectSettingsForm
import org.vlang.configurations.VlangToolchainsState
import org.vlang.ide.VlangPostStartupActivity
import javax.swing.JComponent

class VlangProjectGeneratorPeer(model: VlangProjectSettingsForm.Model) : GeneratorPeerImpl<Unit>() {
    init {
        val knownToolchains = VlangToolchainsState.getInstance().knownToolchains
        val needFindToolchains = knownToolchains.isEmpty()
        if (needFindToolchains) {
            VlangPostStartupActivity.setupToolchainCandidates()
        }
    }

    private val toolchainSettingsForm = VlangProjectSettingsForm(null, model).createComponent()
    private val newProjectPanel = panel {
        row("Toolchain:") {
            cell(toolchainSettingsForm)
                .horizontalAlign(HorizontalAlign.FILL)
        }.topGap(TopGap.NONE)
    }

    override fun getSettings(): Unit = Unit

    override fun getComponent(): JComponent = newProjectPanel
}
