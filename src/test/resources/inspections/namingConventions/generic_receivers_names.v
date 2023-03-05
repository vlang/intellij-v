module namingConventions

struct Thing {
	name string
}

fn (<warning descr="Receiver has a generic name"><warning descr="Receiver names are different">this</warning></warning> &Thing) method() {
	println(this.name)
}

fn (<warning descr="Receiver has a generic name"><warning descr="Receiver names are different">me</warning></warning> Thing) method1() {
	println(me.name)
}

fn (<warning descr="Receiver has a generic name"><warning descr="Receiver names are different">self</warning></warning> Thing) method2() {
	println(self.name)
}
