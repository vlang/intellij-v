package org.vlang.lang.stubs.types

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubIndexKey
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.stubs.VlangFileStub
import org.vlang.lang.stubs.VlangNamedStub
import org.vlang.lang.stubs.index.VlangNamesIndex

abstract class VlangNamedStubElementType<S : VlangNamedStub<T>, T : VlangNamedElement>(debugName: String) :
    VlangStubElementType<S, T>(debugName) {

    override fun shouldCreateStub(node: ASTNode): Boolean {
        if (!super.shouldCreateStub(node)) return false
        val psi = node.psi
        return psi is VlangNamedElement && StringUtil.isNotEmpty(psi.name)
    }

    override fun indexStub(stub: S, sink: IndexSink) {
        val name = stub.name ?: return
        if (shouldIndex() && name.isNotEmpty()) {
            var packageName: String? = null
            var parent: StubElement<*>? = stub.parentStub
            while (parent != null) {
                if (parent is VlangFileStub) {
                    packageName = parent.getModuleName()
                    break
                }
                parent = parent.parentStub
            }
            val indexingName = if (packageName != null && packageName.isNotEmpty()) "$packageName.$name" else name
//            if (stub.isPublic) {
                sink.occurrence(VlangNamesIndex.KEY, indexingName)
//            } else {
//                sink.occurrence<PsiElement, String>(VlangAllPrivateNamesIndex.ALL_PRIVATE_NAMES, indexingName)
//            }

            for (key in getExtraIndexKeys()) {
                sink.occurrence(key, indexingName)
            }
        }
    }

    protected fun shouldIndex() = true

    protected open fun getExtraIndexKeys() = emptyList<StubIndexKey<String, out VlangNamedElement>>()
}
