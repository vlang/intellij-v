module parser

fn main() {
	x := 123.4567
	println('[${x:.2}]')
	println('[${x:10}]')
	println('[${int(x):-10}]')
	println('[${int(x):010}]')
	println('[${int(x):b}]')
	println('[${int(x):o}]')
	println('[${int(x):X}]')

	println('[${10.0000:.2}]')
	println('[${10.0000:+.2f}]')

	args := ['']
	res := 100
	data := 100
	name := 100
	println('${args[0]} failed with return code ${res.exit_code}.\n$res.output')
	println('$name.field')
	println('$name.field.field2')
	println('$name.method()') // TODO: support
	println('$name.method[100]') // TODO: support

	println('< client read back buf: |${buf[0..data.len].bytestr()}|')
	println(' ${any_to_json(val)},')
	// println(' ${any_to_json("data")},') // TODO: support
	// println(' ${any_to_json('data')},') // TODO: support
}
