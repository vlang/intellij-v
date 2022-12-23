module types

expr_type(typeof(100), 'stubs.TypeInfo')
expr_type(typeof(100).name, 'string')
expr_type(typeof(100).idx, 'int')

expr_type(dump(100), 'int')
expr_type(@COLUMN, 'string')

const @my_const = 100
expr_type(@my_const, 'int')

fn @fun() int {
	return 100
}
expr_type(@fun(), 'int')
