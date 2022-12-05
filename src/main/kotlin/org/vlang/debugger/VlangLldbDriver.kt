package org.vlang.debugger

import com.jetbrains.cidr.ArchitectureType
import com.jetbrains.cidr.execution.debugger.backend.*
import com.jetbrains.cidr.execution.debugger.backend.lldb.LLDBDriver
import com.jetbrains.cidr.execution.debugger.backend.lldb.LLDBDriverConfiguration
import com.jetbrains.cidr.execution.debugger.memory.Address

class VlangLldbDriver(
    handler: Handler,
    starter: LLDBDriverConfiguration,
    architectureType: ArchitectureType,
) : LLDBDriver(handler, starter, architectureType) {

    override fun getVariables(thread: LLThread, frame: LLFrame): List<LLValue> =
        super.getVariables(thread, frame)

    override fun getFrames(
        thread: LLThread,
        from: Int,
        count: Int,
        untilFirstLineWithCode: Boolean
    ): ResultList<LLFrame> =
        super.getFrames(thread, from, count, untilFirstLineWithCode)
            .mapItems { it.patch() }

    override fun handleBreakpoint(stopPlace: StopPlace, breakpointNumber: Int) {
        super.handleBreakpoint(stopPlace.patch(), breakpointNumber)
    }

    override fun handleException(
        stopPlace: StopPlace,
        exceptionAddress: Address,
        exceptionFile: String?,
        exceptionHash: DebuggerSourceFileHash?,
        exceptionLine: Int,
        description: String
    ) {
        super.handleException(stopPlace.patch(), exceptionAddress, exceptionFile, exceptionHash, exceptionLine, description)
    }

    override fun handleSignal(stopPlace: StopPlace, signal: String, meaning: String) {
        super.handleSignal(stopPlace.patch(), signal, meaning)
    }

    override fun handleSelectedFrameChanged(thread: LLThread, frame: LLFrame) {
        super.handleSelectedFrameChanged(thread, frame.patch())
    }

    override fun handleInterrupted(stopPlace: StopPlace) {
        super.handleInterrupted(stopPlace.patch())
    }

    override fun handleWatchpoint(stopPlace: StopPlace, watchpointNumber: Int) {
        super.handleWatchpoint(stopPlace.patch(), watchpointNumber)
    }

    private fun LLFrame.patch(): LLFrame {
        val rawName = function
        val name = rawName.takeWhile { it != ' ' && it != '(' && it != '[' }
        return this
    }

    private fun StopPlace.patch() =
        StopPlace(thread, frame.patch())

}