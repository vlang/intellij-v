module main

struct Foo {}

fn expr_type(expr any, typ string) {}

fn ret_tuple() (int, string) {
	return 1, 'hello'
}

fn ret_three_tuple() (bool, string, Foo) {
	return true, 'hello', Foo{}
}

fn main() {
	a, b := ret_tuple()

	expr_type(a, 'int')
	expr_type(b, 'string')

	c, d, e := ret_three_tuple()

	expr_type(c, 'bool')
	expr_type(d, 'string')
	expr_type(e, 'Foo')

	tuple := ret_tuple()
	expr_type(tuple, '(int, string)')

	three_tuple := ret_three_tuple()
	expr_type(three_tuple, '(bool, string, Foo)')
}
