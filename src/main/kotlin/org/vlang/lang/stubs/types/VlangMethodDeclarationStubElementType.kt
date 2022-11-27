package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.ArrayFactory
import org.vlang.lang.psi.VlangMethodDeclaration
import org.vlang.lang.psi.VlangPointerType
import org.vlang.lang.psi.impl.VlangMethodDeclarationImpl
import org.vlang.lang.stubs.VlangFileStub
import org.vlang.lang.stubs.VlangMethodDeclarationStub
import org.vlang.lang.stubs.index.VlangMethodFingerprintIndex
import org.vlang.lang.stubs.index.VlangMethodIndex

class VlangMethodDeclarationStubElementType(name: String) :
    VlangNamedStubElementType<VlangMethodDeclarationStub, VlangMethodDeclaration>(name) {

    override fun createPsi(stub: VlangMethodDeclarationStub): VlangMethodDeclaration {
        return VlangMethodDeclarationImpl(stub, this)
    }

    override fun createStub(psi: VlangMethodDeclaration, parentStub: StubElement<*>?): VlangMethodDeclarationStub {
        return VlangMethodDeclarationStub(parentStub, this, psi.name, psi.isPublic(), calcTypeText(psi))
    }

    override fun serialize(stub: VlangMethodDeclarationStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
        dataStream.writeName(stub.typeName)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangMethodDeclarationStub {
        return VlangMethodDeclarationStub(
            parentStub,
            this,
            dataStream.readName(),
            dataStream.readBoolean(),
            dataStream.readName()
        )
    }

    override fun indexStub(stub: VlangMethodDeclarationStub, sink: IndexSink) {
        super.indexStub(stub, sink)

        val name = stub.name
        if (name != null) {
            sink.occurrence(VlangMethodFingerprintIndex.KEY, name)
        }

        val typeName = stub.typeName ?: return
        if (typeName.isEmpty()) return

        val parent = stub.parentStub
        if (parent !is VlangFileStub) return

        val moduleName = parent.getModuleQualifiedName()
        if (moduleName.isNullOrEmpty()) {
            sink.occurrence(VlangMethodIndex.KEY, typeName)
        } else {
            sink.occurrence(VlangMethodIndex.KEY, "$moduleName.$typeName")
        }
    }

    companion object {
        val EMPTY_ARRAY: Array<VlangMethodDeclaration?> = arrayOfNulls(0)
        val ARRAY_FACTORY = ArrayFactory<VlangMethodDeclaration> { count: Int ->
            if (count == 0) EMPTY_ARRAY else arrayOfNulls<VlangMethodDeclaration>(count)
        }

        fun calcTypeText(psi: VlangMethodDeclaration): String? {
            val type = psi.receiverType
            val underlyingType = if (type is VlangPointerType) {
                type.type
            } else {
                type
            }

            val text = underlyingType?.text ?: return null
            if (text.contains("<")) {
                // Foo<T> -> Foo
                return text.substringBefore("<")
            }

            if (text.contains("[") && !text.contains("map[") && !text.startsWith("[")) {
                // Foo[T] -> Foo
                return text.substringBefore("[")
            }

            return text
        }
    }
}
