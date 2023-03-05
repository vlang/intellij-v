module interfaces

interface /*caret*/Empty {}

// Field is interface
interface /*caret*/Field {
	foo ?string
}

interface /*caret*/Method {
	foo() string
}

interface /*caret*/MethodAndField {
	bar string
	foo() (string, int)
}

interface /*caret*/SeveralFields {
	foo string
	b string
}

interface /*caret*/SeveralMethods {
	foo() string
	bar(f string, b int, f bool, i f64) string
}

// SeveralMethodsAndFields is interface
interface /*caret*/SeveralMethodsAndFields {
	foo string
	b string
	foo(age int) string
	bar() string
}

interface /*caret*/WithEmbedded {
	SeveralMethodsAndFields
	foo !string
}

interface /*caret*/WithOwnMethods {
	SeveralMethodsAndFields
	foo !string
}

fn (m WithOwnMethods) some_method() {}
