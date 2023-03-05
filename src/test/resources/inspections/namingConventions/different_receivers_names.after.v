module namingConventions

import sokol.f

struct Thing {
	name string
}

fn (thing &Thing) method() {
	println(thing.name)
}

fn (thing Thing) method1() {
	println(thing.name)
}

fn (thing &Thing) method2() {
	println(thing.name)
}
