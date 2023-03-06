module main

struct Foo {
	name string
	surname ?string
}

fn foo() int {}
fn opt() ?int {}
fn res() !int {}

foo1 := Foo{}

foo1!
foo1?
foo1.name!
foo1.name?
foo1.surname!
foo1.surname?
<error descr="Cannot use propagation '!' for non-Option and non-Result type">foo()!</error>
<error descr="Cannot use propagation '?' for non-Option and non-Result type">foo()?</error>
<warning descr="Use '?' to propagate the Option instead of '!'">opt()!</warning>
opt()?
res()!
<warning descr="Use '!' to propagate the Result instead of '?'">res()?</warning>
