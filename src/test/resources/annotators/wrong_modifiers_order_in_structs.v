module annotators

struct Foo {
<error descr="Use 'pub mut' instead of 'mut pub'">mut pub:</error>
	a int
pub mut:
	b int
}
