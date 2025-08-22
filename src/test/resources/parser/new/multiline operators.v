module main

fn add(a int, b int) int {
	return a
	+
	b
}

fn main() {
	println(
		'Hello, World!'
		+
		'newline'
	)

	println(
	true || false &&
		true
		&& false
		|| true
		|| false && true
	)

	println(
		1
		==
		1
		&&
		1
		!=
		2
		||
		1
		<
		1
		||
		2
		>
		1
		&&
		1
		<=
		2
		||
		2
		>=
		1
	)

	println(
		2
		^
		8
		+
		1
		%
		10
		/
		13
		*
		1
	)

	println(
		1
		<<
		10
		>>
		10
		>>>
		1
		&
		0xff
		|
		0x0f
	)

	add(
		6
		-
		1,
		3
		-
		1
	)


}