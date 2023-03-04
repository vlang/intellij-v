module annotators

struct Foo {
	a1 int
	a2 int
}

struct Foo1 {
	a1 int
mut:
	a2 int
}

struct Foo2 {
mut:
	a1 int
<error descr="Duplicate modifier group 'mut:'">mut</error>:
	a2 int
}

struct Foo3 {
pub:
	a1 int
<error descr="Duplicate modifier group 'pub:'">pub</error>:
	a2 int
}

struct Foo4 {
pub mut:
	a1 int
<error descr="Duplicate modifier group 'pub mut:'">pub mut</error>:
	a2 int
}

struct Foo5 {
pub:
	a1 int
pub mut:
	a2 int
<error descr="Duplicate modifier group 'pub mut:'">pub mut</error>:
	a3 int
mut:
	a4 int
}
