module types

fn main() {
	a := [1, 2, 3]
	expr_type(a, '[]int')

	for i := 0; i < a.len; i++ {
		expr_type(i, 'int')
		expr_type(a[i], 'int')
	}

	for key in a {
		expr_type(key, 'int')
	}

	for i, key in a {
		expr_type(i, 'int')
		expr_type(key, 'int')
	}
}
