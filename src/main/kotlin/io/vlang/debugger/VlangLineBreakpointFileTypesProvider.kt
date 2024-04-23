package io.vlang.debugger

import com.jetbrains.cidr.execution.debugger.breakpoints.CidrLineBreakpointFileTypesProvider
import io.vlang.lang.VlangFileType

class VlangLineBreakpointFileTypesProvider : CidrLineBreakpointFileTypesProvider {
    override fun getFileTypes() = setOf(VlangFileType)
}
