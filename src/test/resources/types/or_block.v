module main

fn foo() ?int {
	return 10
}

fn get_none() ? {

}

fn main() {
	a := foo() or {
		return
	}
	expr_type(a, 'int')

	a1 := get_none() or {
		return
	}
	expr_type(a1, 'void')
}
