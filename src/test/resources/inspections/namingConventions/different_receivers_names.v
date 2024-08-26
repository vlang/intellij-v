module namingConventions

import sokol.f

struct Thing {
	name string
}

fn (<weak_warning descr="Receiver names are different">foo</weak_warning> &Thing) method() {
	println(foo.name)
}

fn (<weak_warning descr="Receiver names are different">fo</weak_warning> Thing) method1() {
	println(fo.name)
}

fn (<weak_warning descr="Receiver names are different">thing</weak_warning> &Thing) method2() {
	println(thing.name)
}
