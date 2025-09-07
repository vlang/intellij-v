package io.vlang.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.util.ReflectionUtil
import io.vlang.lang.psi.VlangType
import io.vlang.lang.psi.impl.*
import io.vlang.lang.stubs.types.*

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
            put("WRONG_POINTER_TYPE", VlangWrongPointerTypeImpl::class.java)
            put("ANONYMOUS_STRUCT_TYPE", VlangAnonymousStructTypeImpl::class.java)
            put("STRUCT_TYPE", VlangStructTypeImpl::class.java)
            put("TYPE", VlangTypeImpl::class.java)
            put("RESULT_TYPE", VlangResultTypeImpl::class.java)
            put("OPTION_TYPE", VlangOptionTypeImpl::class.java)
            put("ALIAS_TYPE", VlangAliasTypeImpl::class.java)
        }
    }

    val FIELD_DEFINITION = VlangFieldDefinitionStubElementType("FIELD_DEFINITION")
    val ENUM_FIELD_DEFINITION = VlangEnumFieldDefinitionStubElementType("ENUM_FIELD_DEFINITION")
    val FUNCTION_DECLARATION = VlangFunctionDeclarationStubElementType("FUNCTION_DECLARATION")
    val METHOD_DECLARATION = VlangMethodDeclarationStubElementType("METHOD_DECLARATION")
    val STATIC_METHOD_DECLARATION = VlangStaticMethodDeclarationStubElementType("STATIC_METHOD_DECLARATION")
    val STRUCT_DECLARATION = VlangStructDeclarationStubElementType("STRUCT_DECLARATION")
    val ENUM_DECLARATION = VlangEnumDeclarationStubElementType("ENUM_DECLARATION")
    val INTERFACE_DECLARATION = VlangInterfaceDeclarationStubElementType("INTERFACE_DECLARATION")
    val INTERFACE_METHOD_DEFINITION = VlangInterfaceMethodDefinitionStubElementType("INTERFACE_METHOD_DEFINITION")
    val TYPE_ALIAS_DECLARATION = VlangTypeAliasDeclarationStubElementType("TYPE_ALIAS_DECLARATION")
    val IMPORT_NAME = VlangImportNameStubElementType("IMPORT_NAME")
    val IMPORT_ALIAS = VlangImportAliasStubElementType("IMPORT_ALIAS")
    val PARAM_DEFINITION = VlangParamDefinitionStubElementType("PARAM_DEFINITION")
    val RECEIVER = VlangReceiverStubElementType("RECEIVER")
    val CONST_DEFINITION = VlangConstDefinitionStubElementType("CONST_DEFINITION")
    val GLOBAL_VARIABLE_DEFINITION = VlangGlobalVariableDefinitionStubElementType("GLOBAL_VARIABLE_DEFINITION")
    val VAR_DEFINITION = VlangVarDefinitionStubElementType("VAR_DEFINITION")
    val LABEL_DEFINITION = VlangLabelDefinitionStubElementType("LABEL_DEFINITION")
    val GENERIC_PARAMETER = VlangGenericParameterStubElementType("GENERIC_PARAMETER")
    val GENERIC_PARAMETERS = VlangGenericParametersStubElementType("GENERIC_PARAMETERS")
    val SIGNATURE = VlangSignatureStubElementType("SIGNATURE")
    val PARAMETERS = VlangParametersStubElementType("PARAMETERS")
    val RESULT = VlangResultStubElementType("RESULT")
    val TYPE_UNION_LIST = VlangTypeUnionListStubElementType("TYPE_UNION_LIST")
    val EMBEDDED_INTERFACE_DEFINITION = VlangEmbeddedInterfaceDefinitionStubElementType("EMBEDDED_INTERFACE_DEFINITION")
    val TYPE_REFERENCE_EXPRESSION = VlangTypeReferenceExpressionStubElementType("TYPE_REFERENCE_EXPRESSION")
    val ATTRIBUTES = VlangAttributesStubElementType("ATTRIBUTES")
    val ATTRIBUTE = VlangAttributeStubElementType("ATTRIBUTE")
    val ATTRIBUTE_EXPRESSION = VlangAttributeExpressionStubElementType("ATTRIBUTE_EXPRESSION")
    val PLAIN_ATTRIBUTE = VlangPlainAttributeStubElementType("PLAIN_ATTRIBUTE")
    val ATTRIBUTE_KEY = VlangAttributeKeyStubElementType("ATTRIBUTE_KEY")
    val ATTRIBUTE_VALUE = VlangAttributeValueStubElementType("ATTRIBUTE_VALUE")
    val EMBEDDED_DEFINITION = VlangEmbeddedDefinitionStubElementType("EMBEDDED_DEFINITION")

    @JvmStatic
    fun stubFactory(name: String) = when (name) {
        "FIELD_DEFINITION"              -> FIELD_DEFINITION
        "ENUM_FIELD_DEFINITION"         -> ENUM_FIELD_DEFINITION
        "FUNCTION_DECLARATION"          -> FUNCTION_DECLARATION
        "METHOD_DECLARATION"            -> METHOD_DECLARATION
        "STATIC_METHOD_DECLARATION"     -> STATIC_METHOD_DECLARATION
        "STRUCT_DECLARATION"            -> STRUCT_DECLARATION
        "ENUM_DECLARATION"              -> ENUM_DECLARATION
        "INTERFACE_DECLARATION"         -> INTERFACE_DECLARATION
        "INTERFACE_METHOD_DEFINITION"   -> INTERFACE_METHOD_DEFINITION
        "TYPE_ALIAS_DECLARATION"        -> TYPE_ALIAS_DECLARATION
        "IMPORT_NAME"                   -> IMPORT_NAME
        "IMPORT_ALIAS"                  -> IMPORT_ALIAS
        "PARAM_DEFINITION"              -> PARAM_DEFINITION
        "RECEIVER"                      -> RECEIVER
        "CONST_DEFINITION"              -> CONST_DEFINITION
        "GLOBAL_VARIABLE_DEFINITION"    -> GLOBAL_VARIABLE_DEFINITION
        "MODULE_CLAUSE"                 -> VlangModuleClauseStubElementType.INSTANCE
        "VAR_DEFINITION"                -> VAR_DEFINITION
        "LABEL_DEFINITION"              -> LABEL_DEFINITION
        "GENERIC_PARAMETER"             -> GENERIC_PARAMETER
        "GENERIC_PARAMETERS"            -> GENERIC_PARAMETERS
        "SIGNATURE"                     -> SIGNATURE
        "PARAMETERS"                    -> PARAMETERS
        "RESULT"                        -> RESULT
        "TYPE_UNION_LIST"               -> TYPE_UNION_LIST
        "EMBEDDED_INTERFACE_DEFINITION" -> EMBEDDED_INTERFACE_DEFINITION
        "TYPE_REFERENCE_EXPRESSION"     -> TYPE_REFERENCE_EXPRESSION
        "ATTRIBUTES"                    -> ATTRIBUTES
        "ATTRIBUTE"                     -> ATTRIBUTE
        "ATTRIBUTE_EXPRESSION"          -> ATTRIBUTE_EXPRESSION
        "PLAIN_ATTRIBUTE"               -> PLAIN_ATTRIBUTE
        "ATTRIBUTE_KEY"                 -> ATTRIBUTE_KEY
        "ATTRIBUTE_VALUE"               -> ATTRIBUTE_VALUE
        "EMBEDDED_DEFINITION"           -> EMBEDDED_DEFINITION

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
