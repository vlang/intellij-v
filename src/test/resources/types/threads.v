module types

fn expensive_computing(i int) int {
	return i * i
}

int_thread := spawn fn () int { return 1 }()
expr_type(int_thread, 'thread int')
expr_type(int_thread.wait(), 'int')

arr_string_thread := spawn fn () []string { return [''] }()
expr_type(arr_string_thread, 'thread []string')
expr_type(arr_string_thread.wait(), '[]string')

mut threads := []thread int{}
for i in 1 .. 10 {
	threads << spawn expensive_computing(i)
}
r := threads.wait()
expr_type(r, '[]int')
