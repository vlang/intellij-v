module type_methods

struct /*caret*/Struct {
}

fn (s &Struct) method() {}
fn (s &Struct) method1(str string) int {}

interface /*caret*/Interface {}

fn (i &Interface) method() {}

enum /*caret*/Enum {
	a
}

fn (e Enum) method() {}

type /*caret*/Type = int

fn (t Type) method() {}

type /*caret*/Sum = int | string

fn (s Sum) method() {}

fn foo() {
	t := /*caret*/Type(1)
	su := /*caret*/Sum(1)
}