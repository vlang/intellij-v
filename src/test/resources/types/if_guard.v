module types

fn foo1() ?int {
	return 1
}

fn main() {
	if foo := foo1() {
		expr_type(foo, 'int')
	}
}
