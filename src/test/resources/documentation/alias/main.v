module alias

struct Some<T> {}
struct Some2<T, U> {}

type /*caret*/Foo = int

[typedef]
type /*caret*/Foo1<T> = Some<T>
pub type /*caret*/Foo2<T, U> = Some2<T, U>
