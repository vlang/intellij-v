package org.vlang.lang.stubs.types

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubBase
import com.intellij.psi.util.PsiTreeUtil
import org.vlang.lang.VlangLanguage
import org.vlang.lang.psi.VlangBlock
import org.vlang.lang.psi.VlangCompositeElement

abstract class VlangStubElementType<S : StubBase<T>, T : VlangCompositeElement>(debugName: String) :
    IStubElementType<S, T>(debugName, VlangLanguage) {

    override fun getExternalId() = "vlang." + super.toString()

    override fun indexStub(stub: S, sink: IndexSink) {}

    override fun shouldCreateStub(node: ASTNode): Boolean {
        return super.shouldCreateStub(node) && shouldCreateStubInBlock(node)
    }

    protected open fun shouldCreateStubInBlock(node: ASTNode): Boolean {
        return PsiTreeUtil.getParentOfType(node.psi, VlangBlock::class.java) == null
    }
}
