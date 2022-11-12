package org.vlang.lang.psi.types

import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.*

@Suppress("PropertyName")
abstract class VlangBaseTypeEx<T : VlangType?>(protected val raw: T) : VlangTypeEx<T> {
    protected val UNKNOWN_TYPE = "unknown"
    protected val ANON = "anon"
    protected val containingFile = raw?.containingFile as? VlangFile
    protected val moduleName = containingFile?.getModuleQualifiedName() ?: ""

    override fun raw() = raw

    override fun module() = moduleName

    override fun name(): String {
        return qualifiedName().removePrefix(moduleName).removePrefix(".")
    }

    override fun isBuiltin() = moduleName == VlangCodeInsightUtil.BUILTIN_MODULE

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

            val type = resolveType()
            if (type is VlangStructType && type.parent is VlangStructDeclaration) {
                return when ((type.parent as VlangStructDeclaration).getQualifiedName()) {
                    "builtin.array"  -> VlangBuiltinArrayTypeEx(type)
                    "builtin.string" -> VlangBuiltinStringTypeEx(type)
                    else             -> if (type.isUnion) VlangUnionTypeEx(type) else VlangStructTypeEx(type)
                }
            }

            return when (type) {
                is VlangEnumType        -> VlangEnumTypeEx(type)
                is VlangInterfaceType   -> VlangInterfaceTypeEx(type)
                is VlangNullableType    -> VlangNullableTypeEx(type)
                is VlangNotNullableType -> VlangNotNullableTypeEx(type)
                is VlangSharedType      -> VlangSharedTypeEx(type)
                is VlangPointerType     -> VlangPointerTypeEx(type)
                is VlangArrayType       -> VlangArrayTypeEx(type)
                is VlangMapType         -> VlangMapTypeEx(type)
                is VlangTupleType       -> VlangTupleTypeEx(type)
                is VlangFunctionType    -> VlangFunctionTypeEx(type)
                is VlangAliasType       -> {
                    if (type.isAlias) {
                        VlangAliasTypeEx(type)
                    } else {
                        VlangSumTypeEx(type)
                    }
                }

                else                    -> {
                    if (type.text == "any") {
                        return VlangAnyTypeEx(this)
                    }
                    if (type.text == "void") {
                        return VlangVoidTypeEx(this)
                    }
                    if (type.text == "voidptr") {
                        return VlangVoidPtrTypeEx(this)
                    }

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
