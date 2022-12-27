module types

struct Foo {}

const foo = 100
const foo1 = Foo{}
const foo2 = &Foo{}

fn main() {
	expr_type(foo, 'int')
	expr_type(foo1, 'Foo')
	expr_type(foo2, '&Foo')
}
