package org.vlang.ide.projectview

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.util.treeView.PresentableNodeDescriptor.ColoredFragment
import com.intellij.ui.SimpleTextAttributes
import com.intellij.util.FontUtil.spaceAndThinSpace
import org.vlang.configurations.VlangConfiguration
import org.vlang.ide.ui.VIcons

object VlangNodeDecoration {
    private val wideSpacer = ColoredFragment(spaceAndThinSpace(), SimpleTextAttributes.REGULAR_ATTRIBUTES)

    fun apply(node: ProjectViewNode<*>, data: PresentationData): Boolean {
        setName(data)

        if (node.virtualFile == null) {
            return false
        }

        if (node.virtualFile == VlangConfiguration.getInstance(node.project).srcLocation) {
            data.setIcon(VIcons.SourceRoot)
            data.tooltip = "Source root"
            return true
        }

        if (node.virtualFile == VlangConfiguration.getInstance(node.project).localModulesLocation) {
            data.setIcon(VIcons.LocalModulesRoot)
            data.tooltip = "Local modules root"

            data.addText(wideSpacer)
            data.addText(
                ColoredFragment(
                    " local modules",
                    SimpleTextAttributes.GRAY_ATTRIBUTES
                )
            )
            return true
        }

        return true
    }

    private fun hasEmptyColoredTextValue(data: PresentationData) = data.coloredText.isEmpty()

    private fun setName(data: PresentationData) {
        if (hasEmptyColoredTextValue(data)) {
            val presentableText = data.presentableText
            if (presentableText != null) {
                data.addText(presentableText, SimpleTextAttributes.REGULAR_ATTRIBUTES)
            }
        }
    }
}
