module validity

map := map[string]int{}
<error descr="Immutable variable 'map' cannot be reassigned">map</error>["1"] = 0
<error descr="Immutable variable 'map' cannot be reassigned">map</error>["1"]["2"] = 0
(<error descr="Immutable variable 'map' cannot be reassigned">map</error>["1"]) = 0

arr := [2]int{}
<error descr="Immutable variable 'arr' cannot be reassigned">arr</error>[0] = 0
<error descr="Immutable variable 'arr' cannot be reassigned">arr</error>[1] = 0
<error descr="Immutable variable 'arr' cannot be reassigned">arr</error>[0][1] = 0

mut mut_map := map[string]int{}
mut_map["1"] = 0
mut_map["1"]["2"] = 0
(mut_map["1"]) = 0

mut mut_arr := [2]int{}
mut_arr[0] = 0
mut_arr[1] = 0
mut_arr[0][1] = 0
