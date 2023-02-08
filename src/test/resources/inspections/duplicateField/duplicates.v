module duplicateField

// structs

struct STest {
	name string
	<error descr="Duplicate field 'name'">name</error> string
mut:
	age int
pub:
	<error descr="Duplicate field 'age'">age</error> int
}

struct SInner {
	name string
}

struct SWithInner {
	SInner
	name string // ok
}

// unions

union UTest {
	name string
	<error descr="Duplicate field 'name'">name</error> string
mut:
	age int
pub:
	<error descr="Duplicate field 'age'">age</error> int
}

union UInner {
	name string
}

union UWithInner {
	UInner
	name string // ok
}

// interfaces

interface ITest {
	name string
	<error descr="Duplicate field 'name'">name</error> string
}

interface IInner {
	name string
}

interface IWithInner {
	IInner
	name string // ok, for now
}

// enums

enum ETest {
	name
	<error descr="Duplicate field 'name'">name</error>
}
