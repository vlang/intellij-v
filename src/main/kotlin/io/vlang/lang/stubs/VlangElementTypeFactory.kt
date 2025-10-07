package io.vlang.lang.stubs

import io.vlang.lang.psi.VlangType
import io.vlang.lang.psi.impl.*
import io.vlang.lang.stubs.types.*
import kotlin.reflect.KClass

object VlangElementTypeFactory {

    val ARRAY_TYPE = VlangGenericStubElementType(VlangArrayTypeImpl::class, "ARRAY_TYPE")
    val ALIAS_TYPE = VlangGenericStubElementType(VlangAliasTypeImpl::class, "ALIAS_TYPE")
    val ANONYMOUS_STRUCT_TYPE = VlangGenericStubElementType(VlangAnonymousStructTypeImpl::class, "ANONYMOUS_STRUCT_TYPE")
    val ATOMIC_TYPE = VlangGenericStubElementType(VlangAtomicTypeImpl::class, "ATOMIC_TYPE")
    val ATTRIBUTE = VlangAttributeStubElementType("ATTRIBUTE")
    val ATTRIBUTE_EXPRESSION = VlangAttributeExpressionStubElementType("ATTRIBUTE_EXPRESSION")
    val ATTRIBUTE_KEY = VlangAttributeKeyStubElementType("ATTRIBUTE_KEY")
    val ATTRIBUTE_VALUE = VlangAttributeValueStubElementType("ATTRIBUTE_VALUE")
    val ATTRIBUTES = VlangAttributesStubElementType("ATTRIBUTES")
    val CHANNEL_TYPE = VlangGenericStubElementType(VlangChannelTypeImpl::class, "CHANNEL_TYPE")
    val CONST_DEFINITION = VlangConstDefinitionStubElementType("CONST_DEFINITION")
    val EMBEDDED_DEFINITION = VlangEmbeddedDefinitionStubElementType("EMBEDDED_DEFINITION")
    val EMBEDDED_INTERFACE_DEFINITION = VlangEmbeddedInterfaceDefinitionStubElementType("EMBEDDED_INTERFACE_DEFINITION")
    val ENUM_DECLARATION = VlangEnumDeclarationStubElementType("ENUM_DECLARATION")
    val ENUM_FIELD_DEFINITION = VlangEnumFieldDefinitionStubElementType("ENUM_FIELD_DEFINITION")
    val ENUM_TYPE = VlangGenericStubElementType(VlangEnumTypeImpl::class, "ENUM_TYPE")
    val FIELD_DEFINITION = VlangFieldDefinitionStubElementType("FIELD_DEFINITION")
    val FIXED_SIZE_ARRAY_TYPE = VlangGenericStubElementType(VlangFixedSizeArrayTypeImpl::class, "FIXED_SIZE_ARRAY_TYPE")
    val FUNCTION_DECLARATION = VlangFunctionDeclarationStubElementType("FUNCTION_DECLARATION")
    val FUNCTION_TYPE = VlangGenericStubElementType(VlangFunctionTypeImpl::class, "FUNCTION_TYPE")
    val GENERIC_PARAMETER = VlangGenericParameterStubElementType("GENERIC_PARAMETER")
    val GENERIC_PARAMETERS = VlangGenericParametersStubElementType("GENERIC_PARAMETERS")
    val GLOBAL_VARIABLE_DEFINITION = VlangGlobalVariableDefinitionStubElementType("GLOBAL_VARIABLE_DEFINITION")
    val IMPORT_ALIAS = VlangImportAliasStubElementType("IMPORT_ALIAS")
    val IMPORT_NAME = VlangImportNameStubElementType("IMPORT_NAME")
    val INTERFACE_DECLARATION = VlangInterfaceDeclarationStubElementType("INTERFACE_DECLARATION")
    val INTERFACE_METHOD_DEFINITION = VlangInterfaceMethodDefinitionStubElementType("INTERFACE_METHOD_DEFINITION")
    val INTERFACE_TYPE = VlangGenericStubElementType(VlangInterfaceTypeImpl::class, "INTERFACE_TYPE")
    val LABEL_DEFINITION = VlangLabelDefinitionStubElementType("LABEL_DEFINITION")
    val MAP_TYPE = VlangGenericStubElementType(VlangMapTypeImpl::class, "MAP_TYPE")
    val METHOD_DECLARATION = VlangMethodDeclarationStubElementType("METHOD_DECLARATION")
    val MODULE_CLAUSE = VlangModuleClauseStubElementType()
    val NONE_TYPE = VlangGenericStubElementType(VlangNoneTypeImpl::class, "NONE_TYPE")
    val OPTION_TYPE = VlangGenericStubElementType(VlangOptionTypeImpl::class, "OPTION_TYPE")
    val PARAM_DEFINITION = VlangParamDefinitionStubElementType("PARAM_DEFINITION")
    val PARAMETERS = VlangParametersStubElementType("PARAMETERS")
    val PLAIN_ATTRIBUTE = VlangPlainAttributeStubElementType("PLAIN_ATTRIBUTE")
    val POINTER_TYPE = VlangGenericStubElementType(VlangPointerTypeImpl::class, "POINTER_TYPE")
    val RECEIVER = VlangReceiverStubElementType("RECEIVER")
    val RESULT = VlangResultStubElementType("RESULT")
    val RESULT_TYPE = VlangGenericStubElementType(VlangResultTypeImpl::class, "RESULT_TYPE")
    val SHARED_TYPE = VlangGenericStubElementType(VlangSharedTypeImpl::class, "SHARED_TYPE")
    val SIGNATURE = VlangSignatureStubElementType("SIGNATURE")
    val STATIC_METHOD_DECLARATION = VlangStaticMethodDeclarationStubElementType("STATIC_METHOD_DECLARATION")
    val STRUCT_DECLARATION = VlangStructDeclarationStubElementType("STRUCT_DECLARATION")
    val STRUCT_TYPE = VlangGenericStubElementType(VlangStructTypeImpl::class, "STRUCT_TYPE")
    val THREAD_TYPE = VlangGenericStubElementType(VlangThreadTypeImpl::class, "THREAD_TYPE")
    val TUPLE_TYPE = VlangGenericStubElementType(VlangTupleTypeImpl::class, "TUPLE_TYPE")
    val TYPE = VlangGenericStubElementType(VlangTypeImpl::class, "TYPE")
    val TYPE_ALIAS_DECLARATION = VlangTypeAliasDeclarationStubElementType("TYPE_ALIAS_DECLARATION")
    val TYPE_REFERENCE_EXPRESSION = VlangTypeReferenceExpressionStubElementType("TYPE_REFERENCE_EXPRESSION")
    val TYPE_UNION_LIST = VlangTypeUnionListStubElementType("TYPE_UNION_LIST")
    val VAR_DEFINITION = VlangVarDefinitionStubElementType("VAR_DEFINITION")
    val WRONG_POINTER_TYPE = VlangGenericStubElementType(VlangWrongPointerTypeImpl::class, "WRONG_POINTER_TYPE")

