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

        fun VlangTypeEx.unwrapPointer(): VlangTypeEx {
            if (this is VlangPointerTypeEx) {
                return this.inner
            }
            return this
        }

        fun VlangType?.toEx(visited: MutableMap<VlangType, VlangTypeEx> = mutableMapOf()): VlangTypeEx {
            if (this == null) {
                return VlangUnknownTypeEx.INSTANCE
            }

            if (this in visited) {
                return visited[this]!!
            }

            val type = toExInner(visited)

            if (genericArguments != null) {
                val genericArguments = genericArguments!!.typeArguments.map { it.toEx() }
                val instantiationType = VlangGenericInstantiationEx(type, genericArguments, this)
                visited[this] = instantiationType
                return instantiationType
            }

            visited[this] = type
            return type
        }

        private fun VlangType.toExInner(visited: MutableMap<VlangType, VlangTypeEx>): VlangTypeEx {
            val type = resolveType()
            if (type is VlangStructType && type.parent is VlangStructDeclaration) {
                return when ((type.parent as VlangStructDeclaration).getQualifiedName()) {
                    "builtin.array"            -> VlangBuiltinArrayTypeEx.INSTANCE
                    "builtin.map"              -> VlangBuiltinMapTypeEx.INSTANCE
                    "builtin.string"           -> VlangStringTypeEx.INSTANCE
                    "stub.UnknownCDeclaration" -> VlangStructTypeEx.UnknownCDeclarationStruct
                    else                       -> {
                        if (type.isUnion) VlangUnionTypeEx(parentName(type), type)
                        else VlangStructTypeEx(parentName(type), type)
                    }
                }
            }

            return when (type) {
                is VlangEnumType            -> VlangEnumTypeEx(parentName(type), type)
                is VlangInterfaceType       -> VlangInterfaceTypeEx(parentName(type), type)
                is VlangOptionType          -> VlangOptionTypeEx(type.type?.toEx(visited), type)
                is VlangResultType          -> VlangResultTypeEx(type.type?.toEx(visited), type)
                is VlangThreadType          -> VlangThreadTypeEx(type.type?.toEx(visited) ?: VlangVoidTypeEx.INSTANCE, type)
                is VlangSharedType          -> VlangSharedTypeEx(type.type.toEx(visited), type)
                is VlangPointerType         -> VlangPointerTypeEx(type.type.toEx(visited), type)
                is VlangArrayType           -> VlangArrayTypeEx(type.type.toEx(visited), type)
                is VlangFixedSizeArrayType  -> VlangFixedSizeArrayTypeEx(type.type.toEx(visited), type.size, type)
                is VlangChannelType         -> VlangChannelTypeEx(type.type.toEx(visited), type)
                is VlangMapType             -> VlangMapTypeEx(type.keyType.toEx(visited), type.valueType.toEx(visited), type)
                is VlangTupleType           -> VlangTupleTypeEx(type.typeListNoPin.typeList.map { it.toEx(visited) }, type)
                is VlangGenericType         -> VlangGenericTypeEx(type.name, type)
                is VlangAnonymousStructType -> VlangAnonStructTypeEx(type)
                is VlangFunctionType        -> VlangFunctionTypeEx.from(type) ?: VlangUnknownTypeEx.INSTANCE
                is VlangAliasType           -> {
                    val typeName = parentName(type)

                    val declaration = type.parent as VlangNamedElement
                    // special type from stubs
                    if (declaration.getQualifiedName() == "stubs.Any") {
                        return VlangAliasTypeEx.anyType(type)
                    }

                    if (type.typeUnionList != null && type.typeUnionList!!.typeList.size > 1) {
                        visited[this] = VlangSumTypeEx("<temp>", emptyList(), type)
                        val types = type.typeUnionList!!.typeList.map { it.toEx(visited) }
                        return VlangSumTypeEx(typeName, types, type)
                    }

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

                    VlangAliasTypeEx(typeName, firstType.toEx(visited), type)
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
