module main

// 'type' as keyword - type alias declarations
type MyInt = int
type MyString = string
pub type PublicAlias = int

// 'type' as identifier - struct field
struct TypeHolder {
	type int
	name string
}

// 'type' as identifier - qualified access
fn test_qualified_access() {
	holder := TypeHolder{}
	println(holder.type)
}

// 'type' as identifier - function name
fn type() int {
	return 1
}

// Multiple type aliases in sequence
type First = int
type Second = string
type Third = bool
