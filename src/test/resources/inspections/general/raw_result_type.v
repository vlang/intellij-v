module general

struct Foo {
	id   int
	name string
}

fn foo() {
	mut a := <warning descr="Result value should have either an `or {}` block, or `!` at the end">sql ss {
		select from Foo
	}</warning>
	println(<warning descr="Result value should have either an `or {}` block, or `!` at the end">a</warning>)

	b := sql ss {
		select from Foo
	}!
	println(b)

	c := sql ss {
		select from Foo
	} or {
		panic('')
	}
	println(c)
}
