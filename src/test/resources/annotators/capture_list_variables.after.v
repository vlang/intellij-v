module annotators

fn main() {
	name := ''
	age := 100

	fn [] (name string) {
		println(name)
	}

	fn(name string) {
		println(name)
	}

	fn [name] (name string) {
		println(name)
	}

	fn [age, name] (name string) {
		println(name)
		println(age)
	}

	fn [age, name] (name string, age int) {
		println(name)
		println(age)
	}
}
