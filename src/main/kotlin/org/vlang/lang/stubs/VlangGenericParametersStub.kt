package org.vlang.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import com.intellij.util.io.StringRef
import org.vlang.lang.psi.VlangGenericParameters
import org.vlang.lang.psi.VlangGlobalVariableDefinition

class VlangGenericParametersStub(parent: StubElement<*>?, elementType: IStubElementType<*, *>, val parameters: List<String>) :
    StubBase<VlangGenericParameters>(parent, elementType)

