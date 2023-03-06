package org.vlang.debugger

import com.jetbrains.cidr.ArchitectureType
import com.jetbrains.cidr.execution.debugger.backend.lldb.LLDBDriver
import com.jetbrains.cidr.execution.debugger.backend.lldb.LLDBDriverConfiguration

class VlangLldbDriver(
    handler: Handler,
    starter: LLDBDriverConfiguration,
    architectureType: ArchitectureType,
) : LLDBDriver(handler, starter, architectureType)
