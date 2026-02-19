module types

struct Foo {
	name string
}

fn get_pointer() &Foo {
	return Foo{}
}

fn main() {
	foo := &Foo{}
	expr_type(foo, '&Foo')
	expr_type(get_pointer(), '&Foo')

	a := &u8(7)
	expr_type(a, '&u8')

	b := &&u8(7)
	expr_type(b, '&&u8')

	c := &&&u8(7)
	expr_type(c, '&&&u8')
}
