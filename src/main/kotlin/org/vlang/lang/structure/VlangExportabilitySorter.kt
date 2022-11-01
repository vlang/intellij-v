package org.vlang.lang.structure

import com.intellij.icons.AllIcons
import com.intellij.ide.util.treeView.smartTree.ActionPresentation
import com.intellij.ide.util.treeView.smartTree.Sorter
import org.jetbrains.annotations.NonNls

class VlangExportabilitySorter : Sorter {
    override fun getComparator() = VlangExportabilityComparator.INSTANCE
    override fun isVisible() = true
    override fun getPresentation() = PRESENTATION
    override fun getName() = ID

    companion object {
        val INSTANCE: Sorter = VlangExportabilitySorter()

        private val PRESENTATION: ActionPresentation = object : ActionPresentation {
            override fun getText() = "Sort by Exportability"
            override fun getDescription() = "Sort all elements by exportability"
            override fun getIcon() = AllIcons.ObjectBrowser.VisibilitySort
        }

        private const val ID: @NonNls String = "EXPORTABILITY_SORTER"
    }
}
