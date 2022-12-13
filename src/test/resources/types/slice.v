module types

a := [1, 2, 3]
b := a[1..2]
expr_type(b, '[]int')
c := a#[1..2]
expr_type(c, '[]int')
