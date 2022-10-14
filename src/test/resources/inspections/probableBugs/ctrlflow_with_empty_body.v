fn main() {
	<warning descr="'if' has empty body">if</warning> true {
	}

	if true {
		// a comment
	}

	if true {
		println("test")
	}

	if true {
	} else <warning descr="'if' has empty body">if</warning> false {
	}

	if true {
	} else if false {
		// comment
	}

	if true {
	} else if false {
		eprintln("error")
	}

	if true {
	} else if false {
	} <warning descr="'else' has empty body">else</warning> {
	}

	if true {
	} else if false {
	} else {
		// comment inside else
	}

	if true {
	} else if false {
	} else {
		panic("die")
	}

	<warning descr="'for' has empty body">for</warning> el in arr {
	}

	for el in arr {
		println("element is: $el")
	}

	for el in arr {
		// empty body with comment
	}
}
