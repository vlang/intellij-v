module anon_structs

struct /*caret*/Book {
	/*caret*/author struct {
		name string
		/*caret*/age  int
	}

	title string
}
