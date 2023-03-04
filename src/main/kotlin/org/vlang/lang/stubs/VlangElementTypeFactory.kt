package org.vlang.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.util.ReflectionUtil
import org.vlang.lang.psi.VlangType
import org.vlang.lang.psi.impl.*
import org.vlang.lang.stubs.types.*

object VlangElementTypeFactory {
    private val TYPES: Map<String, Class<*>> = object : HashMap<String, Class<*>>() {
        init {
            put("ARRAY_TYPE", VlangArrayTypeImpl::class.java)
            put("FIXED_SIZE_ARRAY_TYPE", VlangFixedSizeArrayTypeImpl::class.java)
            put("TUPLE_TYPE", VlangTupleTypeImpl::class.java)
            put("CHANNEL_TYPE", VlangChannelTypeImpl::class.java)
            put("THREAD_TYPE", VlangThreadTypeImpl::class.java)
            put("ATOMIC_TYPE", VlangAtomicTypeImpl::class.java)
            put("FUNCTION_TYPE", VlangFunctionTypeImpl::class.java)
            put("INTERFACE_TYPE", VlangInterfaceTypeImpl::class.java)
            put("ENUM_TYPE", VlangEnumTypeImpl::class.java)
            put("MAP_TYPE", VlangMapTypeImpl::class.java)
            put("SHARED_TYPE", VlangSharedTypeImpl::class.java)
            put("NONE_TYPE", VlangNoneTypeImpl::class.java)
            put("POINTER_TYPE", VlangPointerTypeImpl::class.java)
            put("WRONG_POINTER_TYPE", VlangPointerTypeImpl::class.java)
            put("ANONYMOUS_STRUCT_TYPE", VlangAnonymousStructTypeImpl::class.java)
            put("STRUCT_TYPE", VlangStructTypeImpl::class.java)
            put("TYPE", VlangTypeImpl::class.java)
            put("RESULT_TYPE", VlangResultTypeImpl::class.java)
            put("OPTION_TYPE", VlangOptionTypeImpl::class.java)
            put("ALIAS_TYPE", VlangAliasTypeImpl::class.java)
        }
    }

    @JvmStatic
    fun stubFactory(name: String) = when (name) {
        "FIELD_DEFINITION"              -> VlangFieldDefinitionStubElementType(name)
        "ENUM_FIELD_DEFINITION"         -> VlangEnumFieldDefinitionStubElementType(name)
        "FUNCTION_DECLARATION"          -> VlangFunctionDeclarationStubElementType(name)
        "METHOD_DECLARATION"            -> VlangMethodDeclarationStubElementType(name)
        "STRUCT_DECLARATION"            -> VlangStructDeclarationStubElementType(name)
        "ENUM_DECLARATION"              -> VlangEnumDeclarationStubElementType(name)
        "INTERFACE_DECLARATION"         -> VlangInterfaceDeclarationStubElementType(name)
        "INTERFACE_METHOD_DEFINITION"   -> VlangInterfaceMethodDefinitionStubElementType(name)
        "TYPE_ALIAS_DECLARATION"        -> VlangTypeAliasDeclarationStubElementType(name)
        "IMPORT_NAME"                   -> VlangImportNameStubElementType(name)
        "IMPORT_ALIAS"                  -> VlangImportAliasStubElementType(name)
        "PARAM_DEFINITION"              -> VlangParamDefinitionStubElementType(name)
        "RECEIVER"                      -> VlangReceiverStubElementType(name)
        "CONST_DEFINITION"              -> VlangConstDefinitionStubElementType(name)
        "GLOBAL_VARIABLE_DEFINITION"    -> VlangGlobalVariableDefinitionStubElementType(name)
        "MODULE_CLAUSE"                 -> VlangModuleClauseStubElementType.INSTANCE
        "VAR_DEFINITION"                -> VlangVarDefinitionStubElementType(name)
        "LABEL_DEFINITION"              -> VlangLabelDefinitionStubElementType(name)
        "GENERIC_PARAMETER"             -> VlangGenericParameterStubElementType(name)
        "GENERIC_PARAMETERS"            -> VlangGenericParametersStubElementType(name)
        "SIGNATURE"                     -> VlangSignatureStubElementType(name)
        "PARAMETERS"                    -> VlangParametersStubElementType(name)
        "RESULT"                        -> VlangResultStubElementType(name)
        "TYPE_UNION_LIST"               -> VlangTypeUnionListStubElementType(name)
        "EMBEDDED_INTERFACE_DEFINITION" -> VlangEmbeddedInterfaceDefinitionStubElementType(name)
        "TYPE_REFERENCE_EXPRESSION"     -> VlangTypeReferenceExpressionStubElementType(name)
        "ATTRIBUTES"                    -> VlangAttributesStubElementType(name)
        "ATTRIBUTE"                     -> VlangAttributeStubElementType(name)
        "ATTRIBUTE_EXPRESSION"          -> VlangAttributeExpressionStubElementType(name)
        "PLAIN_ATTRIBUTE"               -> VlangPlainAttributeStubElementType(name)
        "ATTRIBUTE_KEY"                 -> VlangAttributeKeyStubElementType(name)
        "ATTRIBUTE_VALUE"               -> VlangAttributeValueStubElementType(name)
        "EMBEDDED_DEFINITION"           -> VlangEmbeddedDefinitionStubElementType(name)

        else                            -> {
            val c = TYPES[name] ?: throw RuntimeException("Unknown element type: $name")

            object : VlangTypeStubElementType(name) {
                override fun createPsi(stub: VlangTypeStub): VlangType {
                    return try {
                        ReflectionUtil.createInstance(
                            c.getConstructor(stub::class.java, IStubElementType::class.java),
                            stub,
                            this
                        ) as VlangType
                    } catch (e: NoSuchMethodException) {
                        throw RuntimeException(e)
                    }
                }
            }
        }
    }
}