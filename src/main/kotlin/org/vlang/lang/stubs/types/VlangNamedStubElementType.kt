package org.vlang.lang.stubs.types

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubIndexKey
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.VlangPsiTreeUtil.parentStubOfType
import org.vlang.lang.stubs.VlangFileStub
import org.vlang.lang.stubs.VlangMethodDeclarationStub
import org.vlang.lang.stubs.VlangNamedStub
import org.vlang.lang.stubs.index.VlangNamesIndex

abstract class VlangNamedStubElementType<S : VlangNamedStub<T>, T : VlangNamedElement>(debugName: String) :
    VlangStubElementType<S, T>(debugName) {

    override fun shouldCreateStub(node: ASTNode): Boolean {
        if (!super.shouldCreateStub(node)) return false
        val psi = node.psi as? VlangNamedElement ?: return false
        val name = psi.name ?: return false
        return name.isNotEmpty()
    }

    override fun indexStub(stub: S, sink: IndexSink) {
        val name = stub.name ?: return
        if (shouldIndex() && name.isNotEmpty()) {
            val file = stub.parentStubOfType<VlangFileStub>()
            val moduleName = file?.getModuleQualifiedName() ?: ""
            val indexingName = if (moduleName.isNotEmpty()) "$moduleName.$name" else name

            if (stub is VlangMethodDeclarationStub) {
                val typeName = stub.typeName ?: return
                val parts = buildList {
                    add(moduleName)
                    if (typeName.isNotEmpty()) {
                        add(typeName)
                    }
                    add(name)
                }
                if (typeName.isNotEmpty()) {
                    sink.occurrence(VlangNamesIndex.KEY, parts.joinToString("."))
                }
            }

            sink.occurrence(VlangNamesIndex.KEY, indexingName)

            for (key in getExtraIndexKeys()) {
                sink.occurrence(key, indexingName)
            }
        }
    }

    protected fun shouldIndex() = true

    protected open fun getExtraIndexKeys() = emptyList<StubIndexKey<String, out VlangNamedElement>>()
}
