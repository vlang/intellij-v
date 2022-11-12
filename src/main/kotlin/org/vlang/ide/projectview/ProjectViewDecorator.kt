package org.vlang.ide.projectview

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ProjectViewNodeDecorator

class ProjectViewDecorator : ProjectViewNodeDecorator {
    override fun decorate(node: ProjectViewNode<*>, presentation: PresentationData) {
        presentation.isChanged = VlangNodeDecoration.apply(node, presentation)
    }
}
