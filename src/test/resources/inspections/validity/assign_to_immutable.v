module validity

const constant = 100

interface IFoo {
	immutable int
mut:
	mutable int
}

fn (b IFoo) foo() {
	<error descr="Immutable field 'immutable' cannot be reassigned"><error descr="Immutable receiver 'b' cannot be reassigned">b</error>.immutable</error> = 100
	<error descr="Immutable receiver 'b' cannot be reassigned">b</error>.mutable = 100
}

struct Boo {
	name string
mut:
	age int
}

@[minify]
struct MinifiedBoo {
	name string
	age int
}

fn (b Boo) foo(p string) {
	<error descr="Immutable receiver 'b' cannot be reassigned">b</error> = Boo{}
	<error descr="Immutable parameter 'p' cannot be reassigned">p</error> = ''
	<error descr="Immutable field 'name' cannot be reassigned"><error descr="Immutable receiver 'b' cannot be reassigned">b</error>.name</error> = ''
	<error descr="Immutable receiver 'b' cannot be reassigned">b</error>.age = 100
}

fn (mut b Boo) mut_foo(mut p string) {
	b = Boo{}
	<error descr="Immutable field 'name' cannot be reassigned">b.name</error> = ''
	p = ''
}

fn (b Boo) foo2() {
	<error descr="Immutable field 'name' cannot be reassigned"><error descr="Immutable receiver 'b' cannot be reassigned">b</error>.name</error> = ''
}

const boo = Boo{}
<error descr="Immutable field 'name' cannot be reassigned"><error descr="Constant 'boo' cannot be reassigned">boo</error>.name</error> = ''

fn opt() ?int {}

fn main() {
	<error descr="Constant 'constant' cannot be reassigned">constant</error> = 200

	immutable := 100
	<error descr="Immutable variable 'immutable' cannot be reassigned">immutable</error> = 200

	mut mutable := 100
	mutable = 200

	for i := 0; i < 100; i++ {
		i++ // ok
	}

	if immutable_assign := opt() {
		<error descr="Immutable variable 'immutable_assign' cannot be reassigned">immutable_assign</error> = 200
	}

	if mut mutable_assign := opt() {
		mutable_assign = 200 // ok
	}

	boo := Boo{}
	<error descr="Immutable field 'name' cannot be reassigned"><error descr="Immutable variable 'boo' cannot be reassigned">boo</error>.name</error> = ''
	<error descr="Immutable variable 'boo' cannot be reassigned">boo</error>.age = 100

	minified := MinifiedBoo{}
	<error descr="Immutable variable 'minified' cannot be reassigned">minified</error>.name = '' // ok
	<error descr="Immutable variable 'minified' cannot be reassigned">minified</error>.age = 100 // ok
}
