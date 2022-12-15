module types

expr_type(typeof(100), 'TypeInfo')
expr_type(typeof(100).name, 'string')
expr_type(typeof(100).idx, 'int')

expr_type(dump(100), 'int')
