module new

[minify]
type Foo = int

[minify]
[name]
[name2: 'name']
[sevaral; attributes]
struct Name {
	name string [sql]
	age  int    [default: 0]
	tag  string [default: nil]
	baz  bool   [prefix.attr0] = false
	name1 string [dot.name]
	name2 string [name: 100]
	name3 string [sql: 100]
	name4 string [unsafe: 100]
}

[minify]
[if debug ?]
fn foo() {

}
