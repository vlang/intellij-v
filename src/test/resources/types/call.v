module types

type Cb = fn () int

struct Foo {
	cb fn () string
	cb2 Cb
}

fn some() string {}
fn some2() int {}

fn main() {
	arr := [some, some]
	a := arr[0]()
	expr_type(a, 'string')

	foo := Foo{some, some2}
	b := foo.cb()
	expr_type(b, 'string')

	c := foo.cb2()
	expr_type(c, 'int')
}
