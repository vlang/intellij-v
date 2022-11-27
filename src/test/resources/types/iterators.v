module types

pub struct Counter {
}

pub fn (mut c Counter) next() f64 {
	return 0.0
}

pub struct Counter2 {
}

pub fn (mut c Counter2) next() ?[]f64 {
	return []
}

pub struct Counter3<T> {
}

pub fn (mut c Counter3<T>) next() ?T {
	return []
}

fn main() {
	c := Counter{}
	for value in c {
		expr_type(value, 'f64')
	}

	c2 := Counter2{}
	for value in c2 {
		expr_type(value, '[]f64')
	}

	c3 := Counter3<Counter3<int>>{}
	for value in c3 {
		expr_type(value, 'Counter3[int]')
		for value1 in value {
			expr_type(value1, 'int')
		}
	}
}
