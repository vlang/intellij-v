module main

type StringOrInt = MyColors | []string | int | string

enum MyColors {
	red
	green
	blue
}

fn enums(color MyColors) {
	match color {
		else {}
	}

	match color {
		else {}
	}

	match color {
		.red {}
		else {}
	}

	match color {
		.red { println('READ') }
		.green { println('GREEN') }
		else {}
	}
}

fn types(val StringOrInt) {
	match val {
		else {}
	}

	match val {
		else {}
	}

	match val {
		MyColors {}
		else {}
	}

	match val {
		MyColors { println('MyColors') }
		[]string { println('[]string') }
		else {}
	}
}
