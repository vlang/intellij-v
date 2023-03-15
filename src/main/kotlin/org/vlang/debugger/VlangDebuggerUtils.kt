package org.vlang.debugger

import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver.ResultList
import com.jetbrains.cidr.execution.debugger.backend.LLFrame
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData
import org.vlang.debugger.renderers.VlangFakeLLValue
import org.vlang.debugger.renderers.VlangRendererEvaluationContext
import org.vlang.debugger.renderers.VlangValue

fun LLValue.withContext(context: VlangRendererEvaluationContext): VlangValue =
    VlangValue(context, this)

fun LLValue.withName(name: String) =
    LLValue(name, type, displayType, address, typeClass, referenceExpression)
        .also { copyUserDataTo(it) }

fun LLValue.withType(type: String) =
    LLValue(name, type, displayType, address, typeClass, referenceExpression)
        .also { copyUserDataTo(it) }

fun LLValue.withData(name: String? = null, data: LLValueData, children: List<LLValue>? = null, type: String? = null) =
    VlangFakeLLValue(name ?: this.name, type ?: this.type, type ?: displayType, address, typeClass, referenceExpression, data, children)
        .also { copyUserDataTo(it) }

fun LLValueData.withChildren() =
    if (this.mayHaveChildren()) {
        this
    } else {
        LLValueData(this.value, this.description, this.hasLongerDescription(), true, this.isSynthetic)
    }

fun LLValueData.withDescription(desc: String?) =
    LLValueData(this.value, desc, this.hasLongerDescription(), this.mayHaveChildren(), this.isSynthetic)

fun LLValueData.withIsSynthetic(isSynthetic: Boolean) =
    LLValueData(this.value, this.description, this.hasLongerDescription(), this.mayHaveChildren(), isSynthetic)

fun LLValueData.withHasLongerDescription(hasLongerDescription: Boolean) =
    LLValueData(this.value, this.description, hasLongerDescription, this.mayHaveChildren(), isSynthetic)

fun LLValueData.withMayHaveChildren(mayHaveChildren: Boolean) =
    LLValueData(this.value, this.description, this.hasLongerDescription(), mayHaveChildren, isSynthetic)

fun LLValueData.withName(name: String) =
    LLValueData(name, this.description, this.hasLongerDescription(), this.mayHaveChildren(), this.isSynthetic)

fun LLFrame.withFunction(name: String) =
    LLFrame(index, name, file, hash, line, programCounter, language, optimized, inlined, module)

fun <T, K> ResultList<T>.mapItems(mapper: (item: T) -> K): ResultList<K> =
    ResultList.create(
        list.map(mapper),
        hasMore
    )
