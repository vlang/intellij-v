module types

type MyMap = map[string]int

fn main() {
	arr := [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
	expr_type(arr[1], 'int')

	mp := {'a': 1, 'b': 2, 'c': 3}
	expr_type(mp['a'], 'int')

	str := 'hello world'
	expr_type(str[1], 'u8')

	mm := MyMap{'a': 1, 'b': 2, 'c': 3}
	expr_type(mm['a'], 'int')

	char_pointer := charptr(0)
	expr_type(char_pointer[0], 'char')

	byte_pointer := byteptr(0)
	expr_type(byte_pointer[0], 'byte')
}

fn foo(m &map[string]int) {
	val := m['']
	expr_type(val, 'int')
}

fn goo(a &[]string) {
	val := a[0]
	expr_type(val, 'string')
}
