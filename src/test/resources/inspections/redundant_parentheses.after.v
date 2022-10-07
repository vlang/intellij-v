module types

fn main() {
	a := 100

	if true {
		println('true')
	} else if 100 == 100 {
		println('false')
	} else if a > 5 && a < 10 {
		println('false')
	} else {
		println('false')
	}

	match true {
		10 { 10 }
	}

	match a {
		10 { 10 }
	}

	match a > 10 {
		true { 10 }
		false { 20 }
	}
}
