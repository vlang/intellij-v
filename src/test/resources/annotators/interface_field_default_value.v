module annotators

interface IFoo {
	name string <error descr="Interface field cannot have a default value">= ""</error>
	age int <error descr="Interface field cannot have a default value">= 100</error>
}
