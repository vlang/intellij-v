module general

struct TrailingArgs {
	name string
	age  int
}

[params]
struct ParamsTrailingArgs {
	name string
	age  int
}

fn single_arg(a int) {}

fn several_arg(a int, b int) {}

fn only_variadic_arg(arr ...int) {}

fn variadic_arg_with_single_common(a int, arr ...int) {}

fn with_only_trailing_struct(str TrailingArgs) {}

fn with_trailing_struct_and_simple_param(a int, str TrailingArgs) {}

fn with_only_params_trailing_struct(str ParamsTrailingArgs) {}

fn main() {
	<error descr="Not enough arguments in call to 'single_arg', expected at least 1 arguments, but got 0">single_arg</error>()
	single_arg(1)
	<error descr="Not enough arguments in call to 'several_arg', expected at least 2 arguments, but got 0">several_arg</error>()
	<error descr="Not enough arguments in call to 'several_arg', expected at least 2 arguments, but got 1">several_arg</error>(1)
	several_arg(1, 2)
	only_variadic_arg()
	only_variadic_arg(1)
	only_variadic_arg(1, 2, 3)
	<error descr="Not enough arguments in call to 'variadic_arg_with_single_common', expected at least 1 arguments, got 0">variadic_arg_with_single_common</error>()
	variadic_arg_with_single_common(1)
	variadic_arg_with_single_common(1, 2)
	variadic_arg_with_single_common(1, 2, 3)
	<error descr="Not enough arguments in call to 'with_only_trailing_struct', expected at least 1 arguments, but got 0">with_only_trailing_struct</error>()
	with_only_trailing_struct(TrailingArgs{})
	with_only_trailing_struct(name: '')
	with_only_trailing_struct(name: '', age: 100)
	<error descr="Too many arguments in call to 'with_only_trailing_struct', unexpected trailing struct params after last argument">with_only_trailing_struct</error>(100, name: '')
	<error descr="Too many arguments in call to 'with_only_trailing_struct', expected 1 arguments before trailing struct params, but got 2">with_only_trailing_struct</error>(100, 200, name: '')
	<error descr="Not enough arguments in call to 'with_trailing_struct_and_simple_param', expected at least 2 arguments, but got 1">with_trailing_struct_and_simple_param</error>(100)
	with_trailing_struct_and_simple_param(100, TrailingArgs{})
	with_trailing_struct_and_simple_param(100, name: '')
	with_trailing_struct_and_simple_param(100, name: '', age: 100)
	<error descr="Not enough arguments in call to 'with_trailing_struct_and_simple_param', expected at least 1 arguments before trailing struct params, but got 0">with_trailing_struct_and_simple_param</error>(name: '', age: 100)
	with_only_params_trailing_struct()
	with_only_params_trailing_struct(ParamsTrailingArgs{})
	with_only_params_trailing_struct(name: '')
	with_only_params_trailing_struct(name: '', age: 100)
	<error descr="Too many arguments in call to 'with_only_params_trailing_struct', unexpected trailing struct params after last argument">with_only_params_trailing_struct</error>(100, name: '')
	<error descr="Too many arguments in call to 'with_only_params_trailing_struct', expected 1 arguments before trailing struct params, but got 2">with_only_params_trailing_struct</error>(100, 200, name: '')
	several_arg(...[1, 2, 3])
	only_variadic_arg(...[1, 2, 3])
	only_variadic_arg(1, 2, 3...[1, 2, 3])
}
