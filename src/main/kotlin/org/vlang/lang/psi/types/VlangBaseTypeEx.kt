package org.vlang.lang.psi.types

import org.vlang.lang.psi.*

@Suppress("PropertyName")
abstract class VlangBaseTypeEx<T : VlangType?>(protected val raw: T) : VlangTypeEx<T> {
    protected val UNKNOWN_TYPE = "unknown"
    protected val ANON = "anon"
    protected val moduleName = (raw?.containingFile as? VlangFile)?.getModuleQualifiedName() ?: ""

    override fun raw() = raw

    override fun module() = moduleName

    protected fun String?.safeAppend(str: String?): String {
        return if (this == null) str ?: "" else this + str
    }

    protected fun String?.safeAppend(type: VlangTypeEx<*>?): String {
        return this.safeAppend(type?.toString())
    }

    protected fun VlangTypeEx<*>?.safeAppend(str: String): String {
        return this?.toString().safeAppend(str)
    }

    companion object {
        protected val primitivesMap = VlangPrimitiveTypes.values().associateBy { it.value }

        fun VlangType?.toEx(): VlangTypeEx<*> {
            if (this == null) {
                return VlangUnknownTypeEx(null)
            }

            return when (val type = resolveType()) {
                is VlangStructType       -> VlangStructTypeEx(type)
                is VlangEnumType         -> VlangEnumTypeEx(type)
                is VlangInterfaceType    -> VlangInterfaceTypeEx(type)
                is VlangUnionType        -> VlangUnionTypeEx(type)
                is VlangNullableType     -> VlangNullableTypeEx(type)
                is VlangNotNullableType  -> VlangNotNullableTypeEx(type)
                is VlangPointerType      -> VlangPointerTypeEx(type)
                is VlangArrayOrSliceType -> VlangArrayTypeEx(type)
                is VlangMapType          -> VlangMapTypeEx(type)
                is VlangTupleType        -> VlangTupleTypeEx(type)
                is VlangFunctionType     -> VlangFunctionTypeEx(type)
                is VlangAliasType        -> VlangAliasTypeEx(type)
                else                     -> {
                    val primitive = primitivesMap[type.text]
                    if (primitive != null) {
                        VlangPrimitiveTypeEx(type, primitive)
                    } else {
                        VlangUnknownTypeEx(type)
                    }
                }
            }
        }
    }
}
