module validity

arr := []int{}
<warning descr="Cannot append to immutable variable 'arr'">arr</warning> << 1

mut arr_mut := []int{}
arr_mut << 1
