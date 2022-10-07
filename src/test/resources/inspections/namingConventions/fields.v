module main

struct Test {
	<error descr="Field name cannot contain uppercase letters, use snake_case instead">Name</error> string
	<error descr="Field name cannot start with '_'">__name</error> string
	name string
}

enum Colors {
	<error descr="Enum field name cannot contain uppercase letters, use snake_case instead">Red</error>
	<error descr="Enum field name cannot contain uppercase letters, use snake_case instead"><error descr="Enum field name cannot start with '_'">__Blue</error></error>
	<error descr="Enum field name cannot contain uppercase letters, use snake_case instead">yeLLlow</error>
	blue
}
