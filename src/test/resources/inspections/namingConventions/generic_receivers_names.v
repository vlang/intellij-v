module namingConventions

struct Thing {
	name string
}

fn (<weak_warning descr="Receiver has a generic name"><weak_warning descr="Receiver names are different">this</weak_warning></weak_warning> &Thing) method() {
	println(this.name)
}

fn (<weak_warning descr="Receiver has a generic name"><weak_warning descr="Receiver names are different">me</weak_warning></weak_warning> Thing) method1() {
	println(me.name)
}

fn (<weak_warning descr="Receiver has a generic name"><weak_warning descr="Receiver names are different">self</weak_warning></weak_warning> Thing) method2() {
	println(self.name)
}
