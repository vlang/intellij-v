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
}
