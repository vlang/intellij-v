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

	<error descr="Match is not exhaustive, add missing branches for: '.red', '.green', '.blue' or 'else {}' branch">match</error> color {

	}

	<error descr="Match is not exhaustive, add missing branches for: '.red', '.green', '.blue' or 'else {}' branch">match</error> color {}

	<error descr="Match is not exhaustive, add missing branches for: '.green', '.blue' or 'else {}' branch">match</error> color {
		.red {}
	}

	<error descr="Match is not exhaustive, add missing branches for: '.blue' or 'else {}' branch">match</error> color {
		.red { println('READ') }
		.green { println('GREEN') }
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

	<error descr="Match is not exhaustive, add missing branches for: 'MyColors', '[]string', 'int', 'string' or 'else {}' branch">match</error> val {

	}

	<error descr="Match is not exhaustive, add missing branches for: 'MyColors', '[]string', 'int', 'string' or 'else {}' branch">match</error> val {}

	<error descr="Match is not exhaustive, add missing branches for: '[]string', 'int', 'string' or 'else {}' branch">match</error> val {
		MyColors {}
	}

	<error descr="Match is not exhaustive, add missing branches for: 'int', 'string' or 'else {}' branch">match</error> val {
		MyColors { println('MyColors') }
		[]string { println('[]string') }
	}
}
