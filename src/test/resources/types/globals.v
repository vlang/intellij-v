module types

struct Foo {}

__global name = 100
__global foo = Foo{}

expr_type(name, "int")
expr_type(foo, "Foo")
