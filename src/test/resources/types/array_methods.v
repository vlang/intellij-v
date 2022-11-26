module types

struct Foo {
	age int
}

fn main() {
	arr := [1, 2, 3]
	first := arr.first()
	expr_type(first, 'int')
	last := arr.last()
	expr_type(last, 'int')

	new_arr1 := arr[1..0]
	expr_type(new_arr1, '[]int')

	new_arr2 := arr.clone()
	expr_type(new_arr2, '[]int')

	new_arr3 := arr.map(fn (it int) Foo {
		return Foo{it}
	})
	expr_type(new_arr3, '[]Foo')

	new_arr4 := arr.filter(fn (it int) bool {
		return it > 100
	})
	expr_type(new_arr4, '[]int')

	new_arr5 := arr.reverse()
	expr_type(new_arr5, '[]int')

	expr_type([1, 2, 3].first(), 'int')

	expr_type([1, 2, 3].map(it > 5), '[]bool')
	expr_type([Foo{}].map(it.age), '[]int')
}
