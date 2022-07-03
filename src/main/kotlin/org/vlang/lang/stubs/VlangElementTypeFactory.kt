package org.vlang.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import org.vlang.lang.stubs.types.VlangFunctionDeclarationStubElementType
import org.vlang.lang.stubs.types.VlangMethodDeclarationStubElementType
import org.vlang.lang.stubs.types.VlangModuleClauseStubElementType

object VlangElementTypeFactory {
//    private val TYPES: Map<String, Class<*>> = object : HashMap<String?, Class<*>?>() {
//        init {
//            put("ARRAY_OR_SLICE_TYPE", VlangArrayOrSliceTypeImpl::class.java)
//            put("CHANNEL_TYPE", VlangChannelTypeImpl::class.java)
//            put("FUNCTION_TYPE", VlangFunctionTypeImpl::class.java)
//            put("INTERFACE_TYPE", VlangInterfaceTypeImpl::class.java)
//            put("MAP_TYPE", VlangMapTypeImpl::class.java)
//            put("POINTER_TYPE", VlangPointerTypeImpl::class.java)
//            put("STRUCT_TYPE", VlangStructTypeImpl::class.java)
//            put("TYPE", VlangTypeImpl::class.java)
//            put("PAR_TYPE", VlangParTypeImpl::class.java)
//            put("SPEC_TYPE", VlangSpecTypeImpl::class.java)
//            put("TYPE_LIST", VlangTypeListImpl::class.java)
//        }
//    }

    @JvmStatic
    fun stubFactory(name: String): IStubElementType<*, *> {
//        if ("CONST_DEFINITION" == name) return VlangConstDefinitionStubElementType(name)
//        if ("FIELD_DEFINITION" == name) return VlangFieldDefinitionStubElementType(name)
//        if ("ANONYMOUS_FIELD_DEFINITION" == name) return VlangAnonymousFieldDefinitionStubElementType(name)
        if ("FUNCTION_DECLARATION" == name) return VlangFunctionDeclarationStubElementType(name)
        if ("METHOD_DECLARATION" == name) return VlangMethodDeclarationStubElementType(name)
//        if ("IMPORT_SPEC" == name) return VlangImportSpecStubElementType(name)
//        if ("PARAM_DEFINITION" == name) return VlangParamDefinitionStubElementType(name)
//        if ("RECEIVER" == name) return VlangReceiverStubElementType(name)
//        if ("TYPE_SPEC" == name) return VlangTypeSpecStubElementType(name)
//        if ("METHOD_SPEC" == name) return VlangMethodSpecStubElementType(name)
//        if ("CONST_SPEC" == name) return VlangConstSpecStubElementType(name)
        if ("MODULE_CLAUSE" == name) return VlangModuleClauseStubElementType.INSTANCE
//        if ("VAR_SPEC" == name) return VlangVarSpecStubElementType(name)
//        if ("SHORT_VAR_DECLARATION" == name) return object : VlangVarSpecStubElementType(name) {
//            fun createPsi(stub: VlangVarSpecStub): VlangVarSpec {
//                return VlangShortVarDeclarationImpl(stub, this)
//            }
//        }
//        if ("RECV_STATEMENT" == name) return object : VlangVarSpecStubElementType(name) {
//            fun createPsi(stub: VlangVarSpecStub): VlangVarSpec {
//                return VlangRecvStatementImpl(stub, this)
//            }
//        }
//        if ("RANGE_CLAUSE" == name) return object : VlangVarSpecStubElementType(name) {
//            fun createPsi(stub: VlangVarSpecStub): VlangVarSpec {
//                return VlangRangeClauseImpl(stub, this)
//            }
//        }
//        if ("VAR_DEFINITION" == name) return VlangVarDefinitionStubElementType(name)
//        if ("LABEL_DEFINITION" == name) return VlangLabelDefinitionStubElementType(name)
//        if ("PARAMETERS" == name) return VlangParametersStubElementType(name)
//        if ("SIGNATURE" == name) return VlangSignatureStubElementType(name)
//        if ("PARAMETER_DECLARATION" == name) return VlangParameterDeclarationStubElementType(name)
//        if ("RESULT" == name) return VlangResultStubElementType(name)
//        val c = TYPES[name]
//        if (c != null) {
//            return object : VlangTypeStubElementType(name) {
//                fun createPsi(stub: VlangTypeStub): VlangType {
//                    return try {
//                        ReflectionUtil.createInstance(
//                            c.getConstructor(stub.getClass(), IStubElementType::class.java),
//                            stub,
//                            this
//                        ) as VlangType
//                    } catch (e: NoSuchMethodException) {
//                        throw RuntimeException(e)
//                    }
//                }
//            }
//        }
        throw RuntimeException("Unknown element type: $name")
    }
}