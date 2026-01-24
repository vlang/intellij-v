fn main() {
	defer {
		println('end of scope')
	}

	defer(fn) {
		println('end of function')
	}
}
