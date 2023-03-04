module embedded_definition

pub struct Name {
pub:
	name string
}

struct Foo {
	/*caret*/Name
	age int
}

