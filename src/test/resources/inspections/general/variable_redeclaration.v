module main

fn foo1() ?int {
	return 1
}

fn main() {
	age := 100
	<error descr="Variable 'age' is already defined on line 8">age</error> := 100

	_ := 100
	_ := 100

	if true {
		for <error descr="Variable 'age' is already defined on line 8">age</error> in [1, 2, 3] {
			// ...
		}

		for <error descr="Variable 'age' is already defined on line 8">age</error> := 0; age < 100; age++ {
			// ...
			_ := 100
		}

		for <error descr="Variable 'age' is already defined on line 8">age</error>, <error descr="Variable 'age' is already defined on line 24">age</error> in [1, 2, 3] {
			// ...
		}

		for i := 0; i < 100; i++ {
			<error descr="Variable 'age' is already defined on line 8">age</error> := 100
		}
	}

	if foo := foo1() {
		if <error descr="Variable 'foo' is already defined on line 33">foo</error> := foo1() {
			// ...
		}
	}

	if foo := foo1() {
		// ...
	}

	name := ''

	fn () {
		age := 100
		name := ''
	}

	fn [age] () {
		<error descr="Variable 'age' is already defined on line 8">age</error> := 100
		name := ''
	}

	fn [age, name] () {
		<error descr="Variable 'age' is already defined on line 8">age</error> := 100
		<error descr="Variable 'name' is already defined on line 43">name</error> := ''
	}

	fn (age int, name string) {
		<error descr="Variable 'age' is already defined as parameter on line 60">age</error> := 100
		<error descr="Variable 'name' is already defined as parameter on line 60">name</error> := ''
	}
}

fn func(param int) {
	<error descr="Variable 'param' is already defined as parameter on line 66">param</error> := 100
}

fn (s string) method(param int) {
	<error descr="Variable 's' is already defined as receiver on line 70">s</error> := 100
	<error descr="Variable 'param' is already defined as parameter on line 70">param</error> := 100

	if true {
		<error descr="Variable 'param' is already defined on line 72">param</error> := 100
	}
}
