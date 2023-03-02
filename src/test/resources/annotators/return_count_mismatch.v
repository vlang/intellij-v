module annotators

fn one_two() string {
	<error descr="Too many return values, expected 1, got 2">return '', 10</error>
}

fn two_two() (string, int) {
	return '', 10
}

fn two_one() (string, int) {
	<error descr="Not enough return values, expected 2, got 1">return ''</error>
}

fn one_one() string {
	return ''
}

fn one_zero() string {
	<error descr="Not enough return values, expected 1, got 0">return</error>
}

fn zero_zero() {
	return
}

fn opt_zero() ? {
	return
}

fn opt_one() ? {
	return none
}

fn opt_two() ? {
	<error descr="Too many return values, expected 0 or 1, got 2">return 100, 100</error>
}

fn res_zero() ! {
	return
}

fn res_one() ! {
	return error('')
}

fn res_two() ! {
	<error descr="Too many return values, expected 0 or 1, got 2">return 100, 100</error>
}

fn single_paren_res() <weak_warning descr="Redundant parentheses">(string)</weak_warning> {
	return ''
}
