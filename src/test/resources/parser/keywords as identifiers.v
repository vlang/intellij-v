module keywords_as_identifiers

// Keywords as struct field names
struct Foo {
	if int
	else int
	for int
}

// Keywords as function names
fn import() int {
	return 0
}

// Keywords as method names
fn (f Foo) return() int {
	return f.if + f.else
}

// Keywords as qualified references (after '.')
fn main() {
	f := Foo{}
	println(f.if)
	println(f.for)
}

// Keywords as const names
const defer = 10
const match = 20

// Keywords as enum field names (already supported)
enum Bar {
	if
	else
	for
}
