module probableBugs

fn foo() ?int {
	return 0
}

fn main(param int) {
	age := 100
	name := ''

	mut f := fn [age, name, param] () {
		println(age)
		println(name)
		println(param)
	}

	f = fn [age, name] () {
		println(age)
		println(name)
	}

	f = fn [age] () {
		name := ''
		println(age)
		println(name)
	}

	f = fn [age, name] () {
		println(age)
		println(name)
	}

	if name := foo() {
		for i := 0; i < 100; i++ {
			f = fn [i, name] () {
				println(i)
				println(name)
			}

			f = fn [name] () {
				i := 100

				println(i)
				println(name)
			}
		}
	}
}
