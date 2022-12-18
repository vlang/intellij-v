module sum

struct Foo {}
struct Some<T> {}
struct Some2<T, U> {}

type /*caret*/String = string
type /*caret*/StringOrInt = string | int

[typedef]
type /*caret*/WithSelf = []WithSelf | StringOrInt
type /*caret*/FooOrInt = Foo | int
type /*caret*/Foo1<T> = Some<T> | Some2<T, int>
pub type /*caret*/Foo2<T, U> = Some2<T, U> | Some2<T, int> | Some2<U, int>
