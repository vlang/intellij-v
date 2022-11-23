module function

struct Some<T> {}
struct Some2<T, U> {}

type /*caret*/Foo = int
type /*caret*/Foo1<T> = Some<T>
type /*caret*/Foo2<T, U> = Some2<T, U>
