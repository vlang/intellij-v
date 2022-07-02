package org.vlang.lang.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import org.vlang.lang.psi.VlangFunctionOrMethodDeclaration
import org.vlang.lang.stubs.VlangFunctionOrMethodDeclarationStub

abstract class VlangFunctionOrMethodDeclarationImpl<T : VlangFunctionOrMethodDeclarationStub<*>> : VlangNamedElementImpl<T>,
    VlangFunctionOrMethodDeclaration {

    constructor(stub: T, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
    constructor(node: ASTNode) : super(node)

//    fun getVlangTypeInner(context: ResolveState?): VlangType? {
//        return VlangPsiImplUtil.getVlangTypeInner(this, context)
//    }
}
