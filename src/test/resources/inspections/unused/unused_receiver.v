module unused

struct Foo {}

fn (f &Foo) method() {
	println(f)
}

fn (<warning descr="Unused receiver 'f'">f</warning> &Foo) method2() {
}

fn (_ &Foo) method3() {
}

fn (f &Foo) method4() string {
	return $tmpl('./path.txt')
}

fn (f &Foo) method5() {
	tmpl := $tmpl('./path.txt')
	println(tmpl)
}

fn (f &Foo) method6() string {
	return $vweb.html()
}
