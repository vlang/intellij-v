package org.vlang.debugger

import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebugSessionListener
import com.jetbrains.cidr.execution.debugger.BackendConsoleInjectionHelper
import com.jetbrains.cidr.execution.debugger.CidrDebugProcess
import com.jetbrains.cidr.execution.debugger.backend.lang.GDBExpressionPlaceholder
import org.vlang.utils.ancestorOrSelf
import org.vlang.utils.virtualFile

class VlangBackendConsoleInjectionHelper : BackendConsoleInjectionHelper {
    override fun subscribeToInjection(session: XDebugSession) {
        val connection = session.project.messageBus.connect()

        val listener = object : VlangDebugInjectionListener, XDebugSessionListener {
            @Volatile
            private var document: Document? = null

            override fun evalDebugContext(host: PsiLanguageInjectionHost, context: VlangDebugInjectionListener.DebugContext) {
                val document = host.originalDocument ?: return
                val process = document.getUserData(CidrDebugProcess.DEBUG_PROCESS_KEY) ?: return
                context.element = process.debuggerContext?.ancestorOrSelf()
            }

            override fun didInject(host: PsiLanguageInjectionHost) {
                if (host is GDBExpressionPlaceholder) {
                    this.document = host.originalDocument
                }
            }

            override fun stackFrameChanged() {
                val file = document?.virtualFile ?: return
                invokeLater(ModalityState.NON_MODAL) {
                    PsiDocumentManager.getInstance(session.project).reparseFiles(setOf(file), true)
                }
            }

            override fun sessionStopped() {
                connection.disconnect()
            }
        }

        connection.subscribe(VlangDebugInjectionListener.INJECTION_TOPIC, listener)
        session.addSessionListener(listener)
    }

    private val PsiLanguageInjectionHost.originalDocument: Document?
        get() = containingFile.originalFile.viewProvider.document
}
