module annotators

struct Foo {}

fn take_one_mutable(mut x Foo) {}

mut f := Foo{}
take_one_mutable(<error descr="Use `mut f` instead of `f` to pass a mutable argument">f</error>)
take_one_mutable(mut f)
take_one_mutable(Foo{}) // ok for now

fn take_two_mutable(mut x Foo, mut x Foo) {}

take_two_mutable(<error descr="Use `mut f` instead of `f` to pass a mutable argument">f</error>, <error descr="Use `mut f` instead of `f` to pass a mutable argument">f</error>)
take_two_mutable(mut f, <error descr="Use `mut f` instead of `f` to pass a mutable argument">f</error>)
take_two_mutable(mut f, mut f)
