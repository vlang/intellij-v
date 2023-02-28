module types

struct Foo {}

type Array = []int

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

	map_value := {'': Foo{}}
	expr_type(map_value, 'map[string]Foo')

	for key in map_value {
	    expr_type(key, 'Foo')
	}

	for key, value in map_value {
		expr_type(key, 'string')
		expr_type(value, 'Foo')
	}

	arr := Array{}
	for value in arr {
		expr_type(value, 'int')
	}
}
