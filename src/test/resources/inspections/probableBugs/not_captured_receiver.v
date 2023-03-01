module probableBugs

struct Foo {
	name string
}

fn (f &Foo) method() {
	spawn fn () {
		println(<error descr="Receiver 'f' is not captured">f</error>.name)
	}()
}

fn (mut f Foo) method2() {
	spawn fn () {
		println(<error descr="Receiver 'f' is not captured">f</error>.name)
	}()
}
