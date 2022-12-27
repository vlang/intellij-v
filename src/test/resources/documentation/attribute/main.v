module attribute

[unsafe]
[sql: 'hello'; hello; callconv: 'bla']
[if debug ?; 'hello'; hello; callconv: 'bla']
['hello']
[100; true]
fn /*caret*/main() {}
