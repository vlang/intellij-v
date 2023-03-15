package org.vlang.debugger.runconfig

import com.intellij.execution.filters.TextConsoleBuilder
import com.intellij.xdebugger.XDebugSession
import com.jetbrains.cidr.execution.debugger.CidrLocalDebugProcess
import com.jetbrains.cidr.execution.debugger.evaluation.CidrValue

class VlangLocalDebugProcess(
    runParameters: VlangDebugRunParameters,
    debugSession: XDebugSession,
    consoleBuilder: TextConsoleBuilder,
) : CidrLocalDebugProcess(runParameters, debugSession, consoleBuilder) {

    init {
        putUserData(CidrValue.THREAT_VALUE_AS_RICH_TEXT, java.lang.Boolean.TRUE)
    }

    override fun isLibraryFrameFilterSupported(): Boolean = false
    override fun isRichValueDescriptionEnabled(): Boolean = true
}
