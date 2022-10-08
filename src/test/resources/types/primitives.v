module types

fn expr_type(expr any, typ string) {}

fn main() {
	expr_type(100, 'int')
	expr_type(100.0, 'f64')
	expr_type(true, 'bool')
	expr_type(false, 'bool')
	expr_type(`c`, 'rune')
	expr_type('hello', 'string')
}
