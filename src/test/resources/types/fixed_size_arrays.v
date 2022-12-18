module types

fn main() {
	arr := [4]int{}
	expr_type(arr, '[4]int')

	arr2 := [1, 2, 3]!
	expr_type(arr2, '[3]int')

	arr3 := [[1,2,3]!]!
	expr_type(arr3, '[1][3]int')

	first := arr3.first()
	expr_type(first, '[3]int')

	slice := arr[0..2]
	expr_type(slice, '[]int')
}
