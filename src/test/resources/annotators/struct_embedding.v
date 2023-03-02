module annotators

struct Foo {
	name string = 'Foo'
	age  int
}

struct Boo {
	name string = 'Boo'
	<error descr="Embedded structs must be defined before any other fields">Foo</error>
}

struct Boo2 {
	Foo
	name string = 'Boo'
}

struct Boo3 {
	Foo
	<error descr="Cannot embed 'Foo' more than once">Foo</error>
	name string = 'Boo'
}

struct Boo4 {
mut:
	name string = 'Boo'
pub:
	<error descr="Embedded structs must be defined before any other fields">Foo</error>
}

struct Boo5 {
mut:
	Foo
pub:
	name string = 'Boo'
}
