module annotators

struct Boo {}

struct Foo {
	<error descr="Invalid recursive type: Foo refers to itself, try change the type to '&Foo'">other Foo</error>
	other2 &Foo
	boo Boo
}
