module types

fn get_variadic(a string, b ...string) {
	expr_type(a, 'string')
	expr_type(b, '[]string')
}

fn main() {
	mut int_arr := [1, 2, 3]
	mut str_arr := ['a', 'b', 'c']

	expr_type(int_arr, '[]int')
	expr_type(str_arr, '[]string')
}
