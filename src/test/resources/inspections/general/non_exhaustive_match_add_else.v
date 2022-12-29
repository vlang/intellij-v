module main

type StringOrInt = MyColors | []string | int | string

enum MyColors {
	red
	green
	blue
}

fn enums(color MyColors) {
	<warning descr="Match is not exhaustive, add missing branches for: '.red', '.green', '.blue' or 'else {}' branch">match</warning> color {

	}

	<warning descr="Match is not exhaustive, add missing branches for: '.red', '.green', '.blue' or 'else {}' branch">match</warning> color {}

	<warning descr="Match is not exhaustive, add missing branches for: '.green', '.blue' or 'else {}' branch">match</warning> color {
		.red {}
	}

	<warning descr="Match is not exhaustive, add missing branches for: '.blue' or 'else {}' branch">match</warning> color {
		.red { println('READ') }
		.green { println('GREEN') }
	}
}

fn types(val StringOrInt) {
	<warning descr="Match is not exhaustive, add missing branches for: 'MyColors', '[]string', 'int', 'string' or 'else {}' branch">match</warning> val {

	}

	<warning descr="Match is not exhaustive, add missing branches for: 'MyColors', '[]string', 'int', 'string' or 'else {}' branch">match</warning> val {}

	<warning descr="Match is not exhaustive, add missing branches for: '[]string', 'int', 'string' or 'else {}' branch">match</warning> val {
		MyColors {}
	}

	<warning descr="Match is not exhaustive, add missing branches for: 'int', 'string' or 'else {}' branch">match</warning> val {
		MyColors { println('MyColors') }
		[]string { println('[]string') }
	}
}
