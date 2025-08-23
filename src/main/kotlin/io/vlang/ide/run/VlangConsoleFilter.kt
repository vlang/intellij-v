package io.vlang.ide.run

import com.intellij.execution.filters.Filter
import com.intellij.execution.filters.OpenFileHyperlinkInfo
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.search.GlobalSearchScope

/**
 * A filter that detects V paths (f.i. in panic messages) in the console output and makes the file paths clickable.
 *
 * ```shell
 * # v -g run panic.v
 * ================ V panic ================
 *    module: main
 *  function: main()
 *   message: Aaaa!
 *      file: panic.v:4
 *    v hash: 63997d6
 * =========================================
 * /home/user/v/vlib/builtin/builtin.c.v:88: at panic_debug: Backtrace
 * /home/user/tmp/panic.v:4: by main__main
 * /tmp/v_1000/panic.01K3C0AKM90VQXJF4HT25ZKK3X.tmp.c:2031: by main
 * ```
 */
class VlangConsoleFilter(val project: Project, val scope: GlobalSearchScope) : Filter {

    override fun applyFilter(line: String, entireLength: Int): Filter.Result? {
        val messageSuffix = pattern.find(line) ?: return null
        val pathEnd = messageSuffix.groups[1]!!.range.last + 1
        val path = line.take(pathEnd)
        val lineNumber = Integer.parseInt(messageSuffix.groupValues[2]) - 1

        val offset = entireLength - line.length
        val file = LocalFileSystem.getInstance().findFileByPathIfCached(path) ?: return null
        return Filter.Result(
            offset, offset + messageSuffix.range.last,
            OpenFileHyperlinkInfo(project, file, lineNumber, 0)
        )
    }

    companion object {
        val pattern = Regex("""\.(v|vsh|tmp\.c):(\d+):""")
    }
}