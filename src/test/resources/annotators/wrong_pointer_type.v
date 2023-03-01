module validity

fn foo1() <error descr="Use '&T' instead of '*T' to define a pointer type"><caret>*int</error> {
	return unsafe { nil }
}

fn foo2() <error descr="Use '&T' instead of '*T' to define a pointer type">**int</error> {
	return unsafe { nil }
}

fn foo3() &int {
	return unsafe { nil }
}

fn foo4() &&int {
	return unsafe { nil }
}
