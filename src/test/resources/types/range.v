module types

fn main() {
	for i in 0 .. 100 {
		expr_type(i, 'int')
	}

	for rune in "value" {
		expr_type(rune, 'u8')
	}

	expr_type(`a`...`z`, 'rune')

	expr_type(1..10, '[]int')
	expr_type(1 + 1..10 + 10, '[]int')

	arr := [1, 2, 3]
	expr_type(arr[0 + 1..], '[]int')
	expr_type(arr[..1 + 1], '[]int')
}
