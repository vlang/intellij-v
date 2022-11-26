module main

struct Array<T> {
}

struct User<T> {
}

struct Foo {
    age int
}

fn main() {
    plain := [Array<string>{}]
    a := plain.map(/*caret*/it)
}
