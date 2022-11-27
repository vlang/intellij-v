module rendered

// foo is some function
fn /*caret*/foo() {}

// foo1 _with_ some **markdown** stuff.
// Note: this is --doc-- comment
fn /*caret*/foo1() {}

// foo2 you can add examples.
// Example: assert is_inline_example()
fn /*caret*/foo2() {}

// foo3 you can also add block examples.
// Example:
// ```
// assert is_block_example()
// ```
fn /*caret*/foo3() {}

// foo4 and several as well
// Example:
// ```
// assert is_block_example()
// ```
// Example:
// ```
// assert !is_inline_example()
// ```
fn /*caret*/foo4() {}

// foo5 what about inline and block together?
// Example: assert is_inline_example()
// Example:
// ```
// assert !is_inline_example()
// ```
fn /*caret*/foo5() {}

// foo6 with some crazy whitespaces.
// Note:    aaaaaaaa.
// Example:     assert is_inline_example()
// Example:
// ```
//    assert !is_inline_example()
// ```
fn /*caret*/foo6() {}

// foo7 inline too
// Example: assert is_block_example()
// Example: assert !is_inline_example()
// Example: anything else
fn /*caret*/foo7() {}

// foo8 each line with . or ! or ? is a separate paragraph.
// And this.
// Also this?
// Yes!
// Great.
fn /*caret*/foo8() {}

// foo9 what about multiline examples?
// Example:
// ```v
// fn main() {
// 	mut a := ['hi', '1', '5', '3']
// 	a.sort_with_compare(fn (a &string, b &string) int {
// 		if a < b {
// 			return -1
// 		}
// 		if a > b {
// 			return 1
// 		}
// 		return 0
// 	})
// 	assert a == ['1', '3', '5', 'hi']
// }
// ```
fn /*caret*/foo9() {}
