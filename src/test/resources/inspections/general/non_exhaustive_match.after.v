module main

type StringOrInt = MyColors | []string | int | string

enum MyColors {
	red
	green
	blue
}

fn enums(color MyColors) {
	a := 100
	// ok, not enum
	match a {
		100 {}
	}

	// ok, with else
	match color {
		.red {}
		else {}
	}

	// ok, all cases
	match color {
		.red {}
		.green {}
		.blue {}
	}

	match color {
		.red {}
		.green {}
		.blue {}
	}

	match color {
		.red {}
		.green {}
		.blue {}
	}

	match color {
		.red {}
		.green {}
		.blue {}
	}

	match color {
		.red { println('READ') }
		.green { println('GREEN') }
		.blue {}
	}
}

fn types(val StringOrInt) {
	match val {
		MyColors {}
		[]string {}
		int {}
		string {}
	}

	// ok, with else
	match val {
		MyColors {}
		else {}
	}

	match val {
		MyColors {}
		[]string {}
		int {}
		string {}
	}

	match val {
		MyColors {}
		[]string {}
		int {}
		string {}
	}

	match val {
		MyColors {}
		[]string {}
		int {}
		string {}
	}

	match val {
		MyColors { println('MyColors') }
		[]string { println('[]string') }
		int {}
		string {}
	}
}
