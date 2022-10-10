module validity

const constant = 100

struct Boo {}

fn (mut b Boo) foo() {
	b = Boo{}
}

fn (mut b Boo) mut_foo() {
	b = Boo{}
}

fn main() {
	constant = 200

	mut immutable := 100
	immutable = 200

	mut mutable := 100
	mutable = 200

	for i := 0; i < 100; i++ {
		i++ // ok
	}

	if mut immutable_assign := 100 {
		immutable_assign = 200
	}

	if mut mutable_assign := 100 {
		mutable_assign = 200 // ok
	}
}
