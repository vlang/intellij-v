module types

struct Foo {
	name string
}

fn get_variadic(a string, b ...string) {
	expr_type(a, 'string')
	expr_type(b, '[]string')
}

fn get_variadic2(b ...Foo) {
	expr_type(b, '[]Foo')
}

fn main() {
	mut int_arr := [1, 2, 3]
	mut str_arr := ['a', 'b', 'c']

	expr_type(int_arr, '[]int')
	expr_type(str_arr, '[]string')

	foo_arr := [Foo{'a'}, Foo{name: 'b'}, Foo{'c'}]
	expr_type(foo_arr, '[]Foo')
}
