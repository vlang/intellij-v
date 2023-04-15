module main

struct <error descr="Struct name must start with uppercase letter">foo</error> {}
interface <error descr="Interface name must start with uppercase letter">ifoo</error> {}
union <error descr="Union name must start with uppercase letter">ufoo</error> {}
enum <error descr="Enum name must start with uppercase letter">efoo</error> {
	red
}

struct <error descr="Name must be at least 2 characters long">f</error> {}
interface <error descr="Name must be at least 2 characters long">i</error> {}
union <error descr="Name must be at least 2 characters long">u</error> {}
enum <error descr="Name must be at least 2 characters long">e</error> {
	red
}
