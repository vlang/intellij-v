package org.vlang.ide.codeInsight

import org.vlang.lang.psi.types.*
import java.lang.Integer.min

class VlangGenericReifier {
    val implicitSpecs = mutableListOf<VlangTypeEx>()
    val implicitSpecializationNameMap = mutableMapOf<String, VlangTypeEx>()
    val implicitClassSpecializationNameMap = mutableMapOf<String, VlangTypeEx>()
    val implicitSpecializationErrors = mutableMapOf<String, Pair<VlangTypeEx, VlangTypeEx>>()

    fun reifyGenericTs(parametersTypes: List<VlangTypeEx?>, argumentTypes: List<VlangTypeEx?>) {
        for (i in 0 until min(parametersTypes.size, argumentTypes.size)) {
            reifyGenericT(argumentTypes[i], parametersTypes[i])
        }

        implicitSpecs.addAll(implicitSpecializationNameMap.values)
    }

    private fun reifyGenericT(argType: VlangTypeEx?, paramType: VlangTypeEx?) {
        if (paramType == null || argType == null) return

        if (paramType is VlangGenericTypeEx) {
            implicitSpecializationNameMap[paramType.name()] = argType
        }

        if (paramType is VlangGenericInstantiationEx) {
            if (argType is VlangGenericInstantiationEx) {
                for (i in 0 until min(paramType.specialization.size, argType.specialization.size)) {
                    reifyGenericT(argType.specialization[i], paramType.specialization[i])
                }
            }
        }

        if (paramType is VlangResultTypeEx) {
            if (argType is VlangResultTypeEx) {
                reifyGenericT(argType.inner, paramType.inner)
            } else {
                reifyGenericT(argType, paramType.inner)
            }
        }

        if (paramType is VlangOptionTypeEx) {
            if (argType is VlangOptionTypeEx) {
                reifyGenericT(argType.inner, paramType.inner)
            } else {
                reifyGenericT(argType, paramType.inner)
            }
        }

        if (paramType is VlangPointerTypeEx) {
            if (argType is VlangPointerTypeEx) {
                reifyGenericT(argType.inner, paramType.inner)
            } else {
                reifyGenericT(argType, paramType.inner)
            }
        }

        if (paramType is VlangSharedTypeEx) {
            if (argType is VlangSharedTypeEx) {
                reifyGenericT(argType.inner, paramType.inner)
            } else {
                reifyGenericT(argType, paramType.inner)
            }
        }

        if (paramType is VlangChannelTypeEx) {
            if (argType is VlangChannelTypeEx) {
                reifyGenericT(argType.inner, paramType.inner)
            }
        }

        if (paramType is VlangArrayTypeEx) {
            if (argType is VlangArrayTypeEx) {
                reifyGenericT(argType.inner, paramType.inner)
            }
        }

        if (paramType is VlangMapTypeEx) {
            if (argType is VlangMapTypeEx) {
                reifyGenericT(argType.key, paramType.key)
                reifyGenericT(argType.value, paramType.value)
            }
        }

        if (paramType is VlangTupleTypeEx) {
            if (argType is VlangTupleTypeEx) {
                for (i in 0 until min(paramType.types.size, argType.types.size)) {
                    reifyGenericT(argType.types[i], paramType.types[i])
                }
            }
        }

        if (paramType is VlangFunctionTypeEx) {
            if (argType is VlangFunctionTypeEx) {
                for (i in 0 until min(paramType.params.size, argType.params.size)) {
                    reifyGenericT(argType.params[i], paramType.params[i])
                }
                reifyGenericT(argType.result, paramType.result)
            }
        }
    }
}
