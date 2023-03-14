module annotators

fn some(a int) {}

arr := []int{len: 100, init: index}
arr2 := []int{len: 100, init: index * 2}
arr3 := []int{len: 100, init: some(index)}
arr4 := []int{len: 100, init: some(index * 2)}

arr.map([]int{len: 1, init: index * 2})
arr.map(it * 2)

arr_ok := []int{len: 100, init: index * 2}
