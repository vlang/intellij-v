module types

[table: 'code_storage']
struct CodeStorage {
	id   int    [primary; sql: serial]
	code string [nonull]
	hash string [nonull]
}

fn main() {
	found := sql db {
		select from CodeStorage where id == hash
	}
	expr_type(found, '[]CodeStorage')

	count := sql db {
		select count from CodeStorage where id == hash
	}
	expr_type(count, 'int')
}
