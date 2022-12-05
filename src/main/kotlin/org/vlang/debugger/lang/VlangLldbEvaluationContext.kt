package org.vlang.debugger.lang

import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Expirable
import com.intellij.openapi.util.UserDataHolderEx
import com.jetbrains.cidr.execution.debugger.backend.*
import com.jetbrains.cidr.execution.debugger.backend.lldb.LLDBEvaluationContext
import org.vlang.debugger.mapItems
import org.vlang.debugger.renderers.*
import org.vlang.debugger.withContext
import org.vlang.lang.psi.VlangEnumDeclaration
import org.vlang.lang.stubs.index.VlangClassLikeIndex

class VlangLldbEvaluationContext(
    val project: Project,
    driver: DebuggerDriver,
    expirable: Expirable?,
    thread: LLThread,
    frame: LLFrame,
    cacheHolder: UserDataHolderEx,
) : LLDBEvaluationContext(driver, expirable, thread, frame, cacheHolder) {

    fun findRenderer(value: LLValue): VlangValueRenderer? =
        VlangValueRenderer.EP_NAME.extensionList.find { it.isApplicable(project, value) }

    override fun getData(value: LLValue): LLValueData {
        val renderer = findRenderer(value)
            ?: return getRawData(value)
        return renderer.getData(value.withContext(VlangRendererEvaluationContext(this, value)))
    }

    /**
     * @param size array size, default 50, see cidr.debugger.value.maxChildren key
     */
    override fun getVariableChildren(value: LLValue, offset: Int, size: Int): DebuggerDriver.ResultList<LLValue> {
        val renderer = findRenderer(value)
            ?: return getRawVariableChildren(value, offset, size)
        return renderer.getVariableChildren(value.withContext(VlangRendererEvaluationContext(this, value)), offset, size)
            .mapItems { it.llValue }
    }

    fun getRawData(llValue: LLValue): LLValueData = (llValue as? VlangFakeLLValue)?.data
        ?: super.getData(llValue)

    fun getRawVariableChildren(llValue: LLValue, offset: Int, size: Int) =
        (llValue as? VlangFakeLLValue)?.children
            ?.let { DebuggerDriver.ResultList.create(it.subList(offset, Integer.min(offset + size, it.size)), offset + size < it.size) }
            ?: super.getVariableChildren(llValue, offset, size)
}
