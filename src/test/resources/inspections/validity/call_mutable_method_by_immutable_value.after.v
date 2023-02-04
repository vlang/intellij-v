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
	mut person := Person{}

	person.other.method() // ok
	person.other.mutable_method()

	person.other_mutable.method() // ok
	person.other_mutable.mutable_method() // ok

	person.method() // ok
	person.mutable_method()

	param_person.method() // ok
	person.mutable_method()

	param_person_mutable.method() // ok
	param_person_mutable.mutable_method() // ok

	mut mutable_person := Person{}
	mutable_person.method() // ok
	mutable_person.mutable_method() // ok
}

fn (mut receiver_person Person) method1() {
	receiver_person.method() // ok
	receiver_person.mutable_method()
}

fn (mut mutable_receiver_person Person) method2() {
	mutable_receiver_person.method() // ok
	mutable_receiver_person.mutable_method() // ok
}
