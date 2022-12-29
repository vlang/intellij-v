package org.vlang.debugger.runconfig

import com.intellij.execution.filters.TextConsoleBuilder
import com.intellij.xdebugger.XDebugSession
import com.jetbrains.cidr.execution.debugger.CidrLocalDebugProcess

class VlangLocalDebugProcess(
    runParameters: VlangDebugRunParameters,
    debugSession: XDebugSession,
    consoleBuilder: TextConsoleBuilder,
) : CidrLocalDebugProcess(runParameters, debugSession, consoleBuilder) {

    override fun isLibraryFrameFilterSupported(): Boolean = false
    override fun isRichValueDescriptionEnabled(): Boolean = true
}
