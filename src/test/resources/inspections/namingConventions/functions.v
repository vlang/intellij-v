module main

fn <error descr="Function name cannot contain uppercase letters, use snake_case instead">PascalCase</error>() {}
fn <error descr="Function name cannot contain uppercase letters, use snake_case instead">camelCase</error>() {}
fn snake_case() {}
fn <error descr="Function name cannot start with '_'">__invalid_snake_case</error>() {}

fn (s string) <error descr="Method name cannot contain uppercase letters, use snake_case instead">PascalCase</error>() {}
fn (s string) <error descr="Method name cannot contain uppercase letters, use snake_case instead">camelCase</error>() {}
fn (s string) snake_case() {}
fn (s string) <error descr="Method name cannot start with '_'">__invalid_snake_case</error>() {}
