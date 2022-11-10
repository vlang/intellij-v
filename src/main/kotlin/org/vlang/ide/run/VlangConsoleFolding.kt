package org.vlang.ide.run

import com.intellij.execution.ConsoleFolding
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir

class VlangConsoleFolding : ConsoleFolding() {
    override fun shouldFoldLine(project: Project, line: String): Boolean {
        val projectDir = project.guessProjectDir()?.path ?: return false
        return line.startsWith(projectDir)
    }

    override fun getPlaceholderText(project: Project, lines: MutableList<String>): String? {
        val first = lines.firstOrNull() ?: return null
        val binName = first.substringBefore(" ")
        if (binName.endsWith(":")) {
            return null
        }

        val args = first.substringAfter(" ", "")
        val lastPart = binName.substringAfterLast('/')
        return "Running .../$lastPart $args"
    }
}
