module struct_fields

struct Name {
	// first *field* in struct
	/*caret*/foo string
}

struct Name1 {
mut:
	// first *field* in struct in group
	/*caret*/foo string
}

struct Name2 {
mut:
	/*caret*/foo string
	// second *field* in struct in group
	/*caret*/boo string
}

struct Name3 {
	/*caret*/boo string // comment after field
}
