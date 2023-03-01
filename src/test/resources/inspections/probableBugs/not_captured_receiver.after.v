module probableBugs

struct Foo {
	name string
}

fn (f &Foo) method() {
	spawn fn [f] () {
		println(f.name)
	}()
}

fn (mut f Foo) method2() {
	spawn fn [f] () {
		println(f.name)
	}()
}
