module validity

arr := []int{}
<error descr="Cannot append to immutable variable 'arr'">arr</error> << 1

mut arr_mut := []int{}
arr_mut << 1
