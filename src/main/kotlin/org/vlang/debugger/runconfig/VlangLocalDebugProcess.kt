package org.vlang.debugger.runconfig

import com.intellij.execution.filters.TextConsoleBuilder
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.breakpoints.XLineBreakpoint
import com.intellij.xdebugger.frame.XSuspendContext
import com.jetbrains.cidr.execution.debugger.CidrExecutionStack
import com.jetbrains.cidr.execution.debugger.CidrLocalDebugProcess
import com.jetbrains.cidr.execution.debugger.CidrStackFrame
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver
import com.jetbrains.cidr.execution.debugger.backend.LLBreakpoint
import com.jetbrains.cidr.execution.debugger.breakpoints.CidrBreakpointHandler

class VlangLocalDebugProcess(
    val runParameters: VlangDebugRunParameters,
    val debugSession: XDebugSession,
    consoleBuilder: TextConsoleBuilder,
) : CidrLocalDebugProcess(runParameters, debugSession, consoleBuilder) {
    override fun isLibraryFrameFilterSupported() = false
//    override fun getBreakpointHandlers(): Array<XBreakpointHandler<*>> {
//        return super.getBreakpointHandlers() + object : XBreakpointHandler<XLineBreakpoint<*>>() {
//            override fun registerBreakpoint(breakpoint: XLineBreakpoint<*>) {
//
//            }
//
//            override fun unregisterBreakpoint(breakpoint: XLineBreakpoint<*>, temporary: Boolean) {
//
//            }
//        }
//    }

//    override fun handleBreakpoint(stopPlace: DebuggerDriver.StopPlace, breakpointNumber: Int) {
//        val line = stopPlace.frame.line
//        val fileName = stopPlace.frame.file
//        if (fileName != null) {
//            val file = LocalFileSystem.getInstance().findFileByPath(fileName)
//            val psiFile = file?.let { PsiManager.getInstance(runParameters.project).findFile(it) }
//            if (psiFile != null) {
//                val document = PsiDocumentManager.getInstance(runParameters.project).getDocument(psiFile)
//                val lineStartOffset = document!!.getLineStartOffset(line)
//                val lineEndOffset = document.getLineEndOffset(line)
//
//                val element = psiFile.findElementAt(
//                    lineStartOffset + 10
//                )
//
//                if (element != null) {
//                    if (element is PsiComment) {
//                        return
//                    }
//                }
//            }
//        }
//
//        super.handleBreakpoint(stopPlace, breakpointNumber)
//    }

//    override fun startStepOver(context: XSuspendContext?) {
//        val stack = context?.activeExecutionStack as? CidrExecutionStack
//
////        val frame = session.currentStackFrame as CidrStackFrame
//
//        val pos = stack?.topFrame?.sourcePosition ?: return
////        super.startStepOver(context)
//
//
//        jumpToLine(stack.thread, pos.file, pos.line + 1)
////        this.postCommand { driver ->
////            driver.stepOver(true)
////        }
//    }

    override fun isRichValueDescriptionEnabled(): Boolean {
        return true
    }

    override fun createBreakpointHandler(): CidrBreakpointHandler {
        return object : CidrBreakpointHandler(this) {
            override fun handleBreakpointAdded(newBreakpoint: LLBreakpoint) {
                val uiBreakpoint = getXBreakpoint(newBreakpoint.id)
                uiBreakpoint?.sourcePosition

                super.handleBreakpointAdded(newBreakpoint)
            }

            override fun addCodepointsInBackend(
                driver: DebuggerDriver,
                breakpoint: XLineBreakpoint<*>,
                threadId: Long,
                frameIndex: Int,
            ): MutableCollection<LLBreakpoint> {
                breakpoint.fileUrl
                breakpoint.line

                return super.addCodepointsInBackend(driver, breakpoint, threadId, frameIndex)
            }
        }
    }
}
