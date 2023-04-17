package org.vlang.lang

import com.intellij.psi.PsiFile
import com.intellij.psi.StubBuilder
import com.intellij.psi.stubs.*
import com.intellij.psi.tree.IStubFileElementType
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.stubs.VlangFileStub
import org.vlang.lang.stubs.index.VlangModulesFingerprintIndex
import org.vlang.lang.stubs.index.VlangModulesIndex

class VlangFileElementType : IStubFileElementType<VlangFileStub>("VLANG_FILE", VlangLanguage) {
    override fun getStubVersion() = VERSION

    override fun getBuilder(): StubBuilder {
        return object : DefaultStubBuilder() {
            override fun createStubForFile(file: PsiFile): StubElement<*> {
                return if (file is VlangFile) {
                    VlangFileStub(file)
                } else {
                    super.createStubForFile(file)
                }
            }
        }
    }

    override fun indexStub(stub: PsiFileStub<*>, sink: IndexSink) {
        super.indexStub(stub, sink)
        if (stub !is VlangFileStub) return

        val fqn = stub.getModuleQualifiedName() ?: return
        if (fqn.isNotEmpty()) {
            sink.occurrence(VlangModulesIndex.KEY, fqn)
        }

        val name = stub.getModuleName() ?: return
        if (name.isNotEmpty()) {
            sink.occurrence(VlangModulesFingerprintIndex.KEY, name)
        }
    }

    override fun serialize(stub: VlangFileStub, dataStream: StubOutputStream) {}

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangFileStub {
        return VlangFileStub(null)
    }

    override fun getExternalId() = "vlang.FILE"

    companion object {
        val INSTANCE = VlangFileElementType()
        const val VERSION = 89
    }
}
