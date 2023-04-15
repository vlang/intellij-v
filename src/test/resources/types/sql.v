module types

[table: 'code_storage']
struct CodeStorage {
	id   int    [primary; sql: serial]
	code string [nonull]
	hash string [nonull]
}

fn main() {
	found1 := sql db {
		select from CodeStorage where id == hash
	}
	expr_type(found1, '![]CodeStorage')

	found2 := sql db {
		select from CodeStorage where code == hash
	}
	expr_type(found2, '![]CodeStorage')

	found3 := sql db {
		select from CodeStorage where code == hash limit 1
	}
	expr_type(found3, '![]CodeStorage')

	count := sql db {
		select count from CodeStorage where id == hash
	}
	expr_type(count, 'int')
}
