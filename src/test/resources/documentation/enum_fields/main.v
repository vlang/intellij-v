module enum_fields

[flag]
enum WithFlagEnum {
	/*caret*/red
	/*caret*/other
	/*caret*/blue
}

enum WithComplexValue {
	/*caret*/red = -1 << 10
	/*caret*/other
	/*caret*/blue
}

enum WithDifferentValues {
	/*caret*/red = 1
	/*caret*/other
	/*caret*/blue = 1 << 1
}

enum WithTooComplexValues {
	/*caret*/blue = (1 << 1) + 10 * 156
}
