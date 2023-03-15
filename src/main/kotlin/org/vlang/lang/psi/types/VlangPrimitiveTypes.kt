package org.vlang.lang.psi.types

enum class VlangPrimitiveTypes(val value: String, val size: Int, val numeric: Boolean = false) {
    BOOL("bool", 1),
    BYTE("byte", 1),
    RUNE("rune" , 4),
    CHAR("char" , 4),
    INT("int", 4, true),
    I8("i8", 1, true),
    I16("i16", 2, true),
    I32("i32", 4, true),
    I64("i64", 8, true),
    ISIZE("isize", 4, true),
    USIZE("usize", 4, true),
    U8("u8", 1, true),
    U16("u16", 2, true),
    U32("u32", 4, true),
    U64("u64", 8, true),
    F32("f32", 4, true),
    F64("f64", 8, true),
    STRING("string", -1),
    VOIDPTR("voidptr", 8),
    BYTEPTR("byteptr", 8),
    CHARPTR("charptr", 8),
    NIL("nil", 0);

    companion object {
        fun isPrimitiveType(name: String): Boolean {
            return values().any { it.value == name }
        }

        fun isNumeric(name: String): Boolean {
            return values().any { it.value == name && it.numeric }
        }
    }
}
