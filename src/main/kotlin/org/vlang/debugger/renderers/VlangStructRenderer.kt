package org.vlang.debugger.renderers

import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData
import org.vlang.debugger.*
import org.vlang.debugger.lang.VlangDebuggerState
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
            return highlightPointer(value)
        }
        if (klass == null) return highlightPointer(value)

        val data = value.data
        if (data.isNullPointer) {
            return highlightPointer(value)
        }

        val name = runReadAction { klass?.getQualifiedName() } ?: return highlightPointer(value)
        val cname = VlangCTypeParser.toCName(name)
        val strMethodName = "${cname}_str"

        val type = runReadAction { klass!!.getType(null) } as? VlangStructTypeEx ?: return highlightPointer(value)
        val method = runReadAction { type.findMethod(value.project, "str") } ?: return highlightPointer(value)
        if (!enoughSimpleMethod(method)) return highlightPointer(value)

        val isMutable = runReadAction { method.isMutable }
        // skip mutable methods because they can change the value
        if (isMutable) return highlightPointer(value)

        val byReference = runReadAction { method.byReference() }
        val dereference = if (!byReference) "*" else ""

        val dataIsPointer = value.data.value.startsWith("0x")
        val address = if (!dataIsPointer) value.llValue.address?.toString(16) else value.data.value.removePrefix("0x")
        val argument = "($cname*)(0x$address)"
        val strResult = value.context.evaluate("$strMethodName($dereference$argument)").withContext(value.context)

        val presentableValue = strResult.data.presentableValue
        return value.data
            .withDescription(presentableValue)
            .withHasLongerDescription(needLongerDescription(presentableValue))
    }

    private fun enoughSimpleMethod(method: VlangMethodDeclaration): Boolean {
        val body = runReadAction { method.getBlock() } ?: return false
        val statements = body.statementList
        return statements.size <= 3
    }

    private fun highlightPointer(value: VlangValue): LLValueData = processPointer(value) ?: value.data
}
