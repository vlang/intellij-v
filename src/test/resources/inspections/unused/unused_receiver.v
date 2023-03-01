module unused

struct Foo {}

fn (f &Foo) method() {
	println(f)
}

fn (<warning descr="Unused receiver 'f'">f</warning> &Foo) method2() {
}

fn (_ &Foo) method3() {
}
