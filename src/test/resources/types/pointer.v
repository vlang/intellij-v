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
}
