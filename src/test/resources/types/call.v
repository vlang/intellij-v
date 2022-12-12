module types

struct Foo {
	cb fn () string
}

fn some() string {}

fn main() {
	arr := [some, some]
	a := arr[0]()
	expr_type(a, 'string')

	foo := Foo{some}
	b := foo.cb()
	expr_type(b, 'string')
}
