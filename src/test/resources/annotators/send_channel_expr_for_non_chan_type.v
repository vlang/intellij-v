module annotators

ch := chan int{}

ch <- 1

arr_chan := [chan int{}]
arr_chan[0] <- 1

arr := [1, 2, 3]
<error descr="Cannot push on non-channel `[]int`, left expression of 'send' operator must be 'chan T' type">arr <- 1</error>

map := {
	1: 1
	2: 2
	3: 3
}
<error descr="Cannot push on non-channel `map[string]int`, left expression of 'send' operator must be 'chan T' type">map <- 1</error>

int_var := 1
<error descr="Cannot push on non-channel `int`, left expression of 'send' operator must be 'chan T' type">int_var <- 1</error>
