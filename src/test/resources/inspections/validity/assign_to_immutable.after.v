module validity

const constant = 100

interface IFoo {
	immutable int
mut:
	mutable int
}

fn (mut b IFoo) foo() {
	b.immutable = 100
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

fn (mut b Boo) foo(mut p string) {
	b = Boo{}
	p = ''
	b.name = ''
	b.age = 100
}

fn (mut b Boo) mut_foo(mut p string) {
	b = Boo{}
	b.name = ''
	p = ''
}

fn (mut b Boo) foo2() {
	b.name = ''
}

const boo = Boo{}
boo.name = ''

fn opt() ?int {}

fn main() {
	constant = 200

	mut immutable := 100
	immutable = 200

	mut mutable := 100
	mutable = 200

	for i := 0; i < 100; i++ {
		i++ // ok
	}

	if mut immutable_assign := opt() {
		immutable_assign = 200
	}

	if mut mutable_assign := opt() {
		mutable_assign = 200 // ok
	}

	mut boo := Boo{}
	boo.name = ''
	boo.age = 100

	mut minified := MinifiedBoo{}
	minified.name = '' // ok
	minified.age = 100 // ok
}
