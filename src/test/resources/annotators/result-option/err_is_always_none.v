module annotators

fn res() !int {
	return error("err")
}

fn opt() ?int {
	return none
}

struct Foo {
	opt_field ?string
}

fn main() {
	strukt := Foo{}

	opt() or {
		println(<weak_warning descr="'err' is always 'none', since 'opt()' returns Option type">err</weak_warning>)
	}

	if val := opt() {
		// ...
	} else {
		println(<weak_warning descr="'err' is always 'none', since 'opt()' returns Option type">err</weak_warning>)
	}


	res() or {
		println(err)

		opt() or {
			println(<weak_warning descr="'err' is always 'none', since 'opt()' returns Option type">err</weak_warning>)
		}
	}

	if res := res() {
		// ...
	} else {
		println(err)
	}

	strukt.opt_field or {
		println(<weak_warning descr="'err' is always 'none', since 'strukt.opt_field' has Option type">err</weak_warning>)
	}

	ch := chan int{}

	v := <-ch or {
		println(<weak_warning descr="'err' is always 'none', since 'ch' has Option type">err</weak_warning>)
	}
}
