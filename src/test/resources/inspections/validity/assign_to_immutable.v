module validity

const constant = 100

interface IFoo {
	immutable int
mut:
	mutable int
}

fn (b IFoo) foo() {
	<warning descr="Immutable field 'immutable' cannot be reassigned"><warning descr="Immutable receiver 'b' cannot be reassigned">b</warning>.immutable</warning> = 100
	<warning descr="Immutable receiver 'b' cannot be reassigned">b</warning>.mutable = 100
}

struct Boo {
	name string
mut:
	age int
}

[minify]
struct MinifiedBoo {
	name string
	age int
}

fn (b Boo) foo(p string) {
	<warning descr="Immutable receiver 'b' cannot be reassigned">b</warning> = Boo{}
	<warning descr="Immutable parameter 'p' cannot be reassigned">p</warning> = ''
	<warning descr="Immutable field 'name' cannot be reassigned"><warning descr="Immutable receiver 'b' cannot be reassigned">b</warning>.name</warning> = ''
	<warning descr="Immutable receiver 'b' cannot be reassigned">b</warning>.age = 100
}

fn (mut b Boo) mut_foo(mut p string) {
	b = Boo{}
	<warning descr="Immutable field 'name' cannot be reassigned">b.name</warning> = ''
	p = ''
}

fn (b Boo) foo2() {
	<warning descr="Immutable field 'name' cannot be reassigned"><warning descr="Immutable receiver 'b' cannot be reassigned">b</warning>.name</warning> = ''
}

const boo = Boo{}
<warning descr="Immutable field 'name' cannot be reassigned"><warning descr="Constant 'boo' cannot be reassigned">boo</warning>.name</warning> = ''

fn opt() ?int {}

fn main() {
	<warning descr="Constant 'constant' cannot be reassigned">constant</warning> = 200

	immutable := 100
	<warning descr="Immutable variable 'immutable' cannot be reassigned">immutable</warning> = 200

	mut mutable := 100
	mutable = 200

	for i := 0; i < 100; i++ {
		i++ // ok
	}

	if immutable_assign := opt() {
		<warning descr="Immutable variable 'immutable_assign' cannot be reassigned">immutable_assign</warning> = 200
	}

	if mut mutable_assign := opt() {
		mutable_assign = 200 // ok
	}

	boo := Boo{}
	<warning descr="Immutable field 'name' cannot be reassigned"><warning descr="Immutable variable 'boo' cannot be reassigned">boo</warning>.name</warning> = ''
	<warning descr="Immutable variable 'boo' cannot be reassigned">boo</warning>.age = 100

	minified := MinifiedBoo{}
	<warning descr="Immutable variable 'minified' cannot be reassigned">minified</warning>.name = '' // ok
	<warning descr="Immutable variable 'minified' cannot be reassigned">minified</warning>.age = 100 // ok
}
