module main

struct Foo {
	name string
	surname ?string
}

fn foo() int {}
fn opt() ?int {}
fn res() !int {}

foo1 := Foo{}

foo1 <error descr="Left expression of 'or' operator must be 'Option' or 'Result' type or '<-chan' or 'arr[i]' expression">or</error> {}
100 <error descr="Left expression of 'or' operator must be 'Option' or 'Result' type or '<-chan' or 'arr[i]' expression">or</error> {}
"" <error descr="Left expression of 'or' operator must be 'Option' or 'Result' type or '<-chan' or 'arr[i]' expression">or</error> {}
foo() <error descr="Left expression of 'or' operator must be 'Option' or 'Result' type or '<-chan' or 'arr[i]' expression">or</error> {}
opt() or {}
res() or {}
foo1.name <error descr="Left expression of 'or' operator must be 'Option' or 'Result' type or '<-chan' or 'arr[i]' expression">or</error> {}
foo1.surname or {}

sql db {

} or {

}
