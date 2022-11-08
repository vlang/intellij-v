module types

struct StructWithoutStrMethod {
	x int
}

fn test_dump_of_type_that_has_no_custom_str_method() {
	s := StructWithoutStrMethod{123}
	expr_type(dump(s), 'StructWithoutStrMethod')
	expr_type(dump(100), 'int')
	expr_type(dump('hello'), 'string')
	expr_type(dump(StructWithoutStrMethod{}), 'StructWithoutStrMethod')
}
