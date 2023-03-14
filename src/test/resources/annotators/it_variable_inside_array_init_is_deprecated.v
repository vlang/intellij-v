module annotators

fn some(a int) {}

arr := []int{len: 100, init: <warning descr="'it' is deprecated, use 'index' instead">it</warning>}
arr2 := []int{len: 100, init: <warning descr="'it' is deprecated, use 'index' instead">it</warning> * 2}
arr3 := []int{len: 100, init: some(<warning descr="'it' is deprecated, use 'index' instead">it</warning>)}
arr4 := []int{len: 100, init: some(<warning descr="'it' is deprecated, use 'index' instead">it</warning> * 2)}

arr.map([]int{len: 1, init: <warning descr="'it' is deprecated, use 'index' instead">it</warning> * 2})
arr.map(it * 2)

arr_ok := []int{len: 100, init: index * 2}
