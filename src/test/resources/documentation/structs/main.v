module main

struct /*caret*/Empty {}

struct /*caret*/Simple {
	name string
	age  int
}

struct /*caret*/WithPub {
pub:
	name string
	age  int
}

struct /*caret*/WithPubMut {
pub mut:
	name string
	age  int
}

struct /*caret*/WithPubMutGroups {
pub:
	name string
mut:
	age int
}

struct /*caret*/WithDefaultValue {
pub:
	name string = "Hello"
mut:
	age int = 100 + 200
}

struct /*caret*/WithAttributes {
pub:
	name string [sql: 'Name']
mut:
	age int [required; sql: 'Age']
}

struct /*caret*/WithEmbedded {
	WithAttributes
pub:
	name string [sql: 'Name']
mut:
	age int [required; sql: 'Age']
}

struct /*caret*/WithOnlyEmbedded {
	WithAttributes
}

struct /*caret*/WithSeveralEmbedded {
	WithAttributes
	WithDefaultValue
}
