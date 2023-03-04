module annotators

struct Foo {
<error descr="Expected ':' after modifiers">pub mut</error>
}

struct Foo1 {
<error descr="Expected ':' after modifiers">pub</error>
	a int
}

struct Foo1 {
	a int
<error descr="Expected ':' after modifiers">mut</error>
}
