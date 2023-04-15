package org.vlang.debugger

import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.util.messages.Topic
import org.vlang.lang.psi.VlangCompositeElement

interface VlangDebugInjectionListener {
    data class DebugContext(var element: VlangCompositeElement? = null)

    fun evalDebugContext(host: PsiLanguageInjectionHost, context: DebugContext)

    fun didInject(host: PsiLanguageInjectionHost)

    companion object {
        @JvmField
        val INJECTION_TOPIC = Topic.create("V Language Injected", VlangDebugInjectionListener::class.java)
    }
}
