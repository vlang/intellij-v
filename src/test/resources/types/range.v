module types

fn main() {
	for i in 0 .. 100 {
		expr_type(i, 'int')
	}

	for rune in "value" {
		expr_type(rune, 'rune')
	}

	expr_type(`a`...`z`, 'rune')
}
