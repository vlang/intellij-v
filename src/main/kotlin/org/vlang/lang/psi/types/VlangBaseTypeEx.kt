package org.vlang.lang.psi.types

import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.psi.PsiElement
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangLightType.VlangGenericType

@Suppress("PropertyName")
abstract class VlangBaseTypeEx(protected val anchor: PsiElement? = null) : UserDataHolderBase(), VlangTypeEx {
    protected val UNKNOWN_TYPE = "unknown"
    protected val ANON = "anon"
    protected val containingFile = anchor?.containingFile as? VlangFile
    protected open val moduleName = containingFile?.getModuleQualifiedName() ?: ""

    override fun anchor() = anchor

    override fun module() = moduleName

    override fun name(): String {
        return qualifiedName().removePrefix(moduleName).removePrefix(".")
    }

    override fun isBuiltin() = moduleName == VlangCodeInsightUtil.BUILTIN_MODULE || moduleName == VlangCodeInsightUtil.STUBS_MODULE

    protected fun String?.safeAppend(str: String?): String {
        return if (this == null) str ?: "" else this + str
    }

    protected fun String?.safeAppend(type: VlangTypeEx?): String {
        return this.safeAppend(type?.toString())
    }

    protected fun VlangTypeEx?.safeAppend(str: String): String {
        return this?.toString().safeAppend(str)
    }

    companion object {
        protected val primitivesMap = VlangPrimitiveTypes.values().associateBy { it.value }

        fun VlangTypeEx.isGeneric(): Boolean {
            var isGeneric = false
            accept(object : VlangTypeVisitor {
                override fun enter(type: VlangTypeEx): Boolean {
                    if (type is VlangGenericTypeEx) {
                        isGeneric = true
                        return false
                    }
                    return true
                }
            })
            return isGeneric
        }

        fun VlangTypeEx.getGenericTs(): Set<String> {
            val genericTs = mutableSetOf<String>()
            accept(object : VlangTypeVisitor {
                override fun enter(type: VlangTypeEx): Boolean {
                    if (type is VlangGenericTypeEx) {
                        genericTs.add(type.name())
                        return false
                    }
                    return true
                }
            })
            return genericTs
        }

        fun VlangType?.toEx(): VlangTypeEx {
            if (this == null) {
                return VlangUnknownTypeEx.INSTANCE
            }

            val type = toExInner()

            if (genericArguments != null) {
                val genericArguments = genericArguments!!.typeArguments.map { it.toEx() }
                return VlangGenericInstantiationEx(type, genericArguments, this)
            }

            return type
        }

        private fun VlangType.toExInner(): VlangTypeEx {
            val type = resolveType()
            if (type is VlangStructType && type.parent is VlangStructDeclaration) {
                return when ((type.parent as VlangStructDeclaration).getQualifiedName()) {
                    "builtin.array"  -> VlangBuiltinArrayTypeEx.INSTANCE
                    "builtin.map"    -> VlangBuiltinMapTypeEx.INSTANCE
                    "builtin.string" -> VlangStringTypeEx.INSTANCE
                    else             -> {
                        if (type.isUnion) VlangUnionTypeEx(parentName(type), type)
                        else VlangStructTypeEx(parentName(type), type)
                    }
                }
            }

            return when (type) {
                is VlangEnumType            -> VlangEnumTypeEx(parentName(type), type)
                is VlangInterfaceType       -> VlangInterfaceTypeEx(parentName(type), type)
                is VlangOptionType          -> VlangOptionTypeEx(type.type?.toEx(), type)
                is VlangResultType          -> VlangResultTypeEx(type.type?.toEx(), type)
                is VlangThreadType          -> VlangThreadTypeEx(type.type?.toEx() ?: VlangVoidTypeEx.INSTANCE, type)
                is VlangSharedType          -> VlangSharedTypeEx(type.type.toEx(), type)
                is VlangPointerType         -> VlangPointerTypeEx(type.type.toEx(), type)
                is VlangArrayType           -> VlangArrayTypeEx(type.type.toEx(), type)
                is VlangFixedSizeArrayType  -> VlangFixedSizeArrayTypeEx(type.type.toEx(), type.size, type)
                is VlangChannelType         -> VlangChannelTypeEx(type.type.toEx(), type)
                is VlangMapType             -> VlangMapTypeEx(type.keyType.toEx(), type.valueType.toEx(), type)
                is VlangTupleType           -> VlangTupleTypeEx(type.typeListNoPin.typeList.map { it.toEx() }, type)
                is VlangGenericType         -> VlangGenericTypeEx(type.name, type)
                is VlangAnonymousStructType -> VlangAnonStructTypeEx(type)
                is VlangFunctionType        -> VlangFunctionTypeEx.from(type) ?: VlangUnknownTypeEx.INSTANCE
                is VlangAliasType           -> {
                    if (type.typeUnionList != null && type.typeUnionList!!.typeList.size > 1) {
                        return VlangUnknownTypeEx.INSTANCE
                    }

                    val typeName = parentName(type)

                    if (typeName == "any") {
                        return VlangAnyTypeEx.INSTANCE
                    }
                    if (typeName == "voidptr") {
                        return VlangVoidPtrTypeEx.INSTANCE
                    }

                    val primitive = primitivesMap[typeName]
                    if (primitive != null) {
                        return VlangPrimitiveTypeEx(primitive)
                    }

                    val firstType = type.typeUnionList?.typeList?.firstOrNull()
                    if (firstType?.text == typeName) {
                        // stubs case:
                        //   type i32 = i32
                        return VlangAliasTypeEx(typeName, VlangUnknownTypeEx.INSTANCE, type)
                    }

                    // hack for now for
                    // type Some = []Some
                    if (firstType != null && firstType.text.contains(typeName)) {
                        return VlangUnknownTypeEx.INSTANCE
                    }

                    // TODO: Sum types.
                    VlangAliasTypeEx(typeName, firstType.toEx(), type)
                }

                else                        -> {
                    if (type.text == "void") {
                        return VlangVoidTypeEx.INSTANCE
                    }

                    // only for tests
                    // TODO: remove
                    if (type.text == "string") {
                        return VlangStringTypeEx.INSTANCE
                    }
                    if (type.text == "array") {
                        return VlangBuiltinArrayTypeEx.INSTANCE
                    }
                    if (type.text == "map") {
                        return VlangBuiltinMapTypeEx.INSTANCE
                    }

                    VlangUnknownTypeEx.INSTANCE
                }
            }
        }

        private fun parentName(type: PsiElement) = (type.parent as VlangNamedElement).name!!
    }
}
