module annotators

fn main() {
	name := ''
	age := 100

	fn <error descr="Capture list cannot be empty">[]</error> (name string) {
		println(name)
	}

	fn<error descr="Capture list cannot be empty"><caret>[]</error>(name string) {
		println(name)
	}

	fn [<error descr="Capture variable 'name' duplicates parameter 'name'">name</error>] (name string) {
		println(name)
	}

	fn [age, <error descr="Capture variable 'name' duplicates parameter 'name'">name</error>] (name string) {
		println(name)
		println(age)
	}

	fn [<error descr="Capture variable 'age' duplicates parameter 'age'">age</error>, <error descr="Capture variable 'name' duplicates parameter 'name'">name</error>] (name string, age int) {
		println(name)
		println(age)
	}
}
