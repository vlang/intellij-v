module validity

mut age := 100

fn [age] () {
	<warning descr="Immutably captured variable 'age' cannot be reassigned">age</warning> += 10
	<warning descr="Cannot increment/decrement immutably captured variable 'age'">age</warning>++
}()

fn [mut age] () {
	age += 10
	age++
}()
