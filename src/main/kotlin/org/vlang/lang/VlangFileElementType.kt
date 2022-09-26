package org.vlang.lang

import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiFile
import com.intellij.psi.StubBuilder
import com.intellij.psi.stubs.*
import com.intellij.psi.tree.IStubFileElementType
import com.intellij.util.io.StringRef
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.stubs.VlangFileStub
import org.vlang.lang.stubs.index.VlangPackagesIndex

class VlangFileElementType : IStubFileElementType<VlangFileStub>("VLANG_FILE", VlangLanguage.INSTANCE) {
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

        val packageName = stub.getModuleName() ?: return
        if (packageName.isNotEmpty()) {
            sink.occurrence(VlangPackagesIndex.KEY, packageName)
        }
    }

    override fun serialize(stub: VlangFileStub, dataStream: StubOutputStream) {
        dataStream.writeUTF(StringUtil.notNullize(stub.getBuildFlags()))
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangFileStub {
        return VlangFileStub(null, StringRef.fromNullableString(StringUtil.nullize(dataStream.readUTF())))
    }

    override fun getExternalId() = "vlang.FILE"

    companion object {
        val INSTANCE = VlangFileElementType()
        const val VERSION = 35
    }
}
