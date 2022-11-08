module types

struct Foo {
	name shared string
}

fn main() {
	mut foo := Foo{}
	expr_type(foo.name, 'shared string')
}
