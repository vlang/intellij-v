module builtin

// all ok in builtin module
fn PascalCase() {}
fn camelCase() {}
fn snake_case() {}
fn __invalid_snake_case() {}

fn (s string) PascalCase() {}
fn (s string) camelCase() {}
fn (s string) snake_case() {}
fn (s string) __invalid_snake_case() {}

struct foo {}
interface ifoo {}
union ufoo {}
enum efoo {
	red
}

struct f {}
interface i {}
union u {}
enum e {
	red
}

type mystring = string
