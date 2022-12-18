module types

fn main() {
	a := if true { 1 } else { 2 }
	expr_type(a, 'int')

	b := if true { 1 } else { 2.0 }
	// expr_type(b, 'f64') TODO: fix this

	c := if true { 'a' } else { 'b' }
	expr_type(c, 'string')

	d := if true {
		val := 100
		val
	} else {
		val := 200
		val
	}
	expr_type(d, 'int')

	e := if true {
		val := 100
		if val > 100 {
			1
		} else {
			2
		}
	} else {
		val := 200
		val
	}
	expr_type(e, 'int')

	a1, a2 := if true { 'a', 10 } else { 'b', 10 }
	expr_type(a1, 'string')
	expr_type(a2, 'int')

	a3, a4 := if true { unsafe { 'a', 10 } } else { 'b', 10 }
	expr_type(a3, 'string')
	expr_type(a4, 'int')

	a5 := $if true { 'a' } $else { 'b' }
	expr_type(a5, 'string')
}
