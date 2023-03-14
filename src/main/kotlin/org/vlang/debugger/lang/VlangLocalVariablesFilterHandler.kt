package org.vlang.debugger.lang

import com.intellij.openapi.project.Project
import com.intellij.xdebugger.XSourcePosition
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.evaluation.LocalVariablesFilterHandler
import org.vlang.debugger.withName
import org.vlang.debugger.withType
import java.util.concurrent.CompletableFuture

class VlangLocalVariablesFilterHandler : LocalVariablesFilterHandler {
    override fun filterVars(proj: Project, pos: XSourcePosition, vars: List<LLValue>): CompletableFuture<List<LLValue>> {
        val filteredVars = vars
            .map {
                if (it.name == "_V_closure_ctx") {
                    return@map it
                        .withName("[captured variables]")
                        .withType("Closure Context")
                }

                it
            }.filter {
                !it.name.startsWith("_") &&
                        !it.name.startsWith("mr_") &&
                        !it.name.startsWith("thread__") &&
                        !it.name.startsWith("arg__") &&
                        !DEFER_VARIABLE_REGEX.matches(it.name)
            }
        return CompletableFuture.completedFuture(filteredVars)
    }

    companion object {
        val DEFER_VARIABLE_REGEX = "^.*_defer_\\d+$".toRegex()
    }
}
