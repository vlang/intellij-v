module main

fn <error descr="Function name cannot contain uppercase letters, use snake_case instead">PascalCase</error>() {}
fn <error descr="Function name cannot contain uppercase letters, use snake_case instead">camelCase</error>() {}
fn snake_case() {}
fn <error descr="Function name cannot start with '_'">__invalid_snake_case</error>() {}
