module types

struct Book {
	author struct {
		name string
		age  int
	}

	title string
}

fn main() {
	book := Book{}
	expr_type(book.author, 'struct {
   name string
   age int
}')
}
