module interface_members

interface Name {
	// first *field* in struct
	/*caret*/foo string
}

interface Name1 {
mut:
	// first *field* in struct in group
	/*caret*/foo string
}

interface Name2 {
mut:
	/*caret*/foo string
	// second *field* in struct in group
	/*caret*/boo string
}

interface Name3 {
	/*caret*/boo string // comment after field
}

interface Name4 {
	// first *method* in struct
	/*caret*/method()
}

interface Name4 {
	/*caret*/method() // comment after method
}
