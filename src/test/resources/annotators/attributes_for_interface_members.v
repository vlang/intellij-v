module annotators

interface Foo {
	name string <error descr="Attributes are not allowed for interface members">[deprecated: 'ss']</error>
	foo() string <error descr="Attributes are not allowed for interface members">[deprecated: 'ss']</error>
}
