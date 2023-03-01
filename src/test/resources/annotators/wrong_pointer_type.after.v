module validity

fn foo1() &int {
	return unsafe { nil }
}

fn foo2() **int {
	return unsafe { nil }
}

fn foo3() &int {
	return unsafe { nil }
}

fn foo4() &&int {
	return unsafe { nil }
}
