package org.vlang.debugger.runconfig

import com.jetbrains.cidr.execution.debugger.CidrDebugProcess
import com.jetbrains.cidr.execution.debugger.CidrDebugProcessConfigurator

class VlangDebugProcessConfigurator : CidrDebugProcessConfigurator {
    override fun configure(process: CidrDebugProcess) {
        VlangDebugProcessConfigurationHelper(process).configure()
    }
}
