module types

fn foo() &string {
	s := 'hello'
	return &s
}

fn main() {
	expr_type(!true, 'bool')
	expr_type(!(true && false), 'bool')
	expr_type(+10, 'int')
	expr_type(-10.5, 'f64')
	expr_type(~0b100, 'int')
	a := 100
	expr_type(&a, '&int')
	expr_type(*&a, 'int')
	expr_type(*foo(), 'string')
	expr_type(foo(), '&string')
	expr_type(**foo(), 'string')
}
