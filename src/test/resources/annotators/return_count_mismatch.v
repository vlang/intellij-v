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

fn one_one_2() string {
	return one_one()
}

fn one_one_3() string {
	<error descr="Too many return values, expected 1, got 2">return two_two()</error>
}

fn two_two_2() (string, int) {
	return two_two()
}

fn two_two_3() (string, int) {
	<error descr="Expected 2 return values, but 'one_one()' returns 1'">return one_one()</error>
}

fn two_two_4() (string, int) {
	return one_one(), 10
}

fn three_two_2() (string, int, bool) {
	<error descr="Expected 3 return values, but 'two_two()' returns 2'">return two_two()</error>
}

fn three_two_3() (string, int, bool) {
	return two_two(), true
}

fn three_two_4() (string, int, bool) {
	<error descr="Expected 3 return values, but 'one_one()' returns 1'">return one_one()</error>
}

fn three_two_5() (string, int, bool) {
	<error descr="Not enough return values, expected 3, got 2">return one_one(), 10</error>
}

fn three_two_6() (string, int, bool) {
	return one_one(), 10, true
}

fn three_two_7() (string, int, bool) {
	return one_one(), int(10), true
}

fn option_two() ?(string, int) {
	return none
}

fn option_two_2() ?(string, int) {
	return "1", 2
}

fn option_three() ?(string, int, bool) {
	return none
}

fn option_three_2() ?(string, int, bool) {
	return "1", 2, true
}
