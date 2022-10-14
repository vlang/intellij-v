module types

fn main() {
	s := select {
		100 { return }
	}
	expr_type(s, 'bool')
}
