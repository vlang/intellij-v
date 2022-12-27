<warning descr="Unused variable 'a'">a</warning> := 100
b := 200

if true {
	println(b)
}

fn main() {
	<warning descr="Unused variable 'c'">c</warning> := 300
	d := 400

	if true {
		println(d)
	}
}
