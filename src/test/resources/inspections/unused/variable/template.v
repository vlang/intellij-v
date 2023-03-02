
fn foo() string {
	name := 'foo'
	age := 10

	return $tmpl('./path.txt')
}

fn foo1() {
	name := 'foo'
	age := 10

	tmpl := $tmpl('./path.txt')
	println(tmpl)
}

fn foo2() string {
	name := 'foo'
	age := 10

	return $vweb.html()
}
