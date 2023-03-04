module validity

map := map[string]int{}
<warning descr="Immutable variable 'map' cannot be reassigned">map</warning>["1"] = 0
<warning descr="Immutable variable 'map' cannot be reassigned">map</warning>["1"]["2"] = 0
(<warning descr="Immutable variable 'map' cannot be reassigned">map</warning>["1"]) = 0

arr := [2]int{}
<warning descr="Immutable variable 'arr' cannot be reassigned">arr</warning>[0] = 0
<warning descr="Immutable variable 'arr' cannot be reassigned">arr</warning>[1] = 0
<warning descr="Immutable variable 'arr' cannot be reassigned">arr</warning>[0][1] = 0

mut mut_map := map[string]int{}
mut_map["1"] = 0
mut_map["1"]["2"] = 0
(mut_map["1"]) = 0

mut mut_arr := [2]int{}
mut_arr[0] = 0
mut_arr[1] = 0
mut_arr[0][1] = 0
