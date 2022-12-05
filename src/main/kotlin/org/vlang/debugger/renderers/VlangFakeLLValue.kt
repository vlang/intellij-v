package org.vlang.debugger.renderers

import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData

class VlangFakeLLValue(
    name: String,
    type: String,
    displayType: String,
    address: Long?,
    typeClass: TypeClass?,
    referenceExpression: String,
    val data: LLValueData,
    val children: List<LLValue>? = null
) : LLValue(name, type, displayType, address, typeClass, referenceExpression)