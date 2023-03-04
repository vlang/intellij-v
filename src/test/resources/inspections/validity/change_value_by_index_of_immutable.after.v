module validity

mut map := map[string]int{}
map["1"] = 0
map["1"]["2"] = 0
(map["1"]) = 0

mut arr := [2]int{}
arr[0] = 0
arr[1] = 0
arr[0][1] = 0

mut mut_map := map[string]int{}
mut_map["1"] = 0
mut_map["1"]["2"] = 0
(mut_map["1"]) = 0

mut mut_arr := [2]int{}
mut_arr[0] = 0
mut_arr[1] = 0
mut_arr[0][1] = 0
