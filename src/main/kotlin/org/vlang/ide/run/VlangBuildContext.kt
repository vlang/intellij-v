package org.vlang.ide.run

import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import java.nio.file.Path
import java.util.concurrent.atomic.AtomicInteger

class VlangBuildContext(
    val project: Project,
    val environment: ExecutionEnvironment,
    val taskName: String,
    val buildId: Any,
    val parentId: Any,
) {
    lateinit var workingDirectory: Path

    @Volatile
    lateinit var processHandler: ProcessHandler

    val errors: AtomicInteger = AtomicInteger()
    val warnings: AtomicInteger = AtomicInteger()

    val started: Long = System.currentTimeMillis()
}
