package org.vlang.project

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import org.vlang.utils.isStubFile

class StubFileNotificationProvider(project: Project) : VlangEditorNotificationProviderBase(project) {
    override fun createNotificationPanel(file: VirtualFile, editor: FileEditor, project: Project): EditorNotificationPanel? {
        if (!file.isStubFile) return null
        return createSimpleNotification(
            "This is a stub file, it contains a description of some of the features of the language. This is not real code, it is only there to help the IDE."
        )
    }
}
