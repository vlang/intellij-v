module types

struct Foo {}

__global name = 100
__global foo = Foo{}

__global (
	tp string
	pt voidptr
)

expr_type(name, 'int')
expr_type(foo, 'Foo')
expr_type(tp, 'string')
expr_type(pt, 'voidptr')
