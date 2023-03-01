module probableBugs

fn foo() ?int {
	return 0
}

fn main(param int) {
	age := 100
	name := ''

	mut f := fn () {
		println(<error descr="Variable 'age' is not captured">age</error>)
		println(<error descr="Variable 'name' is not captured">name</error>)
		println(<error descr="Parameter 'param' is not captured">param</error>)
	}

	f = fn [age] () {
		println(age)
		println(<error descr="Variable 'name' is not captured">name</error>)
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
			f = fn () {
				println(<error descr="Variable 'i' is not captured">i</error>)
				println(<error descr="Variable 'name' is not captured">name</error>)
			}

			f = fn () {
				i := 100

				println(i)
				println(<error descr="Variable 'name' is not captured">name</error>)
			}
		}
	}
}
