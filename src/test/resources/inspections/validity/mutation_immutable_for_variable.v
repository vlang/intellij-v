module validity

struct Foo {
mut:
	names []string
}

mut arr := [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

for value in arr {
	<error descr="Immutable variable 'value' cannot be reassigned">value</error> = 100
}

for mut value in arr {
	value = 100
}

mut foo := Foo{}
for value in foo.names {
	<error descr="Immutable variable 'value' cannot be reassigned">value</error> = 'hello'
}

for mut value in foo.names {
	value = 'hello'
}
