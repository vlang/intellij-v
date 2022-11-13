module validity

const constant = 100

interface IFoo {
	immutable int
mut:
	mutable int
}

fn (b IFoo) foo() {
	b.<warning descr="Immutable field 'immutable' cannot be reassigned">immutable</warning> = 100
	b.mutable = 100
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
	b.<warning descr="Immutable field 'name' cannot be reassigned">name</warning> = ''
	b.age = 100
}

fn (mut b Boo) mut_foo(mut p string) {
	b = Boo{}
	p = ''
}

fn main() {
	<warning descr="Constant 'constant' cannot be reassigned">constant</warning> = 200

	immutable := 100
	<warning descr="Immutable variable 'immutable' cannot be reassigned">immutable</warning> = 200

	mut mutable := 100
	mutable = 200

	for i := 0; i < 100; i++ {
		i++ // ok
	}

	if immutable_assign := 100 {
		<warning descr="Immutable variable 'immutable_assign' cannot be reassigned">immutable_assign</warning> = 200
	}

	if mut mutable_assign := 100 {
		mutable_assign = 200 // ok
	}

	boo := Boo{}
	boo.<warning descr="Immutable field 'name' cannot be reassigned">name</warning> = ''
	boo.age = 100

	minified := MinifiedBoo{}
	minified.name = '' // ok
	minified.age = 100 // ok
}
