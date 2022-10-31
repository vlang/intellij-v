module types

interface IFoo {
	name string
	parts []int
}

struct Foo {
	name  string
	parts []int
}

fn foo(f IFoo) {
	expr_type(f.name, 'string')
	expr_type(f.parts, '[]int')
}

fn main() {
	foo := Foo{
		name: 'foo'
		parts: [1, 2, 3]
	}

	expr_type(foo.name, 'string')
	expr_type(foo.parts, '[]int')
}
