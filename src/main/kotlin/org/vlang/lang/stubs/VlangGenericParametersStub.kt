package org.vlang.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import org.vlang.lang.psi.VlangGenericParameters

class VlangGenericParametersStub(parent: StubElement<*>?, elementType: IStubElementType<*, *>, val parameters: List<String>) :
    StubBase<VlangGenericParameters>(parent, elementType)

