package org.vlang.lang.stubs.types

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubIndexKey
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.stubs.VlangFileStub
import org.vlang.lang.stubs.VlangNamedStub

abstract class VlangNamedStubElementType<S : VlangNamedStub<T>, T : VlangNamedElement>(debugName: String) :
    VlangStubElementType<S, T>(debugName) {

    override fun shouldCreateStub(node: ASTNode): Boolean {
        if (!super.shouldCreateStub(node)) return false
        val psi = node.psi
        return psi is VlangNamedElement && StringUtil.isNotEmpty(psi.name)
    }

    override fun indexStub(stub: S, sink: IndexSink) {
        val name = stub.name
        if (shouldIndex() && StringUtil.isNotEmpty(name)) {
            var packageName: String? = null
            var parent: StubElement<*>? = stub.parentStub
            while (parent != null) {
                if (parent is VlangFileStub) {
                    packageName = parent.getPackageName()
                    break
                }
                parent = parent.parentStub
            }
            val indexingName = if (StringUtil.isNotEmpty(packageName)) "$packageName.$name" else name
//            if (stub.isPublic()) {
//                sink.occurrence<PsiElement, String>(VlangAllPublicNamesIndex.ALL_PUBLIC_NAMES, indexingName)
//            } else {
//                sink.occurrence<PsiElement, String>(VlangAllPrivateNamesIndex.ALL_PRIVATE_NAMES, indexingName)
//            }
            if (name != null) {
                for (key in getExtraIndexKeys()) {
                    sink.occurrence(key, name)
                }
            }
        }
    }

    protected fun shouldIndex(): Boolean {
        return true
    }

    protected open fun getExtraIndexKeys(): Collection<StubIndexKey<String, out VlangNamedElement>> {
        return emptyList()
    }
}