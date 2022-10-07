module types

fn main() {
	a := 100

	if <warning descr="Redundant parentheses">(true)</warning> {
		println('true')
	} else if <warning descr="Redundant parentheses">(100 == 100)</warning> {
		println('false')
	} else if <warning descr="Redundant parentheses">(a > 5 && a < 10)</warning> {
		println('false')
	} else {
		println('false')
	}

	match <warning descr="Redundant parentheses">(true)</warning> {
		10 { 10 }
	}

	match <warning descr="Redundant parentheses">(a)</warning> {
		10 { 10 }
	}

	match <warning descr="Redundant parentheses">(a > 10)</warning> {
		true { 10 }
		false { 20 }
	}
}
