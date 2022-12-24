module types

interface IFoo {
	foo string
}

struct Foo {
	foo string
}

struct GenericFoo[T] {
	foo string
}

expr_type(int(100), 'int')
expr_type(i64(100), 'i64')

fn some(foo IFoo) {
	f := Foo(foo)
	expr_type(f, 'Foo')

	f1 := GenericFoo[int](foo)
	expr_type(f1, 'GenericFoo[int]')

	f2 := &GenericFoo[string](foo)
	expr_type(f2, '&GenericFoo[string]')
	f2p := *f2
	expr_type(f2p, 'GenericFoo[string]')
}
