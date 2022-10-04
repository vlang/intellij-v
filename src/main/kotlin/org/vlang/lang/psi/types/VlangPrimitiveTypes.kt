package org.vlang.lang.psi.types

enum class VlangPrimitiveTypes(val value: String) {
    BOOL("bool"),
    BYTE("byte"),
    RUNE("rune"),
    INT("int"),
    I8("i8"),
    I16("i16"),
    I32("i32"),
    I64("i64"),
    ISIZE("isize"),
    USIZE("usize"),
    U8("u8"),
    U16("u16"),
    U32("u32"),
    U64("u64"),
    F32("f32"),
    F64("f64"),
    STRING("string"),
    VOIDPTR("voidptr"),
    ANY("any"),
}
