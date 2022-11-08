module new

struct Book {
	author struct {
		name string
		age  int
	}

	title string
}

fn main() {
	book := Book{
		author: struct {
			name: 'Samantha Black'
			age: 24
		}
	}
	assert book.author.name == 'Samantha Black'
	assert book.author.age == 24
}
