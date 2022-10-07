module builtin

// all ok in builtin module
fn PascalCase() {}
fn camelCase() {}
fn snake_case() {}
fn __invalid_snake_case() {}

struct foo {}
interface ifoo {}
union ufoo {}
enum efoo {}

struct f {}
interface i {}
union u {}
enum e {}

type mystring = string
