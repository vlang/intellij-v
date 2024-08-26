module validity

mut age := 100

fn [age] () {
	<error descr="Immutably captured variable 'age' cannot be reassigned">age</error> += 10
	<error descr="Cannot increment/decrement immutably captured variable 'age'">age</error>++
}()

fn [mut age] () {
	age += 10
	age++
}()
