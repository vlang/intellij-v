module annotators

struct Foo {
	a0 int
pub <error descr="Duplicate modifier 'pub'">pub</error>:
	a1 int
mut <error descr="Duplicate modifier 'mut'">mut</error> pub:
	a2 int
mut <error descr="Duplicate modifier 'mut'">mut</error>:
	a3 int
mut:
	a4 int
pub:
	a5 int
}
