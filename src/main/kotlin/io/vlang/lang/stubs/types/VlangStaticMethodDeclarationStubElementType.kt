package io.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.ArrayFactory
import io.vlang.lang.psi.VlangMethodDeclaration
import io.vlang.lang.psi.VlangPointerType
import io.vlang.lang.psi.VlangStaticMethodDeclaration
import io.vlang.lang.psi.impl.VlangStaticMethodDeclarationImpl
import io.vlang.lang.stubs.VlangStaticMethodDeclarationStub

class VlangStaticMethodDeclarationStubElementType(name: String) :
    VlangNamedStubElementType<VlangStaticMethodDeclarationStub, VlangStaticMethodDeclaration>(name) {

    override fun createPsi(stub: VlangStaticMethodDeclarationStub): VlangStaticMethodDeclaration {
        return VlangStaticMethodDeclarationImpl(stub, this)
    }

    override fun createStub(psi: VlangStaticMethodDeclaration, parentStub: StubElement<*>?): VlangStaticMethodDeclarationStub {
        return VlangStaticMethodDeclarationStub(parentStub, this, psi.name, psi.isPublic(), calcTypeText(psi))
    }

    override fun serialize(stub: VlangStaticMethodDeclarationStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
        dataStream.writeName(stub.typeName)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangStaticMethodDeclarationStub {
        return VlangStaticMethodDeclarationStub(
            parentStub,
            this,
            dataStream.readName(),
            dataStream.readBoolean(),
            dataStream.readName()
        )
    }

    // TODO: Implement indexStub
//    override fun indexStub(stub: VlangStaticMethodDeclarationStub, sink: IndexSink) {
//        super.indexStub(stub, sink)
//
//        val name = stub.name
//        if (name != null) {
//            sink.occurrence(VlangMethodFingerprintIndex.KEY, name)
//        }
//
//        val typeName = stub.typeName ?: return
//        if (typeName.isEmpty()) return
//
//        val parent = stub.parentStub
//        if (parent !is VlangFileStub) return
//
//        val moduleName = parent.getModuleQualifiedName()
//        if (moduleName.isNullOrEmpty()) {
//            sink.occurrence(VlangMethodIndex.KEY, typeName)
//        } else {
//            sink.occurrence(VlangMethodIndex.KEY, "$moduleName.$typeName")
//        }
//    }

    companion object {
        val EMPTY_ARRAY: Array<VlangMethodDeclaration?> = arrayOfNulls(0)
        val ARRAY_FACTORY = ArrayFactory<VlangMethodDeclaration> { count: Int ->
            if (count == 0) EMPTY_ARRAY else arrayOfNulls<VlangMethodDeclaration>(count)
        }

        fun calcTypeText(psi: VlangStaticMethodDeclaration): String? {
            val type = psi.getTypeReferenceExpression().getIdentifier()
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
