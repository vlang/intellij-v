package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class VlangPrimitiveTypeEx(val name: VlangPrimitiveTypes) : VlangBaseTypeEx(null) {
    override fun module(): String = "builtin"

    override fun toString(): String = name.value

    override fun qualifiedName(): String = name.value

    override fun readableName(context: PsiElement): String = name.value

    fun isNumeric(): Boolean {
        return name.numeric
    }

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        return when (rhs) {
            is VlangAnyTypeEx       -> true
            is VlangUnknownTypeEx   -> true
            is VlangVoidPtrTypeEx   -> true
            is VlangGenericTypeEx   -> true
            is VlangPrimitiveTypeEx -> {
                if (name == rhs.name) {
                    return true
                }

                val lhsType = name
                val rhsType = rhs.name

                val lhsName = lhsType.value
                val rhsName = rhsType.value

                if (lhsName.startsWith("i") && rhsName.startsWith("i")) {
                    return rhs.name.size <= name.size
                }

                if (lhsName.startsWith("u") && rhsName.startsWith("u")) {
                    return rhs.name.size <= name.size
                }

                if (lhsName.startsWith("f") && rhsName.startsWith("f")) {
                    return rhs.name.size <= name.size
                }

                false
            }

            else                    -> false
        }
    }

    override fun isEqual(rhs: VlangTypeEx): Boolean {
        return rhs is VlangPrimitiveTypeEx && name == rhs.name
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VlangPrimitiveTypeEx

        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    companion object {
        val BOOL = VlangPrimitiveTypeEx(VlangPrimitiveTypes.BOOL)
        val BYTE = VlangPrimitiveTypeEx(VlangPrimitiveTypes.BYTE)
        val RUNE = VlangPrimitiveTypeEx(VlangPrimitiveTypes.RUNE)
        val CHAR = VlangPrimitiveTypeEx(VlangPrimitiveTypes.CHAR)
        val I16 = VlangPrimitiveTypeEx(VlangPrimitiveTypes.I16)
        val I32 = VlangPrimitiveTypeEx(VlangPrimitiveTypes.I32)
        val I64 = VlangPrimitiveTypeEx(VlangPrimitiveTypes.I64)
        val I8 = VlangPrimitiveTypeEx(VlangPrimitiveTypes.I8)
        val INT = VlangPrimitiveTypeEx(VlangPrimitiveTypes.INT)
        val ISIZE = VlangPrimitiveTypeEx(VlangPrimitiveTypes.ISIZE)
        val U16 = VlangPrimitiveTypeEx(VlangPrimitiveTypes.U16)
        val U32 = VlangPrimitiveTypeEx(VlangPrimitiveTypes.U32)
        val U64 = VlangPrimitiveTypeEx(VlangPrimitiveTypes.U64)
        val U8 = VlangPrimitiveTypeEx(VlangPrimitiveTypes.U8)
        val USIZE = VlangPrimitiveTypeEx(VlangPrimitiveTypes.USIZE)
        val F32 = VlangPrimitiveTypeEx(VlangPrimitiveTypes.F32)
        val F64 = VlangPrimitiveTypeEx(VlangPrimitiveTypes.F64)
        val NIL = VlangPrimitiveTypeEx(VlangPrimitiveTypes.NIL)
        val BYTEPTR = VlangPrimitiveTypeEx(VlangPrimitiveTypes.BYTEPTR)
        val CHARPTR = VlangPrimitiveTypeEx(VlangPrimitiveTypes.CHARPTR)

        fun get(name: String): VlangPrimitiveTypeEx? {
            return when (name) {
                "bool"    -> BOOL
                "byte"    -> BYTE
                "rune"    -> RUNE
                "char"    -> CHAR
                "i16"     -> I16
                "i32"     -> I32
                "i64"     -> I64
                "i8"      -> I8
                "int"     -> INT
                "isize"   -> ISIZE
                "u16"     -> U16
                "u32"     -> U32
                "u64"     -> U64
                "u8"      -> U8
                "usize"   -> USIZE
                "f32"     -> F32
                "f64"     -> F64
                "nil"     -> NIL
                "byteptr" -> BYTEPTR
                "charptr" -> CHARPTR
                else      -> null
            }
        }
    }
}
