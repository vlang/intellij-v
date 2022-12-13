module types

interface IFoo {}

struct Foo {}

type Type = string

fn main() {
	expr_type(isize(100), 'isize')
	expr_type(i8(100), 'i8')
	expr_type(i16(100), 'i16')
	expr_type(i32(100), 'i32')
	expr_type(i64(100), 'i64')

    expr_type(usize(100), 'usize')
    expr_type(u8(100), 'u8')
    expr_type(u16(100), 'u16')
    expr_type(u32(100), 'u32')
    expr_type(u64(100), 'u64')

    expr_type(f32(100), 'f32')
    expr_type(f64(100), 'f64')

    expr_type(bool(1), 'bool')
    expr_type(rune(1), 'rune')
    expr_type(byte(1), 'byte')
    expr_type(voidptr(1), 'voidptr')
    expr_type(byteptr(1), 'byteptr')
    expr_type(charptr(1), 'charptr')

	expr_type(IFoo(Foo{}), 'IFoo')
	expr_type(Type(''), 'Type')
	expr_type(Foo(''), 'Foo')
}
