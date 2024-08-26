package io.vlang.lang.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import io.vlang.lang.codeInsight.controlFlow.VlangControlFlow
import io.vlang.lang.psi.VlangScope
import io.vlang.lang.psi.VlangScopeHolder
import io.vlang.lang.stubs.VlangStaticMethodDeclarationStub

abstract class VlangStaticMethodDeclarationWithScopeHolder : VlangFunctionOrMethodDeclarationImpl<VlangStaticMethodDeclarationStub>, VlangScopeHolder {
    private var scope: VlangScope? = null

    constructor(stub: VlangStaticMethodDeclarationStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
    constructor(node: ASTNode) : super(node)

    override fun controlFlow(): VlangControlFlow {
        return scope().controlFlow()
    }

    override fun scope(): VlangScope {
        if (scope == null) {
            scope = VlangScopeImpl(this)
        }

        return scope!!
    }

    override fun subtreeChanged() {
        super.subtreeChanged()
        scope?.clear()
    }
}
