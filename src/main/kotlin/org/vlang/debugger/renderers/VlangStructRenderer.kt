package org.vlang.debugger.renderers

import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData
import org.vlang.debugger.lang.VlangDebuggerState
import org.vlang.debugger.withContext
import org.vlang.debugger.withDescription
import org.vlang.lang.psi.VlangMethodDeclaration
import org.vlang.lang.psi.VlangStructDeclaration
import org.vlang.lang.psi.types.VlangStructTypeEx
import org.vlang.lang.stubs.index.VlangClassLikeIndex

object VlangStructRenderer : VlangValueRenderer() {
    private var klass: VlangStructDeclaration? = null

    override fun isApplicable(project: Project, value: LLValue): Boolean {
        val type = value.type
        val fqn = VlangCTypeParser.convertCNameToVName(type.trim('*', ' '))
        val klass = runReadAction {
            VlangClassLikeIndex.find(fqn, project, null, null).firstOrNull()
        }
        if (klass !is VlangStructDeclaration) {
            return false
        }
        this.klass = klass
        return true
    }

    override fun getDisplayType(type: String): String = type

    override fun getData(value: VlangValue): LLValueData {
        if (!VlangDebuggerState.getInstance().showStrMethodResult) {
            return value.data
        }
        if (klass == null) return value.data
        val name = runReadAction { klass?.getQualifiedName() } ?: return value.data
        val cname = VlangCTypeParser.toCName(name)
        val strMethodName = "${cname}_str"

        val type = runReadAction { klass!!.getType(null) } as? VlangStructTypeEx ?: return value.data
        val method = runReadAction { type.findMethod(value.project, "str") } ?: return value.data
        if (!enoughSimpleMethod(method)) return value.data

        val byReference = runReadAction { method.byReference() }
        val dereference = if (!byReference) "*" else ""

        val dataIsPointer = value.data.value.startsWith("0x")
        val address = if (!dataIsPointer) value.llValue.address?.toString(16) else value.data.value.removePrefix("0x")
        val argument = "($cname*)(0x$address)"
        val strResult = value.context.evaluate("$strMethodName($dereference$argument)").withContext(value.context)

        return value.data.withDescription(strResult.data.presentableValue)
    }

    private fun enoughSimpleMethod(method: VlangMethodDeclaration): Boolean {
        val body = method.getBlock() ?: return false
        val statements = body.statementList
        if (statements.size > 2) return false
        return true
    }
}
