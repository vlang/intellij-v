module general

struct Foo {
	id   int
	name string
}

fn foo() {
	mut a := <error descr="Result value should have either an `or {}` block, or `!` at the end">sql ss {
		select from Foo
	}</error>
	println(<error descr="Result value should have either an `or {}` block, or `!` at the end">a</error>)

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
