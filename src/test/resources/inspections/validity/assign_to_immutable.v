module validity

const constant = 100

struct Boo {}

fn (b Boo) foo() {
	<warning descr="Immutable receiver 'b' cannot be reassigned">b</warning> = Boo{}
}

fn (mut b Boo) mut_foo() {
	b = Boo{}
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
}