    @JvmStatic
    fun stubFactory(name: String) = when (name) {
        "ALIAS_TYPE"                    -> ALIAS_TYPE
        "ANONYMOUS_STRUCT_TYPE"         -> ANONYMOUS_STRUCT_TYPE
        "ARRAY_TYPE"                    -> ARRAY_TYPE
        "ATOMIC_TYPE"                   -> ATOMIC_TYPE
        "ATTRIBUTE"                     -> ATTRIBUTE
        "ATTRIBUTE_EXPRESSION"          -> ATTRIBUTE_EXPRESSION
        "ATTRIBUTE_KEY"                 -> ATTRIBUTE_KEY
        "ATTRIBUTE_VALUE"               -> ATTRIBUTE_VALUE
        "ATTRIBUTES"                    -> ATTRIBUTES
        "CHANNEL_TYPE"                  -> CHANNEL_TYPE
        "CONST_DEFINITION"              -> CONST_DEFINITION
        "EMBEDDED_DEFINITION"           -> EMBEDDED_DEFINITION
        "EMBEDDED_INTERFACE_DEFINITION" -> EMBEDDED_INTERFACE_DEFINITION
        "ENUM_DECLARATION"              -> ENUM_DECLARATION
        "ENUM_FIELD_DEFINITION"         -> ENUM_FIELD_DEFINITION
        "ENUM_TYPE"                     -> ENUM_TYPE
        "FIELD_DEFINITION"              -> FIELD_DEFINITION
        "FIXED_SIZE_ARRAY_TYPE"         -> FIXED_SIZE_ARRAY_TYPE
        "FUNCTION_DECLARATION"          -> FUNCTION_DECLARATION
        "FUNCTION_TYPE"                 -> FUNCTION_TYPE
        "GENERIC_PARAMETER"             -> GENERIC_PARAMETER
        "GENERIC_PARAMETERS"            -> GENERIC_PARAMETERS
        "GLOBAL_VARIABLE_DEFINITION"    -> GLOBAL_VARIABLE_DEFINITION
        "IMPORT_ALIAS"                  -> IMPORT_ALIAS
        "IMPORT_NAME"                   -> IMPORT_NAME
        "INTERFACE_DECLARATION"         -> INTERFACE_DECLARATION
        "INTERFACE_METHOD_DEFINITION"   -> INTERFACE_METHOD_DEFINITION
        "INTERFACE_TYPE"                -> INTERFACE_TYPE
        "LABEL_DEFINITION"              -> LABEL_DEFINITION
        "MAP_TYPE"                      -> MAP_TYPE
        "METHOD_DECLARATION"            -> METHOD_DECLARATION
        "MODULE_CLAUSE"                 -> MODULE_CLAUSE
        "NONE_TYPE"                     -> NONE_TYPE
        "OPTION_TYPE"                   -> OPTION_TYPE
        "PARAM_DEFINITION"              -> PARAM_DEFINITION
        "PARAMETERS"                    -> PARAMETERS
        "PLAIN_ATTRIBUTE"               -> PLAIN_ATTRIBUTE
        "POINTER_TYPE"                  -> POINTER_TYPE
        "RECEIVER"                      -> RECEIVER
        "RESULT"                        -> RESULT
        "RESULT_TYPE"                   -> RESULT_TYPE
        "SHARED_TYPE"                   -> SHARED_TYPE
        "SIGNATURE"                     -> SIGNATURE
        "STATIC_METHOD_DECLARATION"     -> STATIC_METHOD_DECLARATION
        "STRUCT_DECLARATION"            -> STRUCT_DECLARATION
        "STRUCT_TYPE"                   -> STRUCT_TYPE
        "THREAD_TYPE"                   -> THREAD_TYPE
        "TUPLE_TYPE"                    -> TUPLE_TYPE
        "TYPE"                          -> TYPE
        "TYPE_ALIAS_DECLARATION"        -> TYPE_ALIAS_DECLARATION
        "TYPE_REFERENCE_EXPRESSION"     -> TYPE_REFERENCE_EXPRESSION
        "TYPE_UNION_LIST"               -> TYPE_UNION_LIST
        "VAR_DEFINITION"                -> VAR_DEFINITION
        "WRONG_POINTER_TYPE"            -> WRONG_POINTER_TYPE

        else                            -> {
            throw RuntimeException("Unknown element type: $name")
        }
    }

    class VlangGenericStubElementType<T : VlangTypeImpl>(val clazz: KClass<T>, name: String) :
        VlangTypeStubElementType(name) {
        override fun createPsi(stub: VlangTypeStub): VlangType {
            return clazz.constructors.first { it.parameters.size == 2 }.call(stub, this) as VlangType
        }
    }
}
