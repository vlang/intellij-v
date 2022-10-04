module types

fn main() {
	expr_type(1 < 10, 'bool')
	expr_type(1 > 10, 'bool')
	expr_type(1 >= 10, 'bool')
	expr_type(1 <= 10, 'bool')
	expr_type(1 == 10, 'bool')
	expr_type(1 != 10, 'bool')
	expr_type(1 in [10], 'bool')
}
