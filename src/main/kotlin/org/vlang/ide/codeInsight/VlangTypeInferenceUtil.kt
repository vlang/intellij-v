package org.vlang.ide.codeInsight

import org.vlang.lang.psi.VlangCallExpr
import org.vlang.lang.psi.VlangReferenceExpression
import org.vlang.lang.psi.VlangType
import org.vlang.lang.psi.types.VlangBuiltinArrayTypeEx
import org.vlang.lang.psi.types.VlangPointerTypeEx
import org.vlang.lang.psi.types.VlangTypeEx

object VlangTypeInferenceUtil {
    const val ARRAY_MAP_METHOD = "map"

    fun callerType(call: VlangCallExpr): VlangType? {
        val callExpression = call.expression as? VlangReferenceExpression ?: return null
        val caller = callExpression.getQualifier() as? VlangReferenceExpression ?: return null
        return caller.getType(null)
    }

    fun builtInArrayOrPointerTo(type: VlangTypeEx<*>): Boolean {
        if (type is VlangBuiltinArrayTypeEx) return true
        return type is VlangPointerTypeEx && type.inner is VlangBuiltinArrayTypeEx
    }
}
