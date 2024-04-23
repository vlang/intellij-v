package io.vlang.lang.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import io.vlang.lang.psi.VlangFunctionOrMethodDeclaration
import io.vlang.lang.stubs.VlangFunctionOrMethodDeclarationStub

abstract class VlangFunctionOrMethodDeclarationImpl<T : VlangFunctionOrMethodDeclarationStub<*>> : VlangNamedElementImpl<T>,
    VlangFunctionOrMethodDeclaration {

    constructor(stub: T, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
    constructor(node: ASTNode) : super(node)

//    fun getVlangTypeInner(context: ResolveState?): VlangType? {
//        return VlangPsiImplUtil.getVlangTypeInner(this, context)
//    }
}
