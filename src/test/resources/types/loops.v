module types

fn main() {
	a := [1, 2, 3]
	expr_type(a, '[]i64')

	for i := 0; i < a.len; i++ {
		expr_type(i, 'i64')
		expr_type(a[i], 'i64')
	}

	for key in a {
		expr_type(key, 'i64')
	}

	for i, key in a {
		expr_type(i, 'i64')
		expr_type(key, 'i64')
	}
}
