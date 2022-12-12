package org.vlang.lang.structure

import com.intellij.ide.util.treeView.smartTree.ActionPresentation
import com.intellij.ide.util.treeView.smartTree.ActionPresentationData
import com.intellij.ide.util.treeView.smartTree.Filter
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.util.PlatformIcons
import org.vlang.lang.psi.VlangNamedElement

class VlangPrivateMembersFilter : Filter {
    override fun isVisible(treeNode: TreeElement): Boolean {
        if (treeNode is VlangStructureViewFactory.Element) {
            val psiElement = treeNode.value
            if (psiElement !is VlangNamedElement) {
                return false
            }

            // show main even if it's private (default)
            if (psiElement.name == "main") {
                return true
            }

            return psiElement.isPublic()
        }
        return true
    }

    override fun isReverted() = true

    override fun getPresentation(): ActionPresentation {
        return ActionPresentationData(PRIVATE_MEMBERS_FILTER_TEXT, null, PlatformIcons.PRIVATE_ICON)
    }

    override fun getName() = PRIVATE_MEMBERS_FILTER_TEXT

    companion object {
        private const val PRIVATE_MEMBERS_FILTER_TEXT = "Show Private Members"
    }
}
