module namingConventions

import sokol.f

struct Thing {
	name string
}

fn (<warning descr="Receiver names are different">foo</warning> &Thing) method() {
	println(foo.name)
}

fn (<warning descr="Receiver names are different">fo</warning> Thing) method1() {
	println(fo.name)
}

fn (<warning descr="Receiver names are different">thing</warning> &Thing) method2() {
	println(thing.name)
}
