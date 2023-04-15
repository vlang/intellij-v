module unused

fn declaration(param1 string, param2 int)
fn declaration2(string, int)

fn zero() {}
fn one(<warning descr="Unused parameter 'param1'">param1</warning> string) {}
fn two(param1 string, <warning descr="Unused parameter 'param2'">param2</warning> string) {
	println(param1)
}

struct Foo {}
fn (f Foo) zero() {
	println(f)
}
fn (<warning descr="Unused receiver 'f'">f</warning> Foo) one(<warning descr="Unused parameter 'param1'">param1</warning> string) {}
fn (f Foo) two(param1 string, <warning descr="Unused parameter 'param2'">param2</warning> string) {
	println(param1)
	println(f)
}

fn main() {
	fn (param1 string, <warning descr="Unused parameter 'unused'">unused</warning> int) {
		println(param1)
	}

	fn (param1 string, int) {
		println(param1)
	}
}

fn foo(name string) string {
	return $tmpl('./path.txt')
}

fn foo1(name string) {
	tmpl := $tmpl('./path.txt')
	println(tmpl)
}

fn foo2(name string) string {
	return $vweb.html()
}
