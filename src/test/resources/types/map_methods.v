module types

struct Foo {}

fn main() {
	mp := map[string][]Foo{}
	keys := mp.keys()
	expr_type(keys, "[]string")
	values := mp.values()
	expr_type(values, "[][]Foo")
	copy := mp.clone()
	expr_type(copy, "map[string][]Foo")
	move := mp.move()
	expr_type(move, "map[string][]Foo")
}
