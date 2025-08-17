module new

@[minify]
type Foo = int

@[minify]
@[name] @[in_one_line]
@[name2: 'name']
@[sevaral; attributes]
struct Name {
	name string @[sql]
	age  int    @[default: 0]
	tag  string @[default: nil]
	baz  bool = false   @[prefix.attr0]
	name1 string @[dot.name]
	name2 string @[name: 100]
	name3 string @[sql: 100]
	name4 string @[unsafe; value: 100]
}

@[minify]
@[if debug ?]
fn foo() {

}
