module validity

struct Person {
	name string
	age int

	other &Person
mut:
	other_mutable &Person
}

fn (n &Person) method()

fn (mut n Person) mutable_method()

fn main(param_person Person, mut param_person_mutable Person) {
	person := Person{}

	person.other.method() // ok
	person.other.<warning descr="Cannot call mutable method 'mutable_method' on immutable value">mutable_method</warning>()

	person.other_mutable.method() // ok
	person.other_mutable.mutable_method() // ok

	person.method() // ok
	person.<warning descr="Cannot call mutable method 'mutable_method' on immutable value">mutable_method</warning>()

	param_person.method() // ok
	person.<warning descr="Cannot call mutable method 'mutable_method' on immutable value">mutable_method</warning>()

	param_person_mutable.method() // ok
	param_person_mutable.mutable_method() // ok

	mut mutable_person := Person{}
	mutable_person.method() // ok
	mutable_person.mutable_method() // ok
}

fn (receiver_person &Person) method1() {
	receiver_person.method() // ok
	receiver_person.<warning descr="Cannot call mutable method 'mutable_method' on immutable value">mutable_method</warning>()
}

fn (mutable_receiver_person &Person) method2() {
	mutable_receiver_person.method() // ok
	mutable_receiver_person.<warning descr="Cannot call mutable method 'mutable_method' on immutable value">mutable_method</warning>() // ok
}
