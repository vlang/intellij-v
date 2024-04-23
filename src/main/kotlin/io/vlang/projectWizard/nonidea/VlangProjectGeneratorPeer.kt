package io.vlang.projectWizard.nonidea

import com.intellij.platform.GeneratorPeerImpl
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.TopGap
import com.intellij.ui.dsl.builder.panel
import io.vlang.configurations.VlangProjectSettingsForm
import io.vlang.ide.VlangPostStartupActivity
import io.vlang.toolchain.VlangKnownToolchainsState
import javax.swing.JComponent

class VlangProjectGeneratorPeer(model: VlangProjectSettingsForm.Model) : GeneratorPeerImpl<Unit>() {
    init {
        val knownToolchains = VlangKnownToolchainsState.getInstance().knownToolchains
        val needFindToolchains = knownToolchains.isEmpty()
        if (needFindToolchains) {
            VlangPostStartupActivity.setupToolchainCandidates()
        }
    }

    private val toolchainSettingsForm = VlangProjectSettingsForm(null, model).createComponent()
    private val newProjectPanel = panel {
        row("Toolchain:") {
            cell(toolchainSettingsForm)
                .align(AlignX.FILL)
        }.topGap(TopGap.NONE)
    }

    override fun getSettings(): Unit = Unit

    override fun getComponent(): JComponent = newProjectPanel
}
